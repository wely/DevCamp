var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function (req, res) {
    
    // Render view
    res.render('index', {
        title: 'City Power & Light',
        home: true
    });

});

module.exports = router;