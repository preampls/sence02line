package ls.example.t.zero2line.base.rv;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/08/22
 *     desc  :
 * </pre>
 */

/**
 * onCreateViewHolder(加载ViewHolder的布局)
 *
 * onViewAttachedToWindow（当Item进入这个页面的时候调用）
 *
 * onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦)
 *
 * onViewDetachedFromWindow（当Item离开这个页面的时候调用）
 *
 * onViewRecycled(当Item被回收的时候调用)
 * ————————————————
 * 原文链接：https://blog.csdn.net/u013278099/article/details/49717023
 * @Author ADMIN
 * @time 2019-11-20 16:10
 */

public class BaseItemAdapter<Item extends BaseItem> extends RecyclerView.Adapter<ItemViewHolder> {

    public java.util.List<Item> mItems;

    public BaseItemAdapter() {
        this(false);
    }

    public BaseItemAdapter(boolean hasStableIds) {
        setHasStableIds(hasStableIds);
    }

    @Override
    public final int getItemViewType(int position) {
        Item item = mItems.get(position);
        item.mAdapter = this;
        return item.getViewType();
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getItemId();
    }

     
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Item.onCreateViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder( ItemViewHolder holder, int position) {
        mItems.get(position).bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewRecycled( ItemViewHolder holder) {//onViewRecycled(当Item被回收的时候调用)
        super.onViewRecycled(holder);
        int position = holder.getAdapterPosition();
        if (position < 0 || position >= mItems.size()) {
            return;
        }
        mItems.get(position).onViewRecycled(holder, position);
    }

    public void setItems(  final java.util.List<Item> items) {
        mItems = items;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(mItems);
    }

    public Item getItem( final int position) {
        return mItems.get(position);
    }

    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    ///////////////////////////////////////////////////////////////////////////
    // id
    ///////////////////////////////////////////////////////////////////////////

    public Item getItemById(final long id) {
        int itemIndex = getItemIndexById(id);
        if (itemIndex != -1) {
            return mItems.get(itemIndex);
        } else {
            return null;
        }
    }

    public int getItemIndexById(final long id) {
        for (int i = 0; i < mItems.size(); i++) {
            if (getItemId(i) == id) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasItemWithId(final long id) {
        return getItemIndexById(id) != -1;
    }

    public int replaceItemById(final long id,   final Item item) {
        return replaceItemById(id, item, false);
    }

    public int replaceItemById(final long id,   final Item item, boolean notifyChanged) {
        int itemIndex = getItemIndexById(id);
        if (itemIndex != -1) {
            replaceItem(itemIndex, item, notifyChanged);
        }
        return itemIndex;
    }

    public int removeItemById(final long id) {
        return removeItemById(id, false);
    }

    public int removeItemById(final long id, boolean notifyRemoved) {
        for (int i = 0; i < mItems.size(); i++) {
            if (getItemId(i) == id) {
                removeItem(i, notifyRemoved);
                return i;
            }
        }
        return -1;
    }

    ///////////////////////////////////////////////////////////////////////////
    // operate
    ///////////////////////////////////////////////////////////////////////////

    public void addItem(  final Item item) {
        addItem(item, false);
    }

    public void addItem(  final Item item, boolean notifyInserted) {
        mItems.add(item);
        if (notifyInserted) notifyItemInserted(mItems.size() - 1);
    }

    public void addItem( final int index,   final Item item) {
        addItem(index, item, false);
    }

    public void addItem(final int index,   final Item item, boolean notifyInserted) {
        mItems.add(index, item);
        if (notifyInserted) notifyItemInserted(index);
    }

    public void addItems(  final java.util.List<Item> items) {
        addItems(items, false);
    }

    public void addItems(  final java.util.List<Item> items, boolean notifyInserted) {
        mItems.addAll(items);
        if (notifyInserted) notifyItemRangeInserted(mItems.size() - items.size() - 1, items.size());
    }

    public void addItems( final int index,   final java.util.List<Item> items) {
        addItems(index, items, false);
    }

    public void addItems( final int index,   final java.util.List<Item> items, boolean notifyInserted) {
        mItems.addAll(index, items);
        if (notifyInserted) notifyItemRangeInserted(index, items.size());
    }

    public void swapItem( final int firstIndex, @android.support.annotation.IntRange(from = 0) final int secondIndex) {
        swapItem(firstIndex, secondIndex, false);
    }

    public void swapItem( final int firstIndex,final int secondIndex, boolean notifyMoved) {
        Collections.swap(mItems, firstIndex, secondIndex);
        if (notifyMoved) notifyItemMoved(firstIndex, secondIndex);
    }

    public Item replaceItem( final int index,   final Item item) {
        return replaceItem(index, item, false);
    }

    public Item replaceItem( final int index,   final Item item, boolean notifyChanged) {
        Item prevItem = mItems.set(index, item);
        if (notifyChanged) notifyItemChanged(index);
        return prevItem;
    }

    public boolean replaceItems(  final java.util.List<Item> items) {
        return replaceItems(items, false);
    }

    public boolean replaceItems(  final java.util.List<Item> items, boolean notifyDataSetChanged) {
        mItems.clear();
        boolean added = mItems.addAll(items);
        if (notifyDataSetChanged) notifyDataSetChanged();
        return added;
    }

    public Item removeItem( final int index) {
        return removeItem(index, false);
    }

    public Item removeItem( final int index, boolean notifyRemoved) {
        Item removedItem = mItems.remove(index);
        if (notifyRemoved) notifyItemRemoved(index);
        return removedItem;
    }

    public int removeItem(  final Item object) {
        return removeItem(object, false);
    }

    public int removeItem(  final Item object, boolean notifyRemoved) {
        int itemIndex = mItems.indexOf(object);
        if (itemIndex != -1) {
            mItems.remove(itemIndex);
            if (notifyRemoved) notifyItemRemoved(itemIndex);
        }
        return itemIndex;
    }

    public void clear() {
        clear(false);
    }

    public void clear(boolean notifyDataSetChanged) {
        mItems.clear();
        if (notifyDataSetChanged) notifyDataSetChanged();
    }

    public void sortItems(  final java.util.Comparator<Item> comparator) {
        sortItems(comparator, false);
    }

    public void sortItems(  final Comparator<Item> comparator, boolean notifyDataSetChanged) {
        Collections.sort(mItems, comparator);
        if (notifyDataSetChanged) notifyDataSetChanged();
    }
}
