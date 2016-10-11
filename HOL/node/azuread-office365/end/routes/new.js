var fs = require('fs');
var express = require('express');
var router = express.Router();
var request = require('request');
var formidable = require('formidable');
var storageUtility = require('../utilities/storage');
var authUtility = require('../utilities/auth');
var emailUtility = require('../utilities/email');

/* GET new outage */
router.get('/', authUtility.ensureAuthenticated, function (req, res) {
    res.render('new', {
        title: 'Report an Outage'
    });
});

/* POST new outage */
router.post('/', authUtility.ensureAuthenticated, function (req, res) {

    // Parse a form submission with formidable
    var form = new formidable.IncomingForm();
    form.parse(req, (err, fields, files) => {

        // Process the fields into a new incident, upload image, and add thumbnail queue message
        createIncident(fields, files)
            .then(uploadImage)
            .then(addQueueMessage)
            .then(emailConfirmation(req.user))
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
        var apiUrl = `${process.env.INCIDENT_API_URL}/incidents`;

        // POST new incident to API
        request.post(apiUrl, { form: incident, json: true }, function (error, results) {

            // Successfully created a new incident
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

            // Use the storage utility to upload a blob to Azure Storage
            storageUtility.uploadBlob(input).then(function (blob) {
                console.log('Image uploaded');
                resolve(blob);
            });

        }

    });

}

function addQueueMessage(blob) {

    return new Promise(function (resolve, reject) {

        if (blob) {

            storageUtility.createQueueMessage(blob).then(function () {
                resolve();
            });

        }
        else {
            console.log('No message to add to the queue');
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

module.exports = router;