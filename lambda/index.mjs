import https from 'https';
import { URL } from 'url';
import { S3Client, HeadObjectCommand } from '@aws-sdk/client-s3';

export const handler = async (event) => {
    console.log('🟡 SQS Event 전체:', JSON.stringify(event, null, 2));
    const batchItemFailures = [];

    for (const record of event.Records) {
        try {
            console.log("🔄 처리 중인 SQS 메시지:", record);

            const body = JSON.parse(record.body);
            console.log("🧾 파싱된 메시지 body:", body);

            if (body?.Event === 's3:TestEvent') {
                console.log("📦 Ignoring s3:TestEvent from LocalStack");
                continue;
            }

            const s3Record = body.Records?.[0];
            if (!s3Record) throw new Error("❌ Records[0] 누락됨");

            const bucket = s3Record?.s3?.bucket?.name;
            const key = s3Record?.s3?.object?.key;
            const size = s3Record?.s3?.object?.size;

            console.log("📂 버킷:", bucket, "🗂️ 키:", key, "📏 사이즈:", size);

            if (!bucket || !key) throw new Error("❗ 잘못된 S3 이벤트입니다.");

            const userMetadata = await getUserMetaData(bucket, key);
            console.log("📝 사용자 메타데이터:", userMetadata);

            const callbackBody = createBody(userMetadata, bucket, key, size);
            console.log("📬 콜백 요청 바디:", callbackBody);

            await postToServer("http://host.docker.internal:8080/api/upload-callback", callbackBody);
            console.log("✅ 콜백 요청 성공");

        } catch (error) {
            console.error("❌ 레코드 처리 실패:", error);
            batchItemFailures.push({ itemIdentifier: record.messageId });
            continue;
        }
    }

    return { batchItemFailures };
};

function postToServer(endpoint, body) {
    const url = new URL(endpoint);
    return new Promise((resolve, reject) => {
        const req = https.request({
            hostname: url.hostname,
            port: url.port || 443,
            path: url.pathname,
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Content-length': Buffer.byteLength(JSON.stringify(body))
            },
        }, (res) => {
            res.statusCode >= 200 && res.statusCode < 300
                ? resolve()
                : reject(new Error(`Request failed with status code ${res.statusCode}`));
        });

        req.on('error', reject);
        req.write(JSON.stringify(body));
        req.end();
    });
}

const getUserMetaData = async (bucket, key) => {
    try {
        const s3 = new S3Client({ region: "ap-northeast-2" });
        const command = new HeadObjectCommand({ Bucket: bucket, Key: key });
        const response = await s3.send(command);
        return response.Metadata;
    } catch (err) {
        console.error("❌ Failed to fetch metadata:", err);
        return {};
    }
};

function createBody(userMetadata, bucket, key, size) {
    return {
        bucket,
        driveId: userMetadata["driveid"],
        mimeType: userMetadata["mimetype"],
        fileKey: key,
        parentId: userMetadata["parentid"],
        fileName: userMetadata["filename"],
        size
    };
}