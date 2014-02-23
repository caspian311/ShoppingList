var ObjectID = require('mongodb').ObjectID
   , base = require('./base')

var ShoppingItems = function() {
   var collection = function() {
      return db.collection('shoppingItems');
   }

   this.updateItem = function(item, callback) {
      var checked = isChecked ? 1 : 0;
      base.inConnection(function(db) {
         collection().update({ '_id': ObjectID(item.id) }, item, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };

   this.addItem = function(item, callback) {
      base.inConnection(function(db) {
         collection().insert(item, function(err, addedItems) {
            if (err) {
               throw err;
            }

            callback(addedItems[0]);
         });
      });
   };

   this.getAllItems = function(callback) {
      base.inConnection(function(db) {
         collection().find({}).toArray(function(err, docs) {
            if (err) {
               throw err;
            }

            callback(docs);
         });
      });
   };

   this.deleteItem = function(id, callback) {
      base.inConnection(function(db) {
         collection().remove({ '_id': ObjectID(id) }, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };
};

module.exports = new ShoppingItems();

