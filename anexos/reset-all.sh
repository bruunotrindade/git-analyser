#!/bin/bash

REPS_FOLDER=/media/dados/brunotrindade/Reps
REPS=$(ls $REPS_FOLDER | awk '{print $1}')

for x in $REPS
do
	echo "Resetando $x..."
	cd $REPS_FOLDER/$x
	rm .git/*.lock -f
	git reset --hard
done
