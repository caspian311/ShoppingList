var ObjectID = require('mongodb').ObjectID
   , base = require('./base')

var ShoppingItems = function() {
   var collection = function(db) {
      return db.collection('shoppingItems');
   }

   this.updateItem = function(id, item, callback) {
      base.inConnection(function(db) {
         collection(db).update({ '_id': ObjectID(id) }, item, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };

   this.addItem = function(item, callback) {
      base.inConnection(function(db) {
         collection(db).insert(item, function(err, addedItems) {
            if (err) {
               throw err;
            }

            callback(addedItems[0]);
         });
      });
   };

   this.getAllItems = function(callback) {
      base.inConnection(function(db) {
         collection(db).find({}).toArray(function(err, docs) {
            if (err) {
               throw err;
            }

            callback(docs);
         });
      });
   };

   this.deleteItem = function(id, callback) {
      base.inConnection(function(db) {
         collection(db).remove({ '_id': ObjectID(id) }, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };
};

module.exports = new ShoppingItems();

