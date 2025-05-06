import https from 'https';
import { URL } from 'url';
import { S3Client, HeadObjectCommand} from '@aws-sdk/client-s3';

export const handler = async (event) => {
    console.log('SQS Event:', JSON.stringify(event, null, 2));
    const batchItemFailures = [];

    for(const record of event.Records){
        try {

            // 이벤트 발생시 S3가 자동으로 제공해주는 S3 이벤트 정보
            const body = JSON.parse(record.body);
            const bucket = body.Records?.[0]?.s3?.bucket?.name;
            const key = body.Records?.[0]?.s3?.object?.key;
            const size = body.Records?.[0]?.s3?.object?.size;

            if(!bucket || !key) throw new Error("❗ 잘못된 S3 이벤트입니다. ");
            const userMetadata = await getUserMetaData(bucket, key);
            const callbackBody = createBody(userMetadata, bucket, key, size);

            await postToServer ("https://localhost:8080/api/upload-callback", callbackBody);

        } catch (error) {
            console.log("❌ Failed to process record:", error);
            batchItemFailures.push({ itemIdentifier: record.messageId });
            continue; // 다음 메시지로 넘어감

        }
    }
    // 성공시 [] 배열리턴 - 성공으로 간주
    return { batchItemFailures };

};

function postToServer(endpoint, body){
    const url = new URL(endpoint);
    return new Promise((resolve,reject) => {
        const req = https.request({
            hostname: url.hostname,
            port: 443,
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
/**
 * 사용자 정의 메타데이터를 가져오는 함수
 */
const getUserMetaData = async(bucket,key)=> {
    try {
        const s3 = new S3Client({ region: "ap-northeast-2"});
        const command = new HeadObjectCommand({ Bucket: bucket, Key: key });
        const response = await s3.send(command);
        console.log("📝 Metadata:", response.Metadata);
        return response.Metadata;
    } catch (err) {
        console.error("❌ Failed to fetch metadata:", err);
    }
}
function createBody(userMetadata,bucket,key,size){
    return {
        "bucket": bucket,
        "driveId": userMetadata["driveid"],
        "mimeType": userMetadata["mimetype"],
        "fileKey": key,
        "parentId": userMetadata["parentid"],
        "fileName" : userMetadata["filename"],
        "size": size
    }
}
