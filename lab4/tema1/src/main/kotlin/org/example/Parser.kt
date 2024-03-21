package org.example

import com.charleskorn.kaml.Yaml
import khttp.get
import kotlinx.serialization.decodeFromString
import org.jsoup.Jsoup

interface Parser {
    val item: String
    fun parse(text: String): Map<String, String>
}

class JsonParser(override val item: String) : Parser {
    override fun parse(text: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map[item] = get(text).jsonObject.getString(item)
        return map
    }
}

class XmlParser(override val item: String): Parser {
    override fun parse(text: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map[item] = Jsoup.connect(text).get().select(item).text()
        return map
    }
}

class YamlParser(override val item: String): Parser {
    override fun parse(text: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val response = get(text)
        if (response.statusCode == 200) {
            val yamlData = response.text
            map["item"] = Yaml.default.decodeFromString<String>(yamlData)
        } else {
            println("Eroare de link!")
        }
        return map
    }
}