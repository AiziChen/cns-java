package org.cozz.runnable;

import org.cozz.Main;
import org.cozz.protocals.TCP;
import org.cozz.protocals.UDP;
import org.cozz.tool.Common;
import org.cozz.tool.Tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerRunnable implements Runnable {
    private final SocketChannel sc;

    public ServerRunnable(SocketChannel sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        handleConnect(sc);
    }

    public void handleConnect(SocketChannel sc) {
        ByteBuffer buf = ByteBuffer.allocate(65536);
        try {
            long bytesRead = sc.read(buf);
            if (bytesRead == -1) {
                sc.close();
                return;
            }
            buf.flip();
            byte[] data = buf.array();
            if (Common.isHttpHeader(data)) {
                try {
                    sc.write(ByteBuffer.wrap(Common.responseHeader(data).getBytes()));
                } catch (IOException ioe) {
                    sc.close();
                    return;
                }
                if (Tools.indexOf(data, Main.globalConfig.getUdpFlag()) == -1) {
                    TCP.handleTcpSession(sc, data);
                }
            } else {
                UDP.handleUdpSession(sc, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
