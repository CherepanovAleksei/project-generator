package org.jetbrains

data class Project(val isKotlinBuild: Boolean = false, val hasKotlinSrc: Boolean = false, val dependencies: List<String>? = null) {
    fun checkValidity(moduleName:String){
        if(hasKotlinSrc && !isKotlinBuild) throw Exception("Wrong configuration: " +
                "module $moduleName has kotlin sources, " +
                "but was not marked for Kotlin build! " +
                "(Add  \"isKotlinBuild\": true to your config)")
    }
}