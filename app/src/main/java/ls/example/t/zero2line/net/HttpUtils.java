package ls.example.t.zero2line.net;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/08
 *     desc  : utils about http
 * </pre>
 */
public final class HttpUtils {

    private static final String BOUNDARY    = java.util.UUID.randomUUID().toString();
    private static final String TWO_HYPHENS = "--";

    private static final int CONNECT_TIMEOUT_TIME = 15000;
    private static final int READ_TIMEOUT_TIME    = 20000;
    private static final int BUFFER_SIZE          = 8192;

    private static final ls.example.t.zero2line.net.HttpUtils.Config CONFIG = new ls.example.t.zero2line.net.HttpUtils.Config();


    private final  ls.example.t.zero2line.net.HttpUtils.Config    mConfig;
    private static ls.example.t.zero2line.net.HttpUtils sHttpUtils;

    private HttpUtils(@android.support.annotation.NonNull ls.example.t.zero2line.net.HttpUtils.Config config) {
        mConfig = config;
    }

    public static ls.example.t.zero2line.net.HttpUtils getInstance(@android.support.annotation.NonNull ls.example.t.zero2line.net.HttpUtils.Config config) {
        if (sHttpUtils == null) {
            synchronized (ls.example.t.zero2line.net.HttpUtils.class) {
                sHttpUtils = new ls.example.t.zero2line.net.HttpUtils(config);
            }
        }
        return sHttpUtils;
    }

    public static void call(@android.support.annotation.NonNull final Request request, @android.support.annotation.NonNull final ResponseCallback callback) {
        new ls.example.t.zero2line.net.HttpUtils.Call(request, callback).run();
    }

    private static java.net.HttpURLConnection getConnection(final Request request) throws java.io.IOException {
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) request.mURL.openConnection();
        if (conn instanceof javax.net.ssl.HttpsURLConnection) {
            javax.net.ssl.HttpsURLConnection httpsConn = (javax.net.ssl.HttpsURLConnection) conn;
            httpsConn.setSSLSocketFactory(CONFIG.sslConfig.mSSLSocketFactory);
            httpsConn.setHostnameVerifier(CONFIG.sslConfig.mHostnameVerifier);
        }
        System.out.println(conn.getHeaderField("USE"));
        addHeader(conn, request.mHeader);
        addBody(conn, request.mBody);
        conn.setConnectTimeout(CONFIG.connectTimeout);
        conn.setReadTimeout(CONFIG.readTimeout);
        return conn;
    }

    private static void addBody(java.net.HttpURLConnection conn, Request.Body body) throws java.io.IOException {
        if (body == null) {
            conn.setRequestMethod("GET");
        } else {
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", body.mediaType);
            if (body.length > 0) {
                conn.setRequestProperty("content-length", String.valueOf(body.length));
            }
            java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(conn.getOutputStream(), 10240);
            if (body.bis != null) {
                byte[] buffer = new byte[10240];
                for (int len; (len = body.bis.read(buffer)) != -1; ) {
                    bos.write(buffer, 0, len);
                }
                bos.close();
                body.bis.close();
            }
        }
    }

    private static void addHeader(final java.net.HttpURLConnection conn, final java.util.Map<String, String> headerMap) {
        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                conn.setRequestProperty(key, headerMap.get(key));
            }
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static String is2String(final java.io.InputStream is, final String charset) {
        java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        try {
            for (int len; (len = is.read(buffer)) != -1; ) {
                result.write(buffer, 0, len);
            }
            return result.toString(charset);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                is.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean writeFileFromIS(final java.io.File file,
                                   final java.io.InputStream is) {
        if (!createOrExistsFile(file) || is == null) return false;
        java.io.OutputStream os = null;
        try {
            os = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file));
            byte data[] = new byte[8192];
            for (int len; (len = is.read(data)) != -1; ) {
                os.write(data, 0, len);
            }
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean createOrExistsFile(final java.io.File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final java.io.File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static class Config {

        private java.util.concurrent.Executor workExecutor = ExecutorFactory.getDefaultWorkExecutor();
        private java.util.concurrent.Executor mainExecutor = ExecutorFactory.getDefaultMainExecutor();

        private SSLConfig sslConfig      = SSLConfig.DEFAULT_SSL_CONFIG;
        private int       connectTimeout = CONNECT_TIMEOUT_TIME;
        private int       readTimeout    = READ_TIMEOUT_TIME;
        private java.nio.charset.Charset   charset        = java.nio.charset.Charset.defaultCharset();

        private java.net.Proxy proxy = null;
    }

    static class Call implements Runnable {

        private Request          request;
        private ResponseCallback callback;

        public Call(Request request, ResponseCallback callback) {
            this.request = request;
            this.callback = callback;
        }

        @Override
        public void run() {
            java.net.HttpURLConnection conn = null;
            try {
                conn = getConnection(request);
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    java.io.InputStream is = conn.getInputStream();
                    callback.onResponse(new Response(conn.getHeaderFields(), is));
                    is.close();
                } else if (responseCode == 301 || responseCode == 302) {
                    String location = conn.getHeaderField("Location");
                    call(request, callback);
                } else {
                    String errorMsg = null;
                    java.io.InputStream es = conn.getErrorStream();
                    if (es != null) {
                        errorMsg = is2String(es, "utf-8");
                    }
                    callback.onFailed(new android.accounts.NetworkErrorException("error code: " + responseCode +
                            (isSpace(errorMsg) ? "" : ("\n" + "error message: " + errorMsg))));
                }
            } catch (java.io.IOException e) {
                callback.onFailed(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
