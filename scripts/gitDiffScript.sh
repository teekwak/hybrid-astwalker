#!/bin/bash

# takes in two hash codes of commits and outputs comparison
# if first commit, then compare against nothing
# $1 = directory
# $2 = first hash code
# $3 = second hash code

cd $1

git diff-tree --numstat $2 $3
