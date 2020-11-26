package org.jetbrains

data class Project(val isKotlinBuild: Boolean = false,
              val javaSources: Sources?,
              val kotlinSources: Sources?) {
    fun checkValidity(moduleName:String){
        if(kotlinSources != null && !isKotlinBuild) throw Exception("Wrong configuration: " +
                "module $moduleName has kotlin sources, " +
                "but was not marked for Kotlin build! " +
                "(Add  \"isKotlinBuild\": true to your config)")
        //TODO check dependency to sources
    }
    fun getAllDependencies() = (getJavaSourceDependencies() + getKotlinSourceDependencies()).distinct()

    fun getJavaSourceDependencies() = getAllSourceDependencies(javaSources)
    fun getKotlinSourceDependencies() = getAllSourceDependencies(kotlinSources)

    private fun getAllSourceDependencies(sources: Sources?): List<String>{
        if(sources == null) return emptyList()

        val dependencies = ArrayList<String>()
        dependencies.addAll(sources.javaDep.toTypedArray())
        dependencies.addAll(sources.kotlinDep.toTypedArray())
        return dependencies.distinct()
    }
}

data class Sources(val kotlinDep: List<String> = emptyList(), val javaDep: List<String> = emptyList()) {

}