@file:Suppress("MemberVisibilityCanBePrivate", "unused", "HttpUrlsUsage")

package cn.yurin.yutorix.module.qq.adapter

import cn.yurin.yutori.Adapter
import cn.yurin.yutori.Reinstallable
import cn.yurin.yutori.Yutori
import cn.yurin.yutori.YutoriBuilder
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun Adapter.Companion.qq(
	alias: String? = null,
	id: Long,
	token: String? = null,
	secret: String
) = QQAdapter(alias, id, token, secret)

class QQAdapter(
	alias: String?,
	val id: Long,
	val token: String?,
	val secret: String,
) : Adapter(alias), Reinstallable {
	override fun install(builder: YutoriBuilder) {}

	override fun uninstall(builder: YutoriBuilder) {}

	override suspend fun start(yutori: Yutori) {
		val accessToken = AccessToken.get(id.toString(), secret)
		val headers = accessToken.headers()
		val gateway = Gateway.get(accessToken)
		do {
			val client = HttpClient {
				install(WebSockets) {
					contentConverter = KotlinxWebsocketSerializationConverter(Json { ignoreUnknownKeys = true })
				}
			}

			try {
				client.webSocket(gateway.url) {
					val ready = false
					Log.i { "成功建立 WebSocket 连接, 尝试建立事件推送服务" }
					launch {
						delay(10000)
						if (!ready) throw RuntimeException("无法建立事件推送服务: READY 等待超时")
						while (isActive) {
							Log.d { "发送 PING" }
							delay(10000)
						}
					}
					while (isActive) {
						val receive = (incoming.receive() as? Frame.Text ?: continue).readText()
						Log.d { "接收信令: $receive" }
					}
				}
			} catch (e:Exception) {
				if (e is CancellationException && e.message == "Event service disconnect") {
					Log.i { "WebSocket 连接断开: 主动断开连接" }
				} else {
					Log.w(e) { "WebSocket 连接断开" }
				}
			}
		} while (true)
	}

	override fun stop(yutori: Yutori) {
		TODO()
	}
}