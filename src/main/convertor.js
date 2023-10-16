#!/usr/bin/env node

import * as fs from "fs";
import * as path from "path";
import * as jsYaml from "js-yaml";

export function fromJsonToYaml(inputDir, outputDir){
    const files = searchFile2(inputDir, (f) => f.endsWith(".json"));
    
    for (const f of files){
        let newFile = path.join(outputDir, setPrefix(path.basename(f), "yml"));
        console.log(`${f} -> ${newFile}`);
        let data = readJsonFile(f);
        writeYamlFile(newFile, data);
    }
}

export function fromYamlToJson(inputDir, outputDir){
    const files = searchFile2(inputDir, (f) => f.endsWith(".yml") || f.endsWith(".yaml"));
    
    for (const f of files){
        let newFile = path.join(outputDir, setPrefix(path.basename(f), "json"));
        console.log(`${f} -> ${newFile}`);
        let data = readYamlFile(f);
        writeJsonFile(newFile, data);
    }
}

function searchFile(path, filter){
    const inputs = [path];
    const matches = [];
    while (inputs.length > 0){
        const file = inputs.pop();
        if (!fs.statSync(file).isDirectory()){
            if (filter(file)){
                matches.push(file);
            }
        } else {
            const files = fs.readdirSync(file).map(fname => path.join(file, fname));
            inputs.push(...files);
        }
    }
    return matches;
}

function searchFile2(path, filter){
    const inputs = [path];
    const matches = [];
    while (inputs.length > 0){
        const file = inputs.pop();
        if (!fs.statSync(file).isDirectory()){
            if (filter(file)){
                matches.push(file);
            }
        } else {
            for (const fname of fs.readdirSync(file)){
                const file = path.join(file, fname);
                const stat = fs.statSync(file);
                if (stat.isDirectory()){
                    matches.push(file);
                } else {
                    if (filter(file)){
                        matches.push(file);
                    }
                }
            }
        }
    }
    return matches;
}

function setPrefix(fileName, prefix){
    let fileName = path.basename(f);
    let fileDir = path.dirname(f);
    let name = fileName.slice(0, f.lastIndexOf(".")) + "." + prefix;
    return path.join(fileDir, fileName);
}

function readFile(file){
    return fs.readFileSync(file, "utf8");
}
function writeFile(file, data){
    fs.mkdirSync(path.dirname(file), { recursive: true });
    fs.writeFileSync(file, data, { flag: 'w+' });
}

function readJsonFile(file){
    let json = readFile(file);
    json = json.replace(/(?:\/\*[\s\S]*?\*\/|\/\/.*$/gm, (comment) => {
        const spaces = new Array(comment.length);
        spaces.fill(" ");
        return spaces.join("");
    ]);
    
    let data;
    try {
        data = JSON.parse(json);
    } catch(e){
        quit(`读取时出现错误: ${e.message}`);
   }
    return data;
}

function writeJsonFile(file, data){
    let json = JSON.stringify(data, 4);
    writeFile(file, json);
}

function readYamlFile(file){
    let yaml = readFile(file);
    let data;
    try {
        data = jsYaml.load(yaml);
    } catch(e){
        const reason = e.reason;
        const { snippet, column, position, line } = e.mark;
        quit(`读取时出现错误: line ${line}, column ${column}\n${reason}\n${snippet}`);
   }
    return data;
}

function writeYamlFile(file, data){
    let yaml = jsYaml.dump(data);
    writeFile(file, yaml);
}

function exit(...data){
    if (data.length > 0)
        console.log(...data);
    process.exit(0);
    throw new Error("stopped by exit");
}

function quit(...data){
    if (data.length > 0)
        console.log(...data);
    process.exit(1);
    throw new Error("stopped by quit");
}

