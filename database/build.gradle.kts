plugins {
    `java-library`
    id("no.hiof.set.g6.common-conventions")
}

dependencies {
    api(project(":datatype"))
    implementation(files("lib/mysql-connector-j-9.0.0.jar"))
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.github.jhg023:SimpleNet:1.6.6")
}

