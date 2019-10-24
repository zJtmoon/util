package com.creditease.honeybot.utils;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.config.*;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 提供池化的http链接生产
 */
public class HttpPoolUtil {
	private static PoolingHttpClientConnectionManager connManager = null;
	private static final int default_so_timeout = 5000;
	static {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接  
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;  
                }  
            };  
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            /*
            SSLContext sslContext = SSLContexts.custom().useTLS().build();
            sslContext.init(null,
                    new TrustManager[] { new X509TrustManager() {
                         
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
 
                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {
                        }
 
                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {
                        }
                    }}, null);
            
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();
            */
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslSF)
                    .build();
            
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoTimeout(default_so_timeout).build();
            connManager.setDefaultSocketConfig(socketConfig);
            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(1000);
            connManager.setDefaultMaxPerRoute(100);
            
            new IdleConnectionMonitorThread(connManager).start();
        } catch (KeyStoreException e){
        } catch (KeyManagementException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
	
	public static CloseableHttpClient createHttpClient(){
	    return HttpClients.custom().disableAutomaticRetries().setRedirectStrategy(new LaxRedirectStrategy()).setConnectionManager(connManager).build();
	}
	public static CloseableHttpClient createHttpClient(String host, int port){
        return HttpClients.custom().disableAutomaticRetries().setRedirectStrategy(new LaxRedirectStrategy()).setConnectionManager(connManager).setProxy(new HttpHost(host,port)).build();
    }
    public static CloseableHttpClient createHttpClientWithCookieStore(BasicCookieStore cookieStore){
        CloseableHttpClient client = HttpClients.custom().disableAutomaticRetries().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultCookieStore(cookieStore).setConnectionManager(connManager).build();
        return client;
    }
    public static CloseableHttpClient createHttpClientWithCookieStore(String host, int port, BasicCookieStore cookieStore){
        CloseableHttpClient client = HttpClients.custom().disableAutomaticRetries().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultCookieStore(cookieStore).setConnectionManager(connManager).setProxy(new HttpHost(host,port)).build();
        return client;
    }

	private static class IdleConnectionMonitorThread extends Thread {
	    private final HttpClientConnectionManager connMgr;
	    private volatile boolean shutdown;
	    
	    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr){
	        super();
	        this.connMgr = connMgr;
	    }
	    
	    @Override
	    public void run(){
	        try {
                while(!shutdown){
                    synchronized (this) {
                        wait(5000);
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            }
            catch (InterruptedException e) {
                // TODO: handle exception
            }
	    }
	    
	    public void shutdown(){
	        shutdown = true;
	        synchronized (this) {
                notifyAll();
            }
	    }
	}
}
