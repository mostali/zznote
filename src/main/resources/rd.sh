#!/bin/bash


rm -rf xnapp.jar && docker cp wb:/home/udavuser/app/xnapp.jar ./ && java -Dapp.name=zn -Dapp.host=o.com:8080 -Dserver.port=8080 -jar xnapp.jar