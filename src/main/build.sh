#!/bin/bash

set -xe

node scripts/cover_yaml_to_json.js data_resources resources
( cd resources && zip -ur ../../../../../mods/sil_yoni-1.3.0.jar data/ assets/ )
