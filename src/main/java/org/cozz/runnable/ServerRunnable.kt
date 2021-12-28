package org.cozz.runnable

import kotlinx.coroutines.coroutineScope
import org.cozz.Main
import org.cozz.protocals.TCP.handleTcpSession
import org.cozz.protocals.UDP
import org.cozz.tool.Common
import org.cozz.tool.Tools
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class ServerRunnable {
    suspend fun handleConnect(sc: SocketChannel) = coroutineScope {
        val buf = ByteBuffer.allocate(65536)
        try {
            val bytesRead = sc.read(buf).toLong()
            if (bytesRead == -1L) {
                sc.close()
                return@coroutineScope
            }
            buf.flip()
            val data = buf.array()
            if (Common.isHttpHeader(data)) {
                try {
                    sc.write(ByteBuffer.wrap(Common.responseHeader(data).toByteArray()))
                } catch (ioe: IOException) {
                    sc.close()
                    return@coroutineScope
                }
                if (Tools.indexOf(data, Main.globalConfig!!.udpFlag) == -1) {
                    handleTcpSession(sc, data)
                }
            } else {
                UDP.handleUdpSession(sc, data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}