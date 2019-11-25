package ls.example.t.zero2line.bean;

import com.chad.library.adapter.base.entity.*;
import android.provider.MediaStore.Video;

/**
 *
 * @Author ADMIN
 * @time 2019-11-21 14:38
 */

public class MainActivityGridBean extends SectionEntity<Video> implements  MultiItemEntity  {

    public static final int TEXT = 1;
    public static final int IMG = 2;

    public MainActivityGridBean(boolean isHeader, String header) {
        super(isHeader, header);
    }
    public MainActivityGridBean(Video t) {
        super(t);
    }

    private String name;

    private  long drawable_id;

    private int itemType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(long drawable_id) {
        this.drawable_id = drawable_id;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
