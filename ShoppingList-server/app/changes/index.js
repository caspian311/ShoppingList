var app = require('express')()
   , cards = require('./changes')

app.locals.pretty = true;

app.get('/changes', cards.get);

module.exports = app;

