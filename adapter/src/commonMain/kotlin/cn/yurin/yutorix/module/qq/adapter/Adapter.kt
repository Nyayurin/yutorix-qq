@file:Suppress("MemberVisibilityCanBePrivate", "unused", "HttpUrlsUsage")

package cn.yurin.yutorix.module.qq.adapter

import cn.yurin.yutori.Adapter
import cn.yurin.yutori.Reinstallable
import cn.yurin.yutori.Yutori
import cn.yurin.yutori.YutoriBuilder

fun Adapter.Companion.qq(
	alias: String? = null,
	id: Long,
	token: String,
	secret: String
) = QQAdapter(alias, id, token, secret)

class QQAdapter(
	alias: String?,
	val id: Long,
	val token: String,
	val secret: String,
) : Adapter(alias), Reinstallable {
	override fun install(builder: YutoriBuilder) {}

	override fun uninstall(builder: YutoriBuilder) {}

	override suspend fun start(yutori: Yutori) {
		TODO()
	}

	override fun stop(yutori: Yutori) {
		TODO()
	}
}