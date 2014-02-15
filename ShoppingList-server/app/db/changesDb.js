var ObjectID = require('mongodb').ObjectID
   , base = require('./base')

var Changes = function() {
   this.addChange = function(change, callback) {
      base.inConnection(function(db) {
         db.collection('changes').insert(change, function(err) {
            if (err) {
               throw err;
            }

            callback();
         });
      });
   };

   var objectIdWithTimestamp = function(timestamp) {
      var hexSeconds = Math.floor(timestamp/1000).toString(16);
      return ObjectId = ObjectID(hexSeconds + "0000000000000000");
   }

   this.allChangesSince = function(timestamp, callback) {
      base.inConnection(function(db) {
         db.collection('changes').find({ _id: { $gt: objectIdWithTimestamp(timestamp) } }).toArray(function(err, docs) {
            if (err) {
               throw err;
            }
            callback(docs);
         });
      });
   };
};

module.exports = new Changes();

