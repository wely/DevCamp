var fs = require('fs');
var mime = require('mime');
var azure = require('azure-storage');

// Instantiage Blob Storage service
var blobService = azure.createBlobService();

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