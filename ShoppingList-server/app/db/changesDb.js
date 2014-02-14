var ObjectID = require('mongodb').ObjectID
   , base = require('./base')

var Changes = function() {
   this.allChangesSince = function(timestamp, callback) {
      base.inConnection(function(db) {
         db.collection('changes').find({ }).toArray(function(err, docs) {
            callback(docs);
         });
      });
   };
};

module.exports = new Changes();

