#!/bin/bash

achieve_name=sil_yoni-1.3.0.jar

should_rebuild_java(){
   local time_info=$(get_time_info $(get_java_source_dir))
   if [[ ${time_info} != ${java_source_time_info} ]]; then
      java_source_time_info="${time_info}"
      echo "${time_info}" > .java_source_time_info
      should_rebuild_datapack
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
   find "$@" -print0 | xargs -0 stat -c "%n %Y"
}

rebuild_java(){
   ( cd .. && gradle build )
   local jarFile=$(realpath "../build/libs/${achieve_name}")
   bash ./afterbuild.sh "${jarFile}" || true
}

rebuild_datapack(){
   ( cd .. && gradle :processResources :processClientResources )
   local jarFile=$(realpath "../build/libs/${achieve_name}")
   (
      cd ../build/resources/
      for d in *; do
         (
            cd "$d"
            if [[ -n ${jarFile} ]]; then
               zip -r "${jarFile}" data assets
            fi
         )
      done
   )
   bash ./afterbuild.sh "${jarFile}" || true
}

get_java_source_dir(){
   echo ../src/ -type f -a \( -name \*.java -o -name \*.json \)
}

get_datapack_source_dir(){
   echo ../src/*/resources/ -type f
}

cd "$(realpath "$(dirname "$BASH_SOURCE")")"

set -u

java_source_time_info=$(cat .java_source_time_info || true)
datapack_source_time_info=$(cat .datapack_source_time_info || true)

while sleep 3; do
   if should_rebuild_java; then
      rebuild_java
   fi
   if should_rebuild_datapack; then
      rebuild_datapack
   fi
done
