var restify = require('restify')
var mysql = require('mysql')
var fs = require('fs')
var yaml = require('js-yaml')

// === Globals
var config;
var connection;
var server;

function runQuery(request, response, next) {
  response.send({error: "Not yet implemented"})
} 

function runProcedure(request, response, next) {
  response.send({error: "Not yet implemented"})
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
  connection = mysql.createConnection({
    host:     config.db.host || 'localhost',
    user:     config.db.user,
    password: config.db.secret || '',
    database: config.db.database
  });

  // Set up restify routes
  server = restify.createServer(config.restify || {})
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
