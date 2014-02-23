var shoppingItemsDb = require('../db/shoppingItemsDb')
   , changesDb = require('../db/changesDb')

var ShoppingItems = function() {
   this.index = function(request, response) {
      shoppingItemsDb.getAllItems(function(docs) {
         response.json(docs.map(function(doc) {
            return {
               id: doc._id,
               name: doc.name,
               checked: doc.checked
            };
         }));
      });
   };

   this.post = function(request, response) {
      var item = {
         name: request.body.name,
         checked: 0
      };

      shoppingItemsDb.addItem(item, function(itemInDb) {
         response.status(201);
         response.end();
      });
   };

   this.delete = function(request, response) {
      var id = request.params.id;

      shoppingItemsDb.deleteItem(id, function() {
         response.status(201);
         response.end();
      });
   };

   this.put = function(request, response) {
      var id = request.params.id;
      var item = request.body;
      var item.checked = request.body.checked == 'true';

      shoppingItemsDb.markItem(id, item, function(docs) {
         response.status(201);
         response.end();
      });
   };
}

module.exports = new ShoppingItems();

