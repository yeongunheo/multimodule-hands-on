tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":kitchenpos-domain"))
    implementation(project(":module:util"))
}