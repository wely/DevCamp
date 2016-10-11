var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var cookieParser = require('cookie-parser');
var expressSession = require('express-session');
var bodyParser = require('body-parser');
var pug = require('pug');
var passport = require('passport');

module.exports.setup = function (app) {

    // Setup Express Middleware
    app.use(expressSession({ secret: 'contoso secret', resave: true, saveUninitialized: false }));
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(cookieParser());
    app.use(express.static(path.join(__dirname, '../public')));

    // Setup View Engine
    app.set('views', path.join(__dirname, '../views'));
    app.set('view engine', 'pug');

    // Configure Passport authentication
    app.use(passport.initialize());
    app.use(passport.session());

    // Error handlers

};