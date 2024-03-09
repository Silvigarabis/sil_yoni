#!/bin/bash

java_source_time_info=""
datapack_source_time_info=""

should_rebuild_java(){
   local time_info=$(get_time_info $(get_java_source_dir))
   if [[ ${time_info} != ${java_source_time_info} ]]; then
      java_source_time_info="${time_info}"
      return 0
   else
      return 1
   fi
}

should_rebuild_datapack(){
   local time_info=$(get_time_info $(get_datapack_source_dir))
   if [[ ${time_info} != ${datapack_source_time_info} ]]; then
      datapack_source_time_info="${time_info}"
      return 0
   else
      return 1
   fi
}

get_time_info(){
   find "$1" -type f,d | xargs -0 stat -c "%n %Y"
}

rebuild_java(){
   ( cd .. && sh gradlew build )
   local jarFile=$(realpath "../build/libs/sil_yoni-1.3.0.jar")
   bash ./afterbuild.sh "${jarFile}" || true
}

rebuild_datapack(){
   node scripts/cover_yaml_to_json.js "$(get_datapack_source_dir)" ../build/resources/main
   local jarFile=$(realpath "../build/libs/sil_yoni-1.3.0.jar")
   (
      cd ../build/resources/main
      zip -r "${jarFile}" *
   )
   bash ./afterbuild.sh "${jarFile}" || true
}

get_java_source_dir(){
   echo ../src/main/
}

get_datapack_source_dir(){
   echo ../datapack/
}

cd "$(realpath "$(dirname "$BASH_SOURCE")")"
set -u

should_rebuild_datapack
should_rebuild_java
if [[ $# != 0 ]]; then
   rebuild_java
fi

while sleep 3; do
   if should_rebuild_java; then
      rebuild_java
   elif should_rebuild_datapack; then
      rebuild_datapack
   if
done
