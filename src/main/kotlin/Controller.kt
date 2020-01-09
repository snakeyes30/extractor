package org.sam.extractor.configuration

import com.github.junrar.Junrar
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController
class Controller {


    @Value("#{'\${paths}'.split(',')}")
    private val rootPaths: List<String>? = emptyList()

    @RequestMapping(value = ["/files2"], method = [RequestMethod.GET], produces = ["application/json"])
    @ResponseBody
    fun files2(): Node {
        println(rootPaths)
        val flatMap: List<File>? = rootPaths?.flatMap { getFiles(it) }?.sorted()
        val node = Node("Root", arrayListOf(), "")
        flatMap?.forEach { s ->
            // unix specific
            val split = s.absolutePath.split(File.separator).filter { x -> x.isNotEmpty() }
            getNode(node, 0, split)

        }
        return node
    }

    @RequestMapping(value = ["/extract"], method = [RequestMethod.GET], produces = ["application/json"])
    fun extract(@RequestParam("file") file: String): String {
        // unix specific
        val f = File("/" + file)
        Junrar.extract(f.absolutePath, f.parent)
        return "done"
    }

    private fun getNode(root: Node, i: Int, paths: List<String>): Node {
        if (i >= paths.size) {
            return Node("", arrayListOf(), "")
        }
        val current = paths[i]
        val node = root.children.find { n -> n.name == current }
        node?.let { getNode(it, i + 1, paths) } ?: kotlin.run {
            var action = ""
            if (current.endsWith("rar")) {
                // unix specific

                action = Path.of("", *paths.toTypedArray()).toString()
//                        paths.joinToString(File.separator)
            }
            val newNode = Node(current, arrayListOf(), action, action)
            root.children += newNode
            getNode(newNode, i + 1, paths)
        }
        return root

    }

    private fun getFiles(path: String): List<File> {
        println("AA     " + path)
        return File(path).walk().onEnter {
            println("BB     " + it)
            println("BB     can read" + it.canRead())
            println("BB     can isDirectory" + it.isDirectory)
            println("BB     can isDirectory" + it.isHidden)

            val predicate: (File) -> Boolean = { file -> (file.name.contains(".rar")) || file.isDirectory }
            val result = (it.canRead() && it.isDirectory && !Files.isSymbolicLink(it.toPath())
                    && it.listFiles()?.any(predicate)!!)
            println("CC     ")

            result
        }.iterator().asSequence().asIterable().filter {
            it.isFile && it.name.endsWith("rar")
        }.filter {
            println("EE     ")

            val s = Paths.get(it.parentFile.path, it.nameWithoutExtension + ".mkv")
            val s2 = Paths.get(it.parentFile.path, it.nameWithoutExtension + ".avi")
            !(s.toFile().exists() || s2.toFile().exists())
        }
    }


}


