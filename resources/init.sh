#!/bin/bash

# $1 = listOfPeers
# $2 = password to pi

echo
echo "===== Running init.sh ====="
echo

cd /home/pi/astwalker/resources;
bash createConfigProperties.sh;
cd ..;
java -jar astwalker-1.0-SNAPSHOT-jar-with-dependencies.jar resources/config.properties;

echo 
echo "===== Process completed successfully ====="
echo