package com.github.howard12721.trakt.rest.auth

import io.ktor.util.*

internal class HttpBasicAuth : Authentication {
    var username: String? = null
    var password: String? = null

    override fun apply(query: MutableMap<String, List<String>>, headers: MutableMap<String, String>) {
        if (username == null && password == null) return
        val str = (username ?: "") + ":" + (password ?: "")
        val auth = str.encodeBase64()
        headers["Authorization"] = "Basic $auth"
    }
}
