package org.cozz.dns;

import org.cozz.tool.Tools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.Logger;

public class DNS {
    private final static Logger logger = Logger.getGlobal();

    public static boolean responseHttpDns(SocketChannel channel, byte[] data) {
        int i = Tools.indexOf(data, "?dn=");
        if (i < 0) {
            return false;
        }

        String domainStr;
        try {
            byte[] domain = Arrays.copyOfRange(data, i + 4, data.length);
            domainStr = new String(domain);
        } catch (RuntimeException re) {
            return false;
        }
        logger.info("http DNS domain: " + domainStr);

        try {
            InetAddress[] ias = InetAddress.getAllByName(domainStr);
            for (InetAddress ia : ias) {
                String hostAddress = ia.getHostAddress();
                if (hostAddress.contains(":")) {
                    String resp = String.format("HTTP/1.0 200 OK\r\nConnection: Close\r\nServer: CuteBi Linux Network httpDNS, (%%>w<%%)\r\nContent-Length: %d\r\n\r\n%s",
                            hostAddress.length(), hostAddress);
                    channel.write(ByteBuffer.wrap(resp.getBytes()));
                }
            }
        } catch (UnknownHostException e) {
            logger.warning("Unknown DNS domain: " + domainStr);
            try {
                channel.write(ByteBuffer.wrap("HTTP/1.0 404 Not Found\r\nConnection: Close\r\nServer: CuteBi Linux Network httpDNS, (%>w<%)\r\nContent-type: charset=utf-8\r\n\r\n<html><head><title>HTTP DNS Server</title></head><body>查询域名失败<br/><br/>By: 萌萌萌得不要不要哒<br/>E-mail: 915445800@qq.com</body></html>".getBytes()));
                logger.warning("http DNS domain: " + domainStr + ", lookup failed.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
