var fs = require('fs');
var express = require('express');
var router = express.Router();
var request = require('request');
var formidable = require('formidable');
var mime = require('mime');

// Setup Azure Storage
var azure = require('azure-storage');
var blobService = azure.createBlobService();
var queueService = azure.createQueueService();
queueService.messageEncoder = new azure.QueueMessageEncoder.TextBase64QueueMessageEncoder();

/* GET new outage */
router.get('/', function (req, res) {
    res.render('new', { title: 'Report an Outage' });
});

/* POST new outage */
router.post('/', function (req, res) {

    // Parse a form submission
    var form = new formidable.IncomingForm();
    form.parse(req, function (err, fields, files) {

        // Process the fields into a new incident, upload image, and add thumbnail queue message
        createIncident(fields, files)
            .then(uploadImage)
            .then(addQueueMessage)
            .then(function () {

                // Successfully processed form upload
                // Redirect to dashboard
                res.redirect('/dashboard');

            });

    });

});

function createIncident(fields, files) {

    return new Promise(function (resolve, reject) {

        // Build request object
        var incident = {
            "Description": fields.description,
            "Street": fields.addressStreet,
            "City": fields.addressCity,
            "State": fields.addressState,
            "ZipCode": fields.addressZip,
            "FirstName": fields.firstName,
            "LastName": fields.lastName,
            "PhoneNumber": fields.phone,
            "OutageType": "Outage",
            "IsEmergency": (fields.emergency === "on") ? true : false
        };

        // POST new incident to API
        var apiUrl = 'http://incidentapi4w5agyt32vajs.azurewebsites.net/incidents';

        request.post(apiUrl, { form: incident, json: true }, function (error, results) {

            // Successfully created a new incident
            var incidentId = results.body.id;
            resolve([incidentId, files]);

        });

    });

}

function uploadImage(input) {

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
                resolve(blob);

            });

        });

    });

}

function addQueueMessage(blob) {

    return new Promise(function (resolve, reject) {

        // Confirm queue
        queueService.createQueueIfNotExists(process.env.AZURE_STORAGE_QUEUE, function (error, result, response) {

            // Insert new queue message
            queueService.createMessage(process.env.AZURE_STORAGE_QUEUE, blob.name, function (error, result, response) {

                // Successfully created queue message
                resolve();

            });

        });

    });

}

module.exports = router;