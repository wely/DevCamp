var passport = require('passport');
var OIDCStrategy = require('passport-azure-ad').OIDCStrategy;

var config = {
    creds: {
        returnURL: process.env.AAD_RETURN_URL,
        identityMetadata: 'https://login.microsoftonline.com/common/v2.0/.well-known/openid-configuration', // For using Microsoft you should never need to change this.
        validateIssuer: false,
        clientID: process.env.AAD_CLIENT_ID,
        clientSecret: process.env.AAD_CLIENT_SECRET, // if you are doing a responseType of code or id_token code
        skipUserProfile: true, // for AzureAD should be set to true.
        responseType: 'id_token code', // for login only flows use id_token. For accessing resources use `id_token code`
        responseMode: 'form_post', // For login only flows we should have token passed back to us in a POST
        scope: ['Profile', 'Openid', 'Email', 'User.Read', 'Mail.Send', 'Calendars.ReadWrite'] // additional scopes you may wish to pass
    }
};

module.exports.setup = function (app) {

    //   Passport session setup.

    //   To support persistent login sessions, Passport needs to be able to
    //   serialize users into and deserialize users out of the session.  Typically,
    //   this will be as simple as storing the user ID when serializing, and finding
    //   the user by ID when deserializing.
    passport.serializeUser(function (user, done) {
        done(null, user.email);
    });

    passport.deserializeUser(function (id, done) {
        findByEmail(id, function (err, user) {
            done(err, user);
        });
    });

    // array to hold logged in users
    var users = [];

    var findByEmail = function (email, fn) {
        for (var i = 0, len = users.length; i < len; i++) {
            var user = users[i];
            console.log('we are using user: ', user);
            if (user.email === email) {
                return fn(null, user);
            }
        }
        return fn(null, null);
    };

    //  Use the OIDCStrategy within Passport. 
    // 
    //   Strategies in passport require a `validate` function, which accept
    //   credentials (in this case, an OpenID identifier), and invoke a callback
    //   with a user object.
    passport.use(new OIDCStrategy({
        callbackURL: config.creds.returnURL,
        realm: config.creds.realm,
        clientID: config.creds.clientID,
        clientSecret: config.creds.clientSecret,
        oidcIssuer: config.creds.issuer,
        identityMetadata: config.creds.identityMetadata,
        responseType: config.creds.responseType,
        responseMode: config.creds.responseMode,
        skipUserProfile: config.creds.skipUserProfile,
        scope: config.creds.scope,
        validateIssuer: config.creds.validateIssuer
    },
        function (iss, sub, profile, accessToken, refreshToken, done) {

            console.log(`Email address we received was: ${profile.email}`);
            
            // Add the token to the profile
            // TODO: Add logic for token refreshment
            profile.token = accessToken;
            
            // Asynchronous verification for effect...
            process.nextTick(function () {
                findByEmail(profile.email, function (err, user) {
                    if (err) {
                        return done(err);
                    }
                    if (!user) {
                        // "Auto-registration"
                        users.push(profile);
                        return done(null, profile);
                    }
                    return done(null, user);
                });
            });
        
    }

    ));

    //Routes
    app.get('/', function (req, res) {
        res.render('index', { user: req.user });
    });

    app.get('/login',
        passport.authenticate('azuread-openidconnect', { failureRedirect: '/login' }),
        function (req, res) {
            console.log('Login was called in the Sample');
            res.redirect('/');
        });

    //   Our POST routes

    //   POST /auth/openid
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  The first step in OpenID authentication will involve redirecting
    //   the user to their OpenID provider.  After authenticating, the OpenID
    //   provider will redirect the user back to this application at
    //   /auth/openid/return
    app.post('/auth/openid',
        passport.authenticate('azuread-openidconnect', { failureRedirect: '/login' }),
        function (req, res) {
            console.log('Authentication was called');
            res.redirect('/');
        });

    // GET /auth/openid/return
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  If authentication fails, the user will be redirected back to the
    //   login page.  Otherwise, the primary route function function will be called,
    //   which, in this example, will redirect the user to the home page.
    app.get('/auth/openid/return',
        passport.authenticate('azuread-openidconnect', { failureRedirect: '/login' }),
        function (req, res) {
            res.redirect('/');
        });

    // POST /auth/openid/return
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  If authentication fails, the user will be redirected back to the
    //   login page.  Otherwise, the primary route function function will be called,
    //   which, in this example, will redirect the user to the home page.
    app.post('/auth/openid/return',
        passport.authenticate('azuread-openidconnect', { failureRedirect: '/login' }),
        function (req, res) {
            res.redirect('/');
        });

    app.get('/logout', function (req, res) {
        req.logout();
        res.redirect('/');
    });


    // Simple route middleware to ensure user is authenticated.

    //   Use this route middleware on any resource that needs to be protected.  If
    //   the request is authenticated (typically via a persistent login session),
    //   the request will proceed.  Otherwise, the user will be redirected to the
    //   login page.
    function ensureAuthenticated(req, res, next) {
        if (req.isAuthenticated()) { return next(); }
        res.redirect('/login');
    }

};

module.exports.ensureAuthenticated = function (req, res, next) {
    if (req.isAuthenticated()) {
        return next();
    }
    res.redirect('/login');
};