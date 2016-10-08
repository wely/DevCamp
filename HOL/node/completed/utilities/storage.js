// Setup Azure Storage
var azure = require('azure-storage');

// Configure Queue Storage
var queueService = azure.createQueueService();
queueService.messageEncoder = new azure.QueueMessageEncoder.TextBase64QueueMessageEncoder();

// Configure Blob Storage
var blobService = azure.createBlobService();

module.exports.getQueueService = function () {

    return queueService;

};

module.exports.getBlobService = function () {

    return blobService;

};