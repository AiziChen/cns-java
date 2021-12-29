package org.cozz

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.cozz.record.Config
import org.cozz.runnable.ServerRunnable
import org.quanye.sobj.SObjParser
import org.quanye.sobj.exception.InvalidSObjSyntaxException
import java.io.*
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.util.logging.Logger

class Main {
    @Throws(IOException::class, InvalidSObjSyntaxException::class)
    fun overrideConfig() {
        // load the default config file
        val userConfigFile = File("config.sobj")
        val stream: InputStream? = if (userConfigFile.exists()) {
            FileInputStream(userConfigFile)
        } else {
            javaClass.classLoader.getResourceAsStream("default-config.sobj")
        }
        if (stream == null) {
            throw RuntimeException("File ead failed: `" + userConfigFile.absolutePath + "`")
        } else {
            val br = BufferedReader(InputStreamReader(stream))
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            globalConfig = SObjParser.toObject(sb.toString(), Config::class.java)
        }
    }

    @Throws(IOException::class)
    suspend fun initListener() = coroutineScope {
        for (port in globalConfig!!.listenPorts) {
            val ssc = ServerSocketChannel.open()
            ssc.configureBlocking(true)
            ssc.socket().bind(InetSocketAddress(Math.toIntExact(port)))
            logger.info("Listen to port $port successfully")
            while (true) {
                try {
                    val sc = ssc.accept()
                    logger.info("Handle a new connection...")
                    launch { ServerRunnable().handleConnect(sc) }
                } catch (e: IOException) {
                    logger.warning("new connection read failed")
                    continue
                }
            }
        }
    }

    companion object {
        private val logger = Logger.getGlobal()

        @JvmField
        var globalConfig: Config? = null
    }
}

@Throws(IOException::class, InvalidSObjSyntaxException::class)
suspend fun main(args: Array<String>) {
    val m = Main()
    m.overrideConfig()
    m.initListener()
}