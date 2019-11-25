package ls.example.t.zero2line.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/11/16
 *     desc  :
 * </pre>
 */
public interface IBaseView {

    void initData(@android.support.annotation.Nullable android.os.Bundle bundle);

    int bindLayout();

    void setRootLayout(@android.support.annotation.LayoutRes int layoutId);

    void initView(@android.support.annotation.Nullable android.os.Bundle savedInstanceState, @android.support.annotation.Nullable android.view.View contentView);

    void doBusiness();

    void onDebouncingClick(@android.support.annotation.NonNull android.view.View view);
}
