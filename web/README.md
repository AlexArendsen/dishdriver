# Web

This directory is for the code that runs our thin web service that allows our
app to communicate with our database.

This web service will be a [NodeJS](https://nodejs.org/en/) application that
uses the lightweight Restify framework to handle web requests, and the MySQL
client provided by the `mysqljs` project as a database client:

<https://github.com/restify/node-restify>

<https://github.com/mysqljs/mysql>

## Running

System requirements:

* [NodeJS](https://nodejs.org/en/), and
* [MariaDB](http://mariadb.org/) running with the DishDriver database initialized,

Install local NodeJS dependencies:

    npm install

Then run it!

    node dishdriver-web.js

Go to `localhost:6789/ping` to test that everything works. The following
message should be shown:

    {"message":"You have reached the DishDriver web service"}

See `config.yml` to tweak the web service config.
