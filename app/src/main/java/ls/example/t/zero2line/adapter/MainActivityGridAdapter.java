package ls.example.t.zero2line.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ls.t.zero2line.R;
import ls.example.t.zero2line.bean.MainActivityGridBean;

public class MainActivityGridAdapter extends BaseQuickAdapter<MainActivityGridBean, BaseViewHolder> {

    public MainActivityGridAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MainActivityGridBean item) {
        helper.setText(ls.t.zero2line.R.id.adapter_main_grid_item_name, item.getName())
                .addOnClickListener(ls.t.zero2line.R.id.adapter_main_grid_item_name)
                .addOnClickListener(ls.t.zero2line.R.id.adapter_main_grid_item_src);
    }
}
