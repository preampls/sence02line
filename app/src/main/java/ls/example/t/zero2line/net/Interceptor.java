package ls.example.t.zero2line.net;

import java.io.IOException;

public interface Interceptor {

    Response intercept(Chain chain) throws IOException;
}
