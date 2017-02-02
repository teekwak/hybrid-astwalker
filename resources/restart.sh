#!/bin/bash

echo
echo "===== Running restart.sh ====="
echo

while [ 1 ];
do
	count=0

	# find number of astwalker jar instances (1 means not running, 2 means running)
	IFS=$'\n'
	for line in $(ps ax | grep astwalker-1.0-SNAPSHOT-jar-with-dependencies.jar)
	do
		(( count++ ))
	done

	if [[ $count -lt 2 ]]; then
		echo "Restarting astwalker"
		screen -S ast -p 0 -X stuff $'cd ~/astwalker/resources; bash init.sh;\r'
	fi

	sleep 60;
done
