#!/bin/bash
cd $1 2>&1
if (( $# == 3 ))
then
	$2 < $3 2>&1
else
	$2 2>&1
fi
