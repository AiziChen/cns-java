package org.cozz.record;

public class Config {
    public static String proxyKey = "Meng";
    public static String udpFlag = "httpUDP";
    public static int listenPort = 1080;
    public static String password = "quanyec";
    public static int udpTimeout = 60;
    public static int tcpKeepAlive = 60;
    public static boolean enableDnsOverUdp = true;
    public static boolean enableHttpDns = true;
    public static boolean enableTFO = true;

    public static String getProxyKey() {
        return proxyKey;
    }

    public static void setProxyKey(String proxyKey) {
        Config.proxyKey = proxyKey;
    }

    public static String getUdpFlag() {
        return udpFlag;
    }

    public static void setUdpFlag(String udpFlag) {
        Config.udpFlag = udpFlag;
    }

    public static int getListenPort() {
        return listenPort;
    }

    public static void setListenPort(int listenPort) {
        Config.listenPort = listenPort;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Config.password = password;
    }

    public static int getUdpTimeout() {
        return udpTimeout;
    }

    public static void setUdpTimeout(int udpTimeout) {
        Config.udpTimeout = udpTimeout;
    }

    public static int getTcpKeepAlive() {
        return tcpKeepAlive;
    }

    public static void setTcpKeepAlive(int tcpKeepAlive) {
        Config.tcpKeepAlive = tcpKeepAlive;
    }

    public static boolean isEnableDnsOverUdp() {
        return enableDnsOverUdp;
    }

    public static void setEnableDnsOverUdp(boolean enableDnsOverUdp) {
        Config.enableDnsOverUdp = enableDnsOverUdp;
    }

    public static boolean isEnableHttpDns() {
        return enableHttpDns;
    }

    public static void setEnableHttpDns(boolean enableHttpDns) {
        Config.enableHttpDns = enableHttpDns;
    }

    public static boolean isEnableTFO() {
        return enableTFO;
    }

    public static void setEnableTFO(boolean enableTFO) {
        Config.enableTFO = enableTFO;
    }
}
