
//    Simple Snippet that takes Lambda Event and puts on SQS Queue

const {SQSClient, SendMessageCommand} = require("@aws-sdk/client-sqs");
const client = new SQSClient();
const sqs_queue = process.env.SQS_QUEUE_URL;
exports.handler = async function (event) {
    const params = {
        MessageBody: JSON.stringify(event),
        QueueUrl: sqs_queue
    };
    const response = await client.send(new SendMessageCommand(params));
    console.log("Success, message sent. MessageID:", response.MessageId);
    return {status: 'OK'};
};


