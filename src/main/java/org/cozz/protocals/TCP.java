package org.cozz.protocals;

import org.cozz.Main;
import org.cozz.tool.Cipher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCP {

    private static final Logger logger = Logger.getGlobal();
    private static final Pattern PROXY_PATTERN = Pattern.compile(Main.globalConfig.getProxyKey() + ":\\s*(.*)\r");

    private static String getProxyHost(byte[] data) {
        Matcher m = PROXY_PATTERN.matcher(new String(data));
        if (m.find()) {
            return Main.globalConfig.getPassword().isEmpty() ? m.group(1) : Cipher.decryptHost(m.group(1));
        } else {
            return "";
        }
    }

    public static void handleTcpSession(SocketChannel channel, byte[] data) {
        String host = getProxyHost(data);
        if (host.isEmpty()) {
            logger.warning("No proxy host: " + new String(data));
            try {
                channel.write(ByteBuffer.wrap("No proxy host".getBytes()));
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        logger.info("proxyHost: " + host);

        // tcp DNS over upd DNS
//        if (Config.isEnableDnsOverUdp() && host.endsWith(":53")) {
//            dnsTcpOverUdp(channel, host, data);
//            return;
//        }

        // tcp forward
        try {
            SocketChannel desChannel = SocketChannel.open();
            if (!host.contains(":")) {
                desChannel.connect(new InetSocketAddress(host, 80));
            } else {
                String[] hostPort = host.split(":");
                desChannel.connect(new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1])));
            }
            logger.info("Starting tcp forward");
            // starting forward
            new Thread(() -> tcpForward(desChannel, channel)).start();
            tcpForward(channel, desChannel);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                channel.close();
            } catch (IOException ignore) {
            }
        }
    }

    private static void tcpForward(SocketChannel cChannel, SocketChannel dChannel) {
        ByteBuffer buf = ByteBuffer.allocate(65536);
        int subI = 0;
        while (true) {
            buf.clear();
            try {
                int bytesRead = cChannel.read(buf);
                if (bytesRead == -1) {
                    break;
                }
                subI = Cipher.xorCrypt(buf, subI);
                buf.flip();
                dChannel.write(buf);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    cChannel.close();
                    dChannel.close();
                } catch (IOException ignore) {
                }
                return;
            }
        }
    }
}
