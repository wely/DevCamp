// Modules
var express = require('express');
var appInsightsConfiguration = require('./config/appInsights');
var expressConfiguration = require('./config/express');
var authConfiguration = require('./config/auth');
var errorConfiguration = require('./config/errors');

// Setup Application Insights
appInsightsConfiguration.setup();

// Create Express Application
var app = express();

// Configure Locals
var env = process.env.NODE_ENV || 'development';
app.locals.moment = require('moment');

// Configure Express 
expressConfiguration.setup(app);

// Configure Routes
app.use('/', require('./routes/index'));
app.use('/dashboard', require('./routes/dashboard'));
app.use('/new', require('./routes/new'));
app.use('/profile', require('./routes/profile'));

// Configure Authentication
authConfiguration.setup(app);

// Configure error handling
errorConfiguration.setup(app);

// Start Server
app.set('port', process.env.PORT || 3000);
var server = app.listen(app.get('port'), function () {
    console.log('Express server listening on port ' + server.address().port);
});