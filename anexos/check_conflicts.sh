#!/bin/bash

MAIN_FOLDER=/home/brunotrindade
REPS=$(ls $MAIN_FOLDER/eclipse-workspace/tipmerge/anexos/CSV-geral/ | awk '{print $1}')

for x in $REPS
do
	CONFLITOS=$(cat $MAIN_FOLDER/eclipse-workspace/tipmerge/anexos/CSV-geral/$x | grep "SIM" | wc -l)
    HASH_CONFLITO=$(cat $MAIN_FOLDER/eclipse-workspace/tipmerge/anexos/CSV-geral/$x | grep "SIM" -m 1 | cut -d, -f1)
	echo "$x | $CONFLITOS conflitos (geral)"
    echo "Primeiro conflito: $HASH_CONFLITO"
    echo
done
