val mavenGroup: String by rootProject

group = "$mavenGroup.server-common"

dependencies {
    implementation(project(":api:common"))
    implementation(project(":api:server"))

    implementation(project(":common"))

    implementation(project(":protocol"))

    implementation(rootProject.libs.config)
    compileOnly(rootProject.libs.luckperms)

    compileOnly(rootProject.libs.netty)
}
