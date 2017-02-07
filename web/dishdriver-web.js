var restify = require('restify')
var mysql = require('mysql')
var fs = require('fs')
var yaml = require('js-yaml')

// === Globals
var config;
var db;
var server;

// Handler /query requests
function runQuery(request, response, next) {
  if (request.headers['dd-token-client'] != config.clientToken)
    throw "Unauthorized access";
  else if (!(request.params.sql && request.params.args && 1))
    throw "SQL or args missing";
  else
    if (typeof(request.params.sql) != "string" || typeof(request.params.args) != "object")
      throw "SQL or args type mismatched"
    db.query(request.params.sql, request.params.args, function(err, results, fields) {
      response.send({code: "Success", results: results});
    });
} 

function runProcedure(request, response, next) {
  throw "Not yet implemented"
} 

function pong(request, response, next) {
  response.send({message: "You have reached the DishDriver web service"})
}

// Initialization function, run at daemon start
function initialize() {
  // Read configuration file (it's in YAML because I like nice things)
  config = yaml.safeLoad(fs.readFileSync('config.yml'))

  // TODO -- Read the SSL Certificate
  if (config.cert)
    console.warn("WARNING: An SSL certificate is defined, but dishdriver does not yet support SSL-encrypted traffic");

  // Initialize database connection
  db = mysql.createConnection({
    host:     config.db.host || 'localhost',
    user:     config.db.user,
    password: config.db.password || '',
    database: config.db.database
  });

  // Create and configure restify server
  server = restify.createServer(config.restify || undefined)
  server.use(restify.bodyParser(config.bodyParser || undefined));

  // Set up restify routes
  server.post('/query', runQuery);
  server.post('/proc/:name', runProcedure);
  server.get('/ping', pong);
}

// Driver
function main() {
  initialize()

  // Start daemon
  server.listen(config.port, () => { console.log("%s listening on %s", server.name, config.port); })
}

main()
