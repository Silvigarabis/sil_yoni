#!/bin/bash

set -xeu
npm ci --no-bin-links
node scripts/cover_yaml_to_json.js "$1" "$2"
