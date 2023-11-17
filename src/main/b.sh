#!/bin/bash

function do_update(){
   node cover_yaml_to_json.js
   ( cd resources && zip -ur ../../../../../mods/sil_yoni-1.3.0.jar data/ assets/ )
}

do_update
while sleep 10; do
    do_update
done
