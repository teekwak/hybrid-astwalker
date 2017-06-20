#!/bin/bash

# $1 = repoFileName
# $2 = clone directory

cd $2
git log --reverse --format="format:Commit: %H%nAuthor: %an%n" -- $1
