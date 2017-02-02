#!/bin/bash

# $1 = repoFileName

git log --reverse --format="format:Commit: %H%nAuthor: %an%n" -- $1 >fail2.txt 2>&1
