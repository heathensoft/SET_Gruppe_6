plugins {
    `java-library`
    id("no.hiof.set.g6.common-conventions")
}

dependencies {
    api(project(":datatype"))
    implementation(files("lib/mysql-connector-j-9.0.0.jar"))
}

