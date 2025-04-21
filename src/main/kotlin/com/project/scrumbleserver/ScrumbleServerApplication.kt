package com.project.scrumbleserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScrumbleServerApplication

fun main(args: Array<String>) {
    runApplication<ScrumbleServerApplication>(*args)
}
