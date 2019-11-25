package ls.example.t.zero2line.net;

import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/02/17
 * </pre>
 */
public final class Request {

    java.net.URL mURL;
    java.util.Map<String, String> mHeader;
    ls.example.t.zero2line.net.Request.Body mBody;

    public static ls.example.t.zero2line.net.Request withUrl(@android.support.annotation.NonNull final String url) {
        return new ls.example.t.zero2line.net.Request(url);
    }

    private Request(final String url) {
        try {
            mURL = new java.net.URL(url);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Request addHeader(@android.support.annotation.NonNull final String name, @android.support.annotation.NonNull final String value) {
        if (mHeader == null) {
            mHeader = new java.util.HashMap<>();
        }
        mHeader.put(name, value);
        return this;
    }

    public ls.example.t.zero2line.net.Request addHeader(@android.support.annotation.NonNull final java.util.Map<String, String> header) {
        if (this.mHeader == null) {
            this.mHeader = new java.util.HashMap<>();
        }
        this.mHeader.putAll(header);
        return this;
    }

    public ls.example.t.zero2line.net.Request post(@android.support.annotation.NonNull final ls.example.t.zero2line.net.Request.Body body) {
        this.mBody = body;
        return this;
    }

    public static class Body {
        String mediaType;
        java.io.BufferedInputStream bis;
        long length;

        private Body(final String mediaType, final byte[] body) {
            this.mediaType = mediaType;
            bis = new java.io.BufferedInputStream(new java.io.ByteArrayInputStream(body));
            length = body.length;
        }

        private Body(final String mediaType, final java.io.InputStream body) {
            this.mediaType = mediaType;
            if (body instanceof java.io.BufferedInputStream) {
                bis = (java.io.BufferedInputStream) body;
            } else {
                bis = new java.io.BufferedInputStream(body);
            }
            length = -1;
        }

        private static String getCharsetFromMediaType(String mediaType) {
            mediaType = mediaType.toLowerCase().replace(" ", "");
            int index = mediaType.indexOf("charset=");
            if (index == -1) return "utf-8";
            int st = index + 8;
            int end = mediaType.length();
            if (st >= end) {
                throw new IllegalArgumentException("MediaType is not correct: \"" + mediaType + "\"");
            }
            for (int i = st; i < end; i++) {
                char c = mediaType.charAt(i);
                if (c >= 'A' && c <= 'Z') continue;
                if (c >= 'a' && c <= 'z') continue;
                if (c >= '0' && c <= '9') continue;
                if (c == '-' && i != 0) continue;
                if (c == '+' && i != 0) continue;
                if (c == ':' && i != 0) continue;
                if (c == '_' && i != 0) continue;
                if (c == '.' && i != 0) continue;
                end = i;
                break;
            }
            String charset = mediaType.substring(st, end);
            return checkCharset(charset);
        }

        public static ls.example.t.zero2line.net.Request.Body create(@android.support.annotation.NonNull String mediaType, @android.support.annotation.NonNull byte[] content) {
            return new ls.example.t.zero2line.net.Request.Body(mediaType, content);
        }

        public static ls.example.t.zero2line.net.Request.Body form(@android.support.annotation.NonNull final java.util.Map<String, String> form) {
            return form(form, "utf-8");
        }

        public static ls.example.t.zero2line.net.Request.Body form(@android.support.annotation.NonNull final java.util.Map<String, String> form, String charset) {
            String mediaType = "application/x-www-form-urlencoded;charset=" + checkCharset(charset);
            final StringBuilder sb = new StringBuilder();
            for (String key : form.keySet()) {
                if (sb.length() > 0) sb.append("&");
                sb.append(key).append("=").append(form.get(key));
            }
            try {
                return new ls.example.t.zero2line.net.Request.Body(mediaType, sb.toString().getBytes(charset));
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static ls.example.t.zero2line.net.Request.Body json(@android.support.annotation.NonNull final String json) {
            return json(json, "utf-8");
        }

        public static ls.example.t.zero2line.net.Request.Body json(@android.support.annotation.NonNull final String json, String charset) {
            String mediaType = "application/json;charset=" + checkCharset(charset);
            try {
                return new ls.example.t.zero2line.net.Request.Body(mediaType, json.getBytes(charset));
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

//        public static RequestBody file(String mediaType, final File file) {
//
//            return new RequestBody(mediaType, );
//        }

    }

    private static String checkCharset(final String charset) {
        if (java.nio.charset.Charset.isSupported(charset)) return charset;
        throw new java.nio.charset.IllegalCharsetNameException(charset);
    }
}
