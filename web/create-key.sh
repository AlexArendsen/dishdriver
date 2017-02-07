#!/bin/bash

KEYNAME=dishdriver-key.pem
CERTNAME=dishdriver-crt.pem

openssl req -x509 -newkey rsa:2048 -keyout dishdriver-key.pem -out dishdriver-crt.pem -days 90
