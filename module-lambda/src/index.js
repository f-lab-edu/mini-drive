const https = require('https');
const {URL} = require('url');
const {S3Client, HeadObjectCommand} = require('@aws-sdk/client-s3');
const {Kafka} = require('kafkajs');

// Kafka 클라이언트 생성
const kafka = new Kafka({
    clientId: 'dms-lambda', brokers: ['localhost:9092']
});

// Kafka 프로듀서 생성
const producer = kafka.producer();

const handler = async (event) => {
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

            // await postToServer("http://localhost:8080/api/v1/files/upload/callback", callbackBody);
            // console.log("✅ 콜백 요청 성공");

            await sendToKafka("dms.upload.completed", {
                bucket,
                key,
                size,
                driveId: userMetadata["driveid"],
                fileName: userMetadata["filename"],
                mimeType: userMetadata["mimetype"],
                parentId: userMetadata["parentid"],
                timestamp: new Date().toISOString()
            });

        } catch (error) {
            console.error("❌ 레코드 처리 실패:", error);
            batchItemFailures.push({ itemIdentifier: record.messageId });
        }
    }

    return { batchItemFailures };
};

// kafka에 메시지 전송
async function sendToKafka(topic, message) {
    try {
        await producer.connect();
        await producer.send({
            topic, messages: [{value: JSON.stringify(message)}],
        });
        await producer.disconnect();
        console.log(`📤 Kafka 이벤트 전송 완료 → ${topic}`);
    } catch (err) {
        console.error("❌ Kafka 메시지 전송 실패:", err);
    }
}

// 서버에 콜백 POST 요청
function postToServer(endpoint, body) {
    const url = new URL(endpoint);
    const data = JSON.stringify(body);

    return new Promise((resolve, reject) => {
        const req = https.request({
            hostname: url.hostname,
            port: url.port || 443,
            path: url.pathname,
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', 'Content-Length': Buffer.byteLength(data)
            },
        }, (res) => {
            res.statusCode >= 200 && res.statusCode < 300
                ? resolve()
                : reject(new Error(`Request failed with status code ${res.statusCode}`));
        });

        req.on('error', reject);
        req.write(data);
        req.end();
    });
}

// S3에서 사용자 메타데이터 조회
async function getUserMetaData(bucket, key) {
    try {
        const s3 = new S3Client({ region: "ap-northeast-2" });
        const command = new HeadObjectCommand({ Bucket: bucket, Key: key });
        const response = await s3.send(command);
        return response.Metadata || {};
    } catch (err) {
        console.error("❌ Failed to fetch metadata:", err);
        return {};
    }
}

// 서버 콜백에 보낼 body 생성
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

// Lambda에서 사용할 수 있도록 export
module.exports = {handler};