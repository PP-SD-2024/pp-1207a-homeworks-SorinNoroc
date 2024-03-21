package org.example

import khttp.get
import khttp.responses.Response
import org.jsoup.nodes.Document
import org.jsoup.Jsoup

class Crawler(private var url: String) {
    private var parser: Parser? = null
    fun getUrl(): String {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }

    private fun getResource(): Response {
        return get(url)
    }

    fun processContent(contentType: String, item: String) {
        parser = when (contentType) {
            "json" -> {
                JsonParser(item)
            }
            "yaml" -> {
                YamlParser(item)
            }
            else -> {
                XmlParser(item)
            }
        }
        println(parser!!.parse(url))
    }
}

fun main() {
    val crawler = Crawler("https://jsoup.org/apidocs/org/jsoup/Connection.html")
    crawler.processContent("xml", "h2")

    crawler.setUrl("https://jsonplaceholder.typicode.com/todos/1")
    crawler.processContent("json", "title")
}