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
      if (typeof(timestamp) == 'string') {
         timestamp = new Date(timestamp);
      }

      var hexSeconds = Math.floor(timestamp/1000).toString(16);
      var constructedObjectId = ObjectId(hexSeconds + "0000000000000000");

      return constructedObjectId
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

