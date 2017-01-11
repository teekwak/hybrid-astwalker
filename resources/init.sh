#!/bin/bash

# $1 = listOfPeers
# $2 = password to pi

echo
echo "===== Running init.sh ====="
echo

IFS=$'\n'
for line in $(cat $1)
do
	sshpass -p "$2" ssh pi@$line "
		cd /home/pi/astwalker/resources;
		bash createConfigProperties.sh;
		cd ..; 
		java -jar astwalker-1.0-SNAPSHOT-jar-with-dependencies.jar resources/config.properties;
	"
done

echo 
echo "===== Process completed successfully ====="
echo