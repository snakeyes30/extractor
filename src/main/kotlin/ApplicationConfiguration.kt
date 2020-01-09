package org.sam.extractor.configuration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

fun main(vararg args: String) {
    SpringApplication.run(ExtractorApplicationConfiguration::class.java, *args)
}
@SpringBootApplication
open class ExtractorApplicationConfiguration {

}
