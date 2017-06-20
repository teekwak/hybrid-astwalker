#!/bin/bash

# $1 = repo url
# $2 = path to clone

rm -rf $2
mkdir $2
git clone $1 $2
