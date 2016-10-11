var fs = require('fs');
var mime = require('mime');
var azure = require('azure-storage');

// Instantiage Blob Storage services
var blobService = azure.createBlobService();
var queueService = azure.createQueueService();
queueService.messageEncoder = new azure.QueueMessageEncoder.TextBase64QueueMessageEncoder();

module.exports.uploadBlob = function (input) {

    return new Promise(function (resolve, reject) {

        // Define variables to use with the Blob Service
        var stream = fs.createReadStream(input[1].image.path);
        var streamLength = input[1].image.size;
        var options = { contentSettings: { contentType: input[1].image.type } };
        var blobName = input[0] + '.' + mime.extension(input[1].image.type);
        var blobContainerName = process.env.AZURE_STORAGE_BLOB_CONTAINER;

        // Confirm blob container
        blobService.createContainerIfNotExists(blobContainerName, function (containerError) {

            // Upload new blob
            blobService.createBlockBlobFromStream(blobContainerName, blobName, stream, streamLength, options, function (blobError, blob) {

                // Successfully uploaded the image
                console.log('Uploaded image');
                resolve(blob);

            });

        });

    });

}

module.exports.createQueueMessage = function (blob) {

    return new Promise(function (resolve, reject) {

        // Create message object
        var message = {
            BlobContainerName: blob.container,
            BlobName: blob.name
        };

        // Confirm queue
        queueService.createQueueIfNotExists(process.env.AZURE_STORAGE_QUEUE, function (error, result, response) {

            // Insert new queue message
            queueService.createMessage(process.env.AZURE_STORAGE_QUEUE, JSON.stringify(message), function (error, result, response) {

                // Successfully created queue message
                console.log('Added message to queue');
                resolve();

            });

        });

    });

}