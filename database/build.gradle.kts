plugins {
    `java-library`
    id("no.hiof.set.g6.common-conventions")
}

dependencies {
    api(project(":datatype"))
    //implementation(files("path til driver filen her"))
}

