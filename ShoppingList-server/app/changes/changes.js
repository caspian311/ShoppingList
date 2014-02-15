var changesDb = require('../db/changesDb')

var Changes = function() {
   this.get = function(request, response) {
      var since = request.query.since;
      var timestamp = new Date(parseInt(since));
      changesDb.allChangesSince(timestamp, function(docs) {
         response.json(docs)
      });
   };
}

module.exports = new Changes();

