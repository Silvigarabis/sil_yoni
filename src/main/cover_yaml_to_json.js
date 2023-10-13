#!/usr/bin/env node

import * as fs from "fs";
import * as path from "path";
import * as jsYaml from "js-yaml";

const dirs = ["data_resources"];
const files = [];

while (dirs.length > 0){
    const dir = dirs.pop();
    for (const fname of fs.readdirSync(dir)){
        const file = path.join(dir, fname);
        const stat = fs.statSync(file);
        if (stat.isFile()){
            if (fname.endsWith(".yml") || fname.endsWith(".yaml")){
                files.push(file);
            }
        } else if (stat.isDirectory()){
            dirs.push(file);
        }
    }
}

for (const f of files){
   let newJsonFile;
   if (f.endsWith(".yml")){
      newJsonFile = f.slice(0, f.lastIndexOf(".yml"));
   } else {
      newJsonFile = f.slice(0, f.lastIndexOf(".yaml"));
   }
   newJsonFile += ".json";
   newJsonFile = newJsonFile.replace(/data_resources/, "resources");
   console.log(`${f} -> ${newJsonFile}`);
   const yamlText = fs.readFileSync(f, "utf8") ?? "";
   const object = jsYaml.load(yamlText);
   const jsonText = JSON.stringify(object) ?? "{}";
   fs.mkdirSync(path.dirname(newJsonFile), { recursive: true });
   fs.writeFileSync(newJsonFile, jsonText, { flag: 'w+' });
   //console.log(jsonText);
}
