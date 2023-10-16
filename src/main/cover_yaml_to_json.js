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
   let object
   try {
      object = jsYaml.load(yamlText);
   } catch(e){
      const reason = e.reason;
      const { snippet, column, position, line } = e.mark;
      console.log(`读取时出现错误: line ${line}, column ${column}\n${reason}\n${snippet}`);
      process.exit(1);
      break;
   }
   const jsonText = JSON.stringify(object) ?? "{}";
   fs.mkdirSync(path.dirname(newJsonFile), { recursive: true });
   fs.writeFileSync(newJsonFile, jsonText, { flag: 'w+' });
   //console.log(jsonText);
}
