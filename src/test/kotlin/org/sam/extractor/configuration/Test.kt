package org.sam.extractor.configuration
import  org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
import java.nio.file.Paths

class CalculatorTests {

    @Test
    fun `1 + 1 = 2`() {
        val toList = File("/Users/sam/tmp").walk().onEnter {
            val predicate: (File) -> Boolean = { file -> file.name.contains(".rar") || file.isDirectory }
            val result = (it.isDirectory
                    && it.listFiles()?.any(predicate)!!)
            println("walking $it result $result")

            result
        }.iterator().asSequence().asIterable().filter {
            it.isFile && it.name.endsWith("rar")  }.filter{
            val s = Paths.get(it.parentFile.path, it.nameWithoutExtension +".mkv")
            val s2 = Paths.get(it.parentFile.path, it.nameWithoutExtension + ".avi")
            println ("validating $s")
            !(s.toFile().exists() || s2.toFile().exists())
        } .toList()
//        System.err.println(toList)
        System.err.println(toList.size)

    }
}