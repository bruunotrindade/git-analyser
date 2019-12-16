#!/bin/bash

REPS_FOLDER=~/Reps/
REPS=$(ls $REPS_FOLDER | awk '{print $1}')

for x in $REPS
do
	echo "Resetando $x..."
	cd $REPS_FOLDER$x
    git reset --hard
done
