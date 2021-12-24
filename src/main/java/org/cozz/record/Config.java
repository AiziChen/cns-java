package org.cozz.record;

public class Config {
    private String proxyKey;
    private String udpFlag;
    private Long[] listenPorts;
    private String password;
    private boolean enableDnsOverUdp;
    private boolean enableHttpDns;

    public String getProxyKey() {
        return proxyKey;
    }

    public void setProxyKey(String proxyKey) {
        this.proxyKey = proxyKey;
    }

    public String getUdpFlag() {
        return udpFlag;
    }

    public void setUdpFlag(String udpFlag) {
        this.udpFlag = udpFlag;
    }

    public Long[] getListenPorts() {
        return listenPorts;
    }

    public void setListenPorts(Long[] listenPorts) {
        this.listenPorts = listenPorts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnableDnsOverUdp() {
        return enableDnsOverUdp;
    }

    public void setEnableDnsOverUdp(boolean enableDnsOverUdp) {
        this.enableDnsOverUdp = enableDnsOverUdp;
    }

    public boolean isEnableHttpDns() {
        return enableHttpDns;
    }

    public void setEnableHttpDns(boolean enableHttpDns) {
        this.enableHttpDns = enableHttpDns;
    }
}
