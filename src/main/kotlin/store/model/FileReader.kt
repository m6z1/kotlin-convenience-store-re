package store.model

import java.io.File

class FileReader(private val path: String) {

    fun readFile(): List<String> {
        return File(path).useLines { it.toList() }
    }
}