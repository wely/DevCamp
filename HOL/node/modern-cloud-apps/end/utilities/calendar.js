// Detailed documentation at
// https://graph.microsoft.io/en-us/docs/api-reference/v1.0/api/user_post_events

var moment = require('moment');

module.exports.generateEventBody = function (user) {

    // Build Event body
    // Start the event now, and end after 30 minutes
    var body = {
        "subject": "Incident Reported",
        "start": {
            "dateTime": moment().toISOString(),
            "timeZone": "UTC"
        },
        "end": {
            "dateTime": moment().add(30, 'm').toISOString(),
            "timeZone": "UTC"
        }
    };

    return body;

};