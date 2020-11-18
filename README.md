# Maven project generator
## Config DSL
```json
{
  "module1" : {
    "java" : {
      "kotlinDep" : [
        "module1"
      ]
    },
    "isKotlinBuild": true
  },
  "asd": {

    "hasKotlinSrc": true,
    "isKotlinBuild": true
  },
  "wewe": {
    "isKotlinBuild": true,
    "dependencies": [
      "asd",
      "qqq"
    ]
  },
  "qqq": {
    "dependencies": [
      "asd"
    ]
  },
  "asd2": {
    "hasKotlinSrc": true,
    "hasJavaSrc" : true,
    "isKotlinBuild": true,
    "dependencies" : [
      
    ]
  }
}
```