var shoppingItemsDb = require('../db/shoppingItemsDb')

var ShoppingItems = function() {
   this.index = function(request, response) {
      shoppingItemsDb.getAllItems(function(docs) {
         response.json(docs.map(function(doc) {
            return {
               id: doc._id,
               value: doc.value,
               checked: doc.checked
            };
         }));
      });
   };

   this.post = function(request, response) {
      var item = {
         value: request.body.value,
         checked: false
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
      var item = {
         value: request.body.value,
         checked: request.body.checked == '1'
      };

      shoppingItemsDb.updateItem(id, item, function(docs) {
         response.status(201);
         response.end();
      });
   };
}

module.exports = new ShoppingItems();

