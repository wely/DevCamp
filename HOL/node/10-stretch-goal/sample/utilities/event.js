// https://graph.microsoft.io/en-us/docs/api-reference/v1.0/api/user_post_events

function wrapEvent(contacts, fields) {
    var date = getDate();
    var eventAsPayload = {
        subject: 'Outage',
        body: {
            ContentType: 'HTML',
            Content: fields.description
        },
        start: {
            dateTime: formatDate(date),
            timeZone: "Pacific Standard Time"
        },
        end: {
            dateTime: formatDate(new Date(date.setHours(date.getHours() + 1))),
            timeZone: "Pacific Standard Time"
        },
        location: {
            displayName: fields.addressCity
        },
        attendees: contacts
    };
    return eventAsPayload;
}

function getDate() {
    var date = new Date();
     date.setDate(date.getDate() + 1);
     while (!isValidDate(date)) {
         date.setHours(date.getHours() + 3);
     }
     return date;
}

function isValidDate(date) {
    if (date.getHours() < 8) {
        return false;
    }
    if (date.getHours() > 17) {
        return false;
    }
    if (date.getUTCDay() == 0) {
        return false;
    }
    if (date.getUTCDay() == 6) {
        return false;
    }
    return true;
}

function formatDate(date) {
    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "T" + date.getHours() + ":00:00";
}

function generateEvent(fields, contacts) {
    return wrapEvent(contacts, fields);
}

module.exports.generateEvent = generateEvent; 