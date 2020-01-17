package zelosin.pack.connections;

import org.apache.http.HttpHost;

public class ProxyConnection {
    private static HttpHost proxy;
    private static String hostname;
    private static int port;
    static{
        hostname = "88.199.163.1";
        port = 45753;
        proxy = new HttpHost("207.154.231.217", 3128, "http");
    }
    public static HttpHost getProxy() {
        return proxy;
    }

    public static String getHostname() {
        return hostname;
    }
    public static int getPort() {
        return port;
    }
}
