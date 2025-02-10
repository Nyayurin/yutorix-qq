package cn.yurin.yutorix.module.qq.adapter

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AccessToken(
    var appId: String,
    val accessToken: String,
    val expiresIn: String,
) {
    companion object {
        suspend fun get(appId: String, clientSecret: String): AccessToken {
            val client = HttpClient {
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            }
            val response = client.post("https://bots.qq.com/app/getAppAccessToken") {
                contentType(ContentType.Application.Json)
                setBody("""{"appId":${appId},"clientSecret":${clientSecret}}""")
            }
            val acc = response.body<AccessToken>()
            acc.appId = appId
            return acc
        }
    }

    fun headers() {
        headers {
            append("Authorization", "QQBot $accessToken")
            append("X-Union-Appid", appId)
        }
    }
}

@Serializable
data class Gateway(
    val url: String,
    val botAccessToken: String
) {
    companion object {
        suspend fun get(accessToken: AccessToken): Gateway {
            val client = HttpClient {
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            }
            val response = client.get("https://api.sgroup.qq.com/gateway") {
                headers {
                    append("Authorization", accessToken.accessToken)
                }
            }
            return response.body()
        }
    }
}