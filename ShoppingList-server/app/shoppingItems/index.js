var app = require('express')()
   , shoppingItems = require('./shoppingItems')

app.locals.pretty = true;

app.get('/shoppingItems', shoppingItems.index);
app.post('/shoppingItems', shoppingItems.post);
app.put('/shoppingItems/:id', shoppingItems.put);
app.delete('/shoppingItems/:id', shoppingItems.delete);

module.exports = app;

