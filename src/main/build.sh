#!/bin/bash

set -xe

build1(){
   node scripts/cover_yaml_to_json.js data_resources resources
   (
   cd resources
   zip -r ../../../../../mods/sil_yoni-1.3.0.jar assets/ data/
   )
}
build2(){
   node scripts/cover_yaml_to_json.js data_resources resources
   (cd ../..
   vhomeexec sh gradlew build
   cp build/libs/sil_yoni-1.3.0.jar ../sil_yoni-1.3.0.jar
   )
   cp build/libs/sil_yoni-1.3.0.jar ../../mods/sil_yoni-1.3.0.jar
}

build1



cp ../../../../mods/sil_yoni-1.3.0.jar ../../../sil_yoni-1.3.0.jar
