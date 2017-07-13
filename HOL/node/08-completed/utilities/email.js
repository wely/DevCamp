// https://graph.microsoft.io/en-us/docs/api-reference/v1.0/api/user_post_messages

// The contents of the outbound email message that will be sent to the user
var emailContent = `
<html>

<head>
  <meta http-equiv='Content-Type' content='text/html; charset=us-ascii\'>
  <title></title>
</head>

<body style="font-family:Calibri">
  <div style="width:50%;background-color:#CCC;padding:10px;margin:0 auto;text-align:center;">
    <h1>City Power &amp; Light</h1>
    <h2>New Incident Reported by {{name}}</h2>
    <p>A new incident has been reported to the City Power &amp; Light outage system.</p>   
    <br />
  </div>
</body>

</html>
`;

/**
 * Returns the outbound email message content with the supplied name populated in the text
 * @param {string} name The proper noun to use when addressing the email
 * @return {string} the formatted email body
 */
function getEmailContent(name) {
    return emailContent.replace('{{name}}', name);
}

/**
 * Wraps the email's message content in the expected [soon-to-deserialized JSON] format
 * @param {string} content the message body of the email message
 * @param {string} recipient the email address to whom this message will be sent
 * @return the message object to send over the wire
 */
function wrapEmail(content, recipient) {
    var emailAsPayload = {
        Message: {
            Subject: 'New Incident Reported',
            Body: {
                ContentType: 'HTML',
                Content: content
            },
            ToRecipients: [
                {
                    EmailAddress: {
                        Address: recipient
                    }
                }
            ]
        },
        SaveToSentItems: true
    };
    return emailAsPayload;
}

/**
 * Delegating method to wrap the formatted email message into a POST-able object
 * @param {string} name the name used to address the recipient
 * @param {string} recipient the email address to which the connect email will be sent
 */
function generateMailBody(name, recipient) {
    return wrapEmail(getEmailContent(name), recipient);
}

module.exports.generateMailBody = generateMailBody;