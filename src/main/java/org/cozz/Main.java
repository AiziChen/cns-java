package org.cozz;

import org.cozz.record.Config;
import org.cozz.runnable.ServerRunnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.overrideConfig();
        m.initListener();
    }

    public void overrideConfig() {
        // TODO: do override
    }

    public void initListener() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(true);
        ssc.socket().bind(new InetSocketAddress(Config.getListenPort()));
        while (true) {
            SocketChannel sc = ssc.accept();
            logger.info("Handle a new connection...");
            new Thread(new ServerRunnable(sc)).start();
        }
    }
}
