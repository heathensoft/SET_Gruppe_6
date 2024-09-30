plugins {
    `java-library`
    id("no.hiof.set.g6.common-conventions")
}

dependencies {
    api(project(":datatype"))

    // Driver for MwSQL JDBC (Windows Only i think)
    // This also requires MySQL to be installed on the machine.
    implementation(files("lib/mysql-connector-j-9.0.0.jar"))

    /*
        // Java SimpleNet API (Could use this, but let's go for "Netty" instead)
        implementation("org.slf4j:slf4j-simple:2.0.16")
        implementation("com.github.jhg023:SimpleNet:1.6.6")
    */

    // Netty. client / server framework: https://netty.io/index.html
    // Tried and tested. Well documented
    implementation("io.netty:netty-all:4.1.113.Final")


}

