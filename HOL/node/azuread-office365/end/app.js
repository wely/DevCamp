// Modules
var express = require('express');
var experssHelper = require('./utilities/express');
var errorHelper = require('./utilities/errors');
var authHelper = require('./utilities/auth');

// Create Express Application
var app = express();

// Configure Locals
var env = process.env.NODE_ENV || 'development';
app.locals.moment = require('moment');

// Configure Express    
experssHelper.setup(app);

// Configure Routes
app.use('/', require('./routes/index'));
app.use('/dashboard', require('./routes/dashboard'));
app.use('/new', require('./routes/new'));
app.use('/profile', require('./routes/profile'));

// Configure Authentication
authHelper.setup(app);

// Configure Errors
errorHelper.setup(app);

// Start Server
app.set('port', process.env.PORT || 3000);
var server = app.listen(app.get('port'), function () {
    console.log('Express server listening on port ' + server.address().port);
});