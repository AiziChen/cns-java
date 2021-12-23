package org.cozz.protocals;

import org.cozz.record.Config;
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
    private static final Pattern PROXY_PATTERN = Pattern.compile(Config.proxyKey + ":\\s*(.*)\r");

    private static String getProxyHost(byte[] data) {
        Matcher m = PROXY_PATTERN.matcher(new String(data));
        if (m.find()) {
            return Config.password.isEmpty() ? m.group(1) : Cipher.decryptHost(m.group(1));
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
            final SocketChannel sChannel = desChannel;
            // starting forward
            new Thread(() -> {
                try {
                    tcpForward(channel, sChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            tcpForward(sChannel, channel);
        } catch (IOException ignore) {
        }
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tcpForward(SocketChannel cChannel, SocketChannel dChannel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(65536);
        while (true) {
            buf.clear();
            int flag = cChannel.read(buf);
            if (flag == -1) {
                break;
            }
            buf.flip();
            dChannel.write(buf);
        }
        cChannel.close();
        dChannel.close();
    }
}
