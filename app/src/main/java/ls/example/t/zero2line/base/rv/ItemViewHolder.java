package ls.example.t.zero2line.base.rv;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/22
 *     desc  :
 * </pre>
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewArray = new SparseArray<>();

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById( final int viewId) {
        View view = viewArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewArray.put(viewId, view);
        }
        return (T) view;
    }

    public void setOnClickListener(final int viewId,  OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }

    public void setOnLongClickListener( final int viewId,OnLongClickListener listener) {
        findViewById(viewId).setOnLongClickListener(listener);
    }
}
