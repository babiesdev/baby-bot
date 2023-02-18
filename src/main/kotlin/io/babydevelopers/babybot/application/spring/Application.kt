package io.babydevelopers.babybot.application.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("io.babydevelopers.babybot.application.spring.discord")
@ComponentScan("io.babydevelopers.babybot.application.spring.config")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
