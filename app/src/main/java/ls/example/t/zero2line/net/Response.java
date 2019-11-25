package ls.example.t.zero2line.net;



/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/17
 * </pre>
 */
public class Response {
    private java.util.Map<String, java.util.List<String>> mHeaders;
    private java.io.InputStream               mBody;

    public Response(java.util.Map<String, java.util.List<String>> headers, java.io.InputStream body) {
        mHeaders = headers;
        mBody = body;
    }

    public java.util.Map<String, java.util.List<String>> getHeaders() {
        return mHeaders;
    }

    public java.io.InputStream getBody() {
        return mBody;
    }

    public String getString() {
        return getString("utf-8");
    }

    public String getString(final String charset) {
        return HttpUtils.is2String(mBody, charset);
    }

    public <T> T getJson(final java.lang.reflect.Type type) {
        return getJson(type, "utf-8");
    }

    public <T> T getJson(final java.lang.reflect.Type type, final String charset) {
        return new com.google.gson.Gson().fromJson(getString(charset), type);
    }

    public boolean downloadFile(final java.io.File file) {
        return HttpUtils.writeFileFromIS(file, mBody);
    }
}
