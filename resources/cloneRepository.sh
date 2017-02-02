#!/bin/bash

# $1 = repo url

rm -rf "/home/pi/astwalker/clone"
mkdir "/home/pi/astwalker/clone"
git clone $1 "/home/pi/astwalker/clone"
