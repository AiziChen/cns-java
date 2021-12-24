package org.cozz;

import org.cozz.record.Config;
import org.cozz.runnable.ServerRunnable;
import org.quanye.sobj.SObjParser;
import org.quanye.sobj.exception.InvalidSObjSyntaxException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getGlobal();
    public static Config globalConfig;

    public static void main(String[] args) throws IOException, InvalidSObjSyntaxException {
        Main m = new Main();
        m.overrideConfig();
        m.initListener();
    }

    public void overrideConfig() throws IOException, InvalidSObjSyntaxException {
        // load the default config file
        File userConfigFile = new File("config.sobj");
        InputStream stream;
        if (userConfigFile.exists()) {
            stream = new FileInputStream(userConfigFile);
        } else {
            stream = getClass().getClassLoader().getResourceAsStream("default-config.sobj");
        }
        if (stream == null) {
            throw new RuntimeException("File ead failed: `" + userConfigFile.getAbsolutePath() + "`");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            globalConfig = SObjParser.toObject(sb.toString(), Config.class);
        }
    }

    public void initListener() throws IOException {
        for (long port : globalConfig.getListenPorts()) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(true);
            ssc.socket().bind(new InetSocketAddress(Math.toIntExact(port)));
            new Thread(() -> {
                logger.info("Listen to port " + port + " successfully");
                while (true) {
                    SocketChannel sc;
                    try {
                        sc = ssc.accept();
                    } catch (IOException e) {
                        logger.warning("new connection read failed");
                        break;
                    }
                    logger.info("Handle a new connection...");
                    new Thread(new ServerRunnable(sc)).start();
                }
            }).start();
        }
    }
}
