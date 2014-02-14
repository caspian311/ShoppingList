var MongoClient = require('mongodb').MongoClient
   , dbHelper = require('./dbHelper')

var DbBase = function() {
   var CONNECTION;

   this.inConnection = function(task) {
      if (!CONNECTION) {
         MongoClient.connect(dbHelper.getConnectionString(), function(err, db) {
               if (err) {
                  throw err;
               }
               CONNECTION = db;
               task(CONNECTION);
            });
      } else {
         task(CONNECTION);
      }
   };
};

module.exports = new DbBase();
