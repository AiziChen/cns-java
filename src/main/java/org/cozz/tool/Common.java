package org.cozz.tool;

public class Common {
    private static final String[] HEADERS = {
            "CONNECT", "GET", "POST", "PUT", "OPTIONS", "HEAD",
            "DELETE", "COPY", "MOVE", "LINK", "UNLINK", "TRACE", "PATCH", "WRAPPED"
    };

    public static boolean isHttpHeader(byte[] data) {
        for (String header : HEADERS) {
            if (Tools.startsWith(data, header)) {
                return true;
            }
        }
        return false;
    }

    public static String responseHeader(byte[] header) {
        if (Tools.indexOf(header, "WebSocket") != -1) {
            return "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: CuteBi Network Tunnel, (%>w<%)\r\n\r\n";
        } else if (Tools.startsWith(header, "CON")) {
            return "HTTP/1.1 200 Connection established\r\nServer: CuteBi Network Tunnel, (%>w<%)\r\nConnection: keep-alive\r\n\r\n";
        } else {
            return "HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\nServer: CuteBi Network Tunnel, (%>w<%)\r\nConnection: keep-alive\r\n\r\n";
        }
    }
}
