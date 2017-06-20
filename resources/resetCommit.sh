#!/bin/bash

# $1 = version number
# $2 = clone directory

cd $2
git reset --hard $1
