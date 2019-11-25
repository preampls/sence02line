package ls.example.t.zero2line.net;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/20
 * </pre>
 */
public final class SSLConfig {

    SSLSocketFactory mSSLSocketFactory;
    HostnameVerifier mHostnameVerifier;

    public SSLConfig(SSLSocketFactory factory, HostnameVerifier verifier) {
        mSSLSocketFactory = factory;
        mHostnameVerifier = verifier;
    }

    public static final javax.net.ssl.HostnameVerifier DEFAULT_VERIFIER           = new javax.net.ssl.HostnameVerifier() {
        public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
            return true;
        }
    };
    public static final javax.net.ssl.SSLSocketFactory DEFAULT_SSL_SOCKET_FACTORY = new ls.example.t.zero2line.net.SSLConfig.DefaultSSLSocketFactory();

    public static final ls.example.t.zero2line.net.SSLConfig DEFAULT_SSL_CONFIG = new ls.example.t.zero2line.net.SSLConfig(DEFAULT_SSL_SOCKET_FACTORY, DEFAULT_VERIFIER);

    private static class DefaultSSLSocketFactory extends javax.net.ssl.SSLSocketFactory {

        private static final String[]       PROTOCOL_ARRAY;
        private static final javax.net.ssl.TrustManager[] DEFAULT_TRUST_MANAGERS;

        static {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                PROTOCOL_ARRAY = new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"};
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                PROTOCOL_ARRAY = new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
            } else {
                PROTOCOL_ARRAY = new String[]{"SSLv3", "TLSv1"};
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DEFAULT_TRUST_MANAGERS = new javax.net.ssl.TrustManager[]{
                        new javax.net.ssl.X509ExtendedTrustManager() {
                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {/**/}

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {/**/}

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[0];
                            }

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType, java.net.Socket socket) {/**/}

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType, java.net.Socket socket) {/**/}

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType, javax.net.ssl.SSLEngine engine) {/**/}

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType, javax.net.ssl.SSLEngine engine) {/**/}
                        }
                };
            } else {
                DEFAULT_TRUST_MANAGERS = new javax.net.ssl.TrustManager[]{
                        new javax.net.ssl.X509TrustManager() {
                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {/**/}

                            @android.annotation.SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {/**/}

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[0];
                            }
                        }
                };
            }

        }

        private javax.net.ssl.SSLSocketFactory mFactory;

        DefaultSSLSocketFactory() {
            try {
                javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
                sslContext.init(null, DEFAULT_TRUST_MANAGERS, new java.security.SecureRandom());
                mFactory = sslContext.getSocketFactory();
            } catch (java.security.GeneralSecurityException e) {
                throw new AssertionError();
            }
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return mFactory.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return mFactory.getSupportedCipherSuites();
        }

        @Override
        public java.net.Socket createSocket(java.net.Socket s, String host, int port, boolean autoClose) throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket(s, host, port, autoClose);
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        @Override
        public java.net.Socket createSocket(String host, int port) throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket(host, port);
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        @Override
        public java.net.Socket createSocket(String host, int port, java.net.InetAddress localHost, int localPort) throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket(host, port, localHost, localPort);
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        @Override
        public java.net.Socket createSocket(java.net.InetAddress host, int port) throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket(host, port);
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        @Override
        public java.net.Socket createSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddress, int localPort) throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket(address, port, localAddress, localPort);
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        @Override
        public java.net.Socket createSocket() throws java.io.IOException {
            java.net.Socket ssl = mFactory.createSocket();
            setSupportProtocolAndCipherSuites(ssl);
            return ssl;
        }

        private void setSupportProtocolAndCipherSuites(java.net.Socket socket) {
            if (socket instanceof javax.net.ssl.SSLSocket) {
                ((javax.net.ssl.SSLSocket) socket).setEnabledProtocols(PROTOCOL_ARRAY);
            }
        }
    }
}
