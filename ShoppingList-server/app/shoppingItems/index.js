var app = require('express')()
   , shoppingItems = require('./shoppingItems')

app.locals.pretty = true;

app.get('/shoppingItems', shoppingItems.index);

module.exports = app;

