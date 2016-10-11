var fs = require('fs');
var express = require('express');
var router = express.Router();
var request = require('request');
var formidable = require('formidable');
var mime = require('mime');
var authUtility = require('../utilities/auth');
var emailUtility = require('../utilities/email');
var calendarUtility = require('../utilities/calendar');
var storageUtility = require('../utilities/storage');

// Setup Azure Storage
var queueService = storageUtility.getQueueService();
var blobService = storageUtility.getBlobService();

/* GET new outage */
router.get('/', authUtility.ensureAuthenticated, function (req, res) {
    res.render('new', {
        title: 'Report an Outage',
        user: req.user
    });
});

/* POST new outage */
router.post('/', authUtility.ensureAuthenticated, function (req, res) {

    // Store user information
    var user = req.user;

    // Parse a form submission
    var form = new formidable.IncomingForm();
    form.parse(req, (err, fields, files) => {

        // Process the fields into a new incident, upload image, and add thumbnail queue message
        createIncident(fields, files)
            .then(uploadImage)
            .then(addQueueMessage)
            .then(emailConfirmation(user))
            .then(createCalendarEvent(user))
            .then(() => {

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

        // Get API URL from environment variable
        var apiUrl = `https://${process.env.INCIDENT_API_URL}/incidents`;

        // POST new incident to API
        request.post(apiUrl, { form: incident, json: true }, function (error, results) {

            // Successfully created a new incident

            // Clear cache
            // TODO: clear.
            console.log('Created incident');

            var incidentId = results.body.id;
            resolve([incidentId, files]);

        });

    });

}

function uploadImage(input) {

    return new Promise(function (resolve, reject) {

        // Check if no image was uploaded
        if (input[1].image.size === 0) {
            console.log('No image uploaded');
            resolve();
        }
        else {

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

        }

    });

}

function addQueueMessage(blob) {

    return new Promise(function (resolve, reject) {

        if (blob) {

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

        }
        else {
            console.log('No message was added to the queue');
            resolve();
        }

    });

}

function emailConfirmation(user) {

    return new Promise(function (resolve, reject) {

        // Generate email markup
        var mailBody = emailUtility.generateMailBody(user.displayName, user.email);

        // Set configuration options
        var options = {
            url: 'https://graph.microsoft.com/v1.0/me/sendMail',
            json: true,
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + user.token
            },
            body: mailBody
        };

        // POST new message to Graph API
        request(options, function (error, response) {

            console.log('Email confirmation message sent.');
            resolve();

        });

    });

}

function createCalendarEvent(user) {

    return new Promise(function (resolve, reject) {

        // Build out the Event body
        var eventBody = calendarUtility.generateEventBody();

        // Configure HTTP request
        var options = {
            url: 'https://graph.microsoft.com/v1.0/me/events',
            json: true,
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + user.token
            },
            body: eventBody
        };

        // Execute request
        request(options, function (error, results) {

            console.log('Created calendar event.');
            resolve();

        });

    });

}

module.exports = router;