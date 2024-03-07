package my.okhouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FunctionApplication

fun main(args: Array<String>) {
    runApplication<FunctionApplication>(*args)
}