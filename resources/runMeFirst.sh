#!/bin/bash

hostName=`echo $HOSTNAME`

echo "# Configuration file for IndexManager.java" >> config.properties
echo "#" >> config.properties
echo "# Please note the paths are relative to the .jar file" >> config.properties
echo "#" >> config.properties
echo "# collectionName (required): the name of the collection on the Solr server" >> config.properties
echo "# serverConfigPath (required): the relative path to the serverConfig.properties file" >> config.properties
echo "# passPath (required): the relative path to the password file" >> config.properties
echo "# pathToURLMapPath (required): the relative path to the .txt file with the raw GitHub URLs" >> config.properties
echo "# pathToTimestampsFile (required): the relative path to outputting the timestamps" >> config.properties
echo "# pathToErrorFile (required): the relative path to the .txt file for problematic URLs" >> config.properties
echo "" >> config.properties
echo "collectionName=MoreLikeThisIndex" >> config.properties
echo "serverConfigPath=resources/serverConfig.properties" >> config.properties
echo "passPath=resources/Pass" >> config.properties
echo "pathToURLMapPath=resources/"$hostName"_urls.txt" >> config.properties
echo "pathToTimestampsFile=output/times.txt" >> config.properties
echo "pathToErrorFile=output/errorURLs.txt" >> config.properties