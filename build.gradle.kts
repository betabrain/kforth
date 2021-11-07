plugins {
  kotlin("jvm") version "1.5.31"
  java
  application
  idea
}

repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.github.petitparser:petitparser:2.3.1")
  testImplementation(kotlin("test"))
}

application {
  mainClass.set("kforth.KForthKt")
}

tasks {
  test {
    useTestNG()
  }

  wrapper {
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.BIN
  }
}
