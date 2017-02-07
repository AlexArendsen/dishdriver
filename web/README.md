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
* [MariaDB](http://mariadb.org/) running with the DishDriver database initialized.

For database initialization to work, you will need a system user called
`dishdriver` with the password `yummo`.

Install local NodeJS dependencies:

    npm install

Then run it!

    node dishdriver-web.js

Go to `localhost:6789/ping` to test that everything works. The following
message should be shown:

    {"message":"You have reached the DishDriver web service"}

See `config.yml` to tweak the web service config.

## Development Notes

Requests to `/query` must use the `POST` verb, define `application/json` in the
`Content-Type` header, and have bodies that follow this format:

    {
      "sql": "SELECT Email FROM Users WHERE ID = ?"
      "args": [45]
    }

Request bodies are limited to 8096 byte in size. If larger request bodies are
required, `bodyParser.maxBodySize` in `config.yml` should be adjusted.

Additionally, the server has been configured to honor query requests only if
the request defines the header `dd-token-client` with the value of
`clientToken` defined within `config.yml`. If this header is missing or the
value is not correct, an error will result.

This format will be returned for `INSERT`, `UPDATE`, and `DELETE` querires:

    {
      "code": "Success",
      "results": {
        "fieldCount": 0,
        "affectedRows": 1,
        "insertId": 2,
        "serverStatus": 2,
        "warningCount": 1,
        "message": "",
        "protocol41": true,
        "changedRows": 0
      }
    }

This format will be returned for `SELECT` queries:

    {
      "code": "Success",
      "results": [
        {
          "ID": 1,
          "Email": "mithrandir@ucf.edu",
          "Password": "<a bcrypt password hash>",
          "FirstName": "Gandalf",
          "LastName": "The Gray",
          "DT_Created": "2017-02-07T17:09:21.000Z",
          "DT_LastLogin": null,
          "DT_Cancelled": null
        },
        {
          "ID": 2,
          "Email": "m.underhill@email.com",
          "Password": "<a bcrypt password hash>",
          "FirstName": "Frodo",
          "LastName": "Baggins",
          "DT_Created": "2017-02-07T17:12:33.000Z",
          "DT_LastLogin": null,
          "DT_Cancelled": null
        }
      ]
    }

If `code` is any other value, it indicates that some error has occurred:

    {
      "code": "InternalError",
      "message": "Unauthorized access"
    }
