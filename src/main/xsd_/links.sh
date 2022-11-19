#!/usr/bin/env bash
DIR=$(dirname "$0")
rm "$DIR"/*.xsd
grep -v '^#' "$DIR"/links.txt | xargs wget -nc -P "$DIR" {}
