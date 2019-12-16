#!/bin/bash

MAIN_FOLDER=/media/dados/brunotrindade
REPS=$(ls $MAIN_FOLDER/Reps/ | awk '{print $1}')

for x in $REPS
do
	CONFLITOS=$(cat $MAIN_FOLDER/CSV-geral/$x-geral.csv | grep "SIM" | wc -l)
	LINHAS=$(cat $MAIN_FOLDER/CSV-dev/$x-conflito.csv | wc -l)
	((--LINHAS)) # Removendo o cabeçalho
	echo "$x | $CONFLITOS conflitos (geral) | $LINHAS linhas (dev)"
done
