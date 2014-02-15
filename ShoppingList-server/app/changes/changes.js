var changesDb = require('../db/changesDb')

var Changes = function() {
   this.get = function(request, response) {
      var since = request.query.since;
      changesDb.allChangesSince(since, function(docs) {
         response.json(docs)
      });
   };
}

module.exports = new Changes();

