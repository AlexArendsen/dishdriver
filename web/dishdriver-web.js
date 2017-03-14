var restify = require('restify')
var mysql = require('mysql')
var fs = require('fs')
var yaml = require('js-yaml')
var merge = require('merge')
var bcrypt = require('bcrypt')
var uuid = require('node-uuid')

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

function runLogin(request, response, next) {
  // Check that access token was provided
  if (request.headers['dd-token-client'] != config.clientToken) {
    throw "Unauthorized access";

  // Handle token login
  } else if (request.params.token) {
    db.query("SELECT * FROM Users WHERE SessionToken = ? LIMIT 1", [request.params.token], function(err, results, fields) {
      if (err)
        return response.send(err);
      if (!results || !results.length || !results[0])
        return response.send({code: "LoginFailed", message: "Unrecognized token"});
      db.query("UPDATE Users SET DT_LastLogin = CURRENT_TIMESTAMP() WHERE Id = ?", [results[0].Id])
      return response.send({code: "success", results: [results[0]], message: "POOP! :D"});
    });

  // Handle credential login
  } else if (request.params.email && request.params.password) {
    db.query("SELECT * FROM Users WHERE Email = ? LIMIT 1", [request.params.email], function(err, results, fields) {
      bcrypt.compare(request.params.password, results[0].Password, function(err, result) {
        if (result) {
          var sessionToken = uuid.v4();
          db.query(
            "UPDATE Users SET DT_LastLogin = CURRENT_TIMESTAMP(), SessionToken = ? WHERE Id = ?",
            [sessionToken, results[0].Id],
            () => {}
          );
          results[0].SessionToken = sessionToken;
          response.send({code: "success", results: [results[0]], message: "POOP! :D"});
        } else {
          response.send({code: "LoginFailed", message: "Invalid email or password."});
        }
      });
    });

  // Give up
  } else {
    throw "Missing email or password";
  }
}

function runLogout(request, response, next) {
  // TODO -- We need to store session information somewhere. For now, we'll
  // just say nothing about it...

  response.send({code: "success", message: "Logout Successful!"});
}

function runSignup(request, response, next) {
  throw "Sign up is not yet supported! Sorry! In the meantime, make sure that you're sending email and password. For employee signups, also provide firstname and lastname fields.";
}

function pong(request, response, next) {
  response.send({code: "success", message: "You have reached the DishDriver web service"})
}

// Initialization function, run at daemon start
function initialize() {

  // Read configuration file (it's in YAML because I like nice things)
  config = yaml.safeLoad(fs.readFileSync('config.yml'))

  // Read the SSL Certificate
  cert = (config.cert)? fs.readFileSync(config.cert) : undefined ;
  key  = (config.key) ? fs.readFileSync(config.key)  : undefined ;

  // Initialize database connection
  db = mysql.createConnection({
    host:     config.db.host || 'localhost',
    user:     config.db.user,
    password: config.db.password || '',
    database: config.db.database
  });

  // Create and configure restify server
  server = restify.createServer(merge(
    config.restify || undefined,
    { cert: cert, key: key }
  ))
  server.use(restify.bodyParser(config.bodyParser || undefined));

  // Set up restify routes
  server.post('/query', runQuery);
  server.post('/proc/:name', runProcedure);
  server.post('/login', runLogin);
  server.post('/logout', runLogout);
  server.post('/user/new', runSignup);
  server.get('/ping', pong);
}

// Driver
function main() {
  initialize()

  // Start daemon
  server.listen(config.port, () => { console.log("%s listening on %s", server.name, config.port); })
}

main()
