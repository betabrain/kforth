plugins {
  kotlin("jvm") version "1.5.21"
  application
  idea
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
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
