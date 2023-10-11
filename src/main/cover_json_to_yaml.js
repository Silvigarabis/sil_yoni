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
            if (fname.endsWith(".json")){
                files.push(file);
            }
        } else if (stat.isDirectory()){
            dirs.push(file);
        }
    }
}

for (const f of files){
   let newYamlFile = f.slice(0, f.lastIndexOf(".json")) + ".yml";
   newYamlFile = newYamlFile.replace(/data_resources/, "resources");
   console.log(`${f} -> ${newYamlFile}`);
   const jsonText = fs.readFileSync(f, "utf8");
   const object = JSON.parse(jsonText);
   const yamlText = jsYaml.dump(object);
   fs.mkdirSync(path.dirname(newYamlFile), { recursive: true });
   fs.writeFileSync(newYamlFile, yamlText, { flag: 'w+' });
   //console.log(yamlText);
}
