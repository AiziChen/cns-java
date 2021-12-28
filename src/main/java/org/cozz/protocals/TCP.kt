package org.cozz.protocals

import org.cozz.Main
import org.cozz.tool.Cipher
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern

object TCP {
    private val logger = Logger.getGlobal()
    private val PROXY_PATTERN = Pattern.compile(Main.globalConfig?.proxyKey + ":\\s*(.*)\r")
    private fun getProxyHost(data: ByteArray): String {
        val m = PROXY_PATTERN.matcher(String(data))
        return if (m.find()) {
            if (Main.globalConfig?.password?.isEmpty() == true) m.group(1) else Cipher.decryptHost(m.group(1))
        } else {
            ""
        }
    }

    @JvmStatic
    fun handleTcpSession(channel: SocketChannel, data: ByteArray) {
        val host = getProxyHost(data)
        if (host.isEmpty()) {
            logger.warning("No proxy host: " + String(data))
            try {
                channel.write(ByteBuffer.wrap("No proxy host".toByteArray()))
                channel.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return
        }
        logger.info("proxyHost: $host")

        // tcp DNS over upd DNS
//        if (Config.isEnableDnsOverUdp() && host.endsWith(":53")) {
//            dnsTcpOverUdp(channel, host, data);
//            return;
//        }

        // tcp forward
        try {
            SocketChannel.open().use { desChannel ->
                if (!host.contains(":")) {
                    desChannel.connect(InetSocketAddress(host, 80))
                } else {
                    val hostPort = host.split(":").toTypedArray()
                    desChannel.connect(InetSocketAddress(hostPort[0], hostPort[1].toInt()))
                }
                logger.info("Starting tcp forward")
                // starting forward
                Thread { tcpForward(desChannel, channel) }.start()
                tcpForward(channel, desChannel)
                try {
                    channel.close()
                    desChannel.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                logger.log(Level.OFF, "tcp-forward had been done.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                channel.close()
            } catch (ignore: IOException) {
            }
        }
    }

    private fun tcpForward(cChannel: SocketChannel, dChannel: SocketChannel) {
        val buf = ByteBuffer.allocate(65536)
        var subI = 0
        while (true) {
            buf.clear()
            try {
                val bytesRead = cChannel.read(buf)
                if (bytesRead == -1) {
                    break
                }
                subI = Cipher.xorCrypt(buf, subI)
                buf.flip()
                dChannel.write(buf)
            } catch (e: IOException) {
                break
            }
        }
    }
}