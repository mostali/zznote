#!/bin/bash


rm -rf znapp.jar && docker cp wb:/home/udavuser/app/znapp.jar ./ && java -Dapp.name=zn -Dapp.host=o.com:8080 -Dserver.port=8080 -jar beaapp.jar