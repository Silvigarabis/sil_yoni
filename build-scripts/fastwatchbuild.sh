#!/bin/bash

should_rebuild_java(){
   local time_info=$(get_time_info $(get_java_source_dir))
   if [[ ${time_info} != ${java_source_time_info} ]]; then
      java_source_time_info="${time_info}"
      echo "${time_info}" > .java_source_time_info
      return 0
   else
      return 1
   fi
}

should_rebuild_datapack(){
   local time_info=$(get_time_info $(get_datapack_source_dir))
   if [[ ${time_info} != ${datapack_source_time_info} ]]; then
      datapack_source_time_info="${time_info}"
      echo "${time_info}" > .datapack_source_time_info
      return 0
   else
      return 1
   fi
}

get_time_info(){
   find "$1" -type f,d -print0 | xargs -0 stat -c "%n %Y"
}

rebuild_java(){
   ( cd .. && gradle build )
   local jarFile=$(realpath "../build/libs/sil_yoni-1.3.0.jar")
   bash ./afterbuild.sh "${jarFile}" || true
}

rebuild_datapack(){
   node scripts/cover_yaml_to_json.js "$(get_datapack_source_dir)" ../build/resources/main
   local jarFile=$(realpath "../build/libs/sil_yoni-1.3.0.jar")
   (
      cd ../build/resources/main
      if [[ -n ${jarFile} ]]; then
         zip -r "${jarFile}" *
      fi
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

java_source_time_info=$(cat .java_source_time_info || true)
datapack_source_time_info=$(cat .datapack_source_time_info || true)

while sleep 3; do
   if should_rebuild_java; then
      rebuild_java
   elif should_rebuild_datapack; then
      rebuild_datapack
   fi
done
