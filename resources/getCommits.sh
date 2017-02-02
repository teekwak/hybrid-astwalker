#!/bin/bash

# $1 = repoFileName

cd "/home/pi/astwalker/clone"
git log --reverse --format="format:Commit: %H%nAuthor: %an%n" -- $1
