# Maven project generator
## Config DSL
Create `config.json` in project root (or use GenerateConfigMain,kt)

Then run `main()` in `GenerateProjectMain.kt`
```json
{
  "module1": {
    "javaSources": {
      "kotlinDep": [
        "module12"
      ],
      "javaDep": [
        "module12"
      ]
    },
    "kotlinSources": {
      "kotlinDep": [
        "module12"
      ],
      "javaDep": [
        "module12"
      ]
    },
    "isKotlinBuild": true
  },
  "module12": {
    "javaSources": {},
    "kotlinSources": {},
    "isKotlinBuild": true
  },
  "module13": {
    "javaSources": {
      "kotlinDep": [
        "module1"
      ],
      "javaDep": [
        "module1"
      ]
    },
    "isKotlinBuild": false
  }
}
```
## Easy config
Create `easyConfig.json` in project root and run `GenerateConfigMain.kt::main`
```json
{
  "numberOfModules" : 10,
  "haveJavaToKotlinDependencies" : false,
  "haveJavaToJavaDependencies" : false,
  "haveKotlinToJavaDependencies" : false,
  "haveKotlinToKotlinDependencies" : true,
  "isAllModulesKotlinBuild" : true
}
```