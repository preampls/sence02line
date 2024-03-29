package ls.example.t.zero2line.base.rv;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/03/16
 *     desc  :
 * </pre>
 */
public abstract class BaseItem<T extends BaseItem> {

    private static final  SparseIntArray    LAYOUT_SPARSE_ARRAY = new SparseIntArray();
    private static final  SparseArray< View> VIEW_SPARSE_ARRAY   = new  SparseArray<>();

    static ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutByType = LAYOUT_SPARSE_ARRAY.get(viewType, -1);
        if (layoutByType != -1) {
            return new ItemViewHolder( LayoutInflater.from(parent.getContext()).inflate(layoutByType, parent, false));
        }
         View viewByType = VIEW_SPARSE_ARRAY.get(viewType);
        if (viewByType != null) {
            return new ItemViewHolder(viewByType);
        }
        throw new RuntimeException("onCreateViewHolder: get holder from view type failed.");
    }

    public abstract void bind(  final ItemViewHolder holder, final int position);

    public void onViewRecycled(  final ItemViewHolder holder, final int position) {/**/}

    public long getItemId() {
        return RecyclerView.NO_ID;
    }

    private int viewType;
    BaseItemAdapter<T> mAdapter;

    public BaseItem(int layoutId) {
        viewType = getViewTypeByLayoutId(layoutId);
        LAYOUT_SPARSE_ARRAY.put(viewType, layoutId);
    }

    public BaseItem(   View view) {
        viewType = getViewTypeByView(view);
        VIEW_SPARSE_ARRAY.put(viewType, view);
    }

    public int getViewType() {
        return viewType;
    }

    public BaseItemAdapter<T> getAdapter() {
        return mAdapter;
    }

    public boolean isViewType(  int layoutId) {
        return viewType == getViewTypeByLayoutId(layoutId);
    }

    public boolean isViewType(   View view) {
        return viewType == getViewTypeByView(view);
    }

    private int getViewTypeByLayoutId(  int layoutId) {
        return layoutId + getClass().hashCode();
    }

    private int getViewTypeByView(   View view) {
        return view.hashCode() + getClass().hashCode();
    }
}
