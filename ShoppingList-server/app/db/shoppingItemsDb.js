var ObjectID = require('mongodb').ObjectID
   , base = require('./base')

var ShoppingItems = function() {
   this.markItem = function(id, isChecked, callback) {
      var checked = isChecked ? 1 : 0;
      base.inConnection(function(db) {
         db.collection('shoppingItems').update({ '_id': ObjectID(id) }, { checked: checked }, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };

   this.addItem = function(item, callback) {
      base.inConnection(function(db) {
         db.collection('shoppingItems').insert(item, function(err, addedItems) {
            if (err) {
               throw err;
            }

            callback(addedItems[0]);
         });
      });
   };

   this.allItems = function(callback) {
      base.inConnection(function(db) {
         db.collection('shoppingItems').find({}).toArray(function(err, docs) {
            if (err) {
               throw err;
            }

            callback(docs);
         });
      });
   };

   this.deleteItem = function(id, callback) {
      base.inConnection(function(db) {
         db.collection('shoppingItems').remove({ '_id': ObjectID(id) }, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };
};

module.exports = new ShoppingItems();

