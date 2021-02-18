package com.auto.Autowallpaperchanger.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.auto.Autowallpaperchanger.Fragments.Your_Album_Fragment;
import com.auto.Autowallpaperchanger.MainActivity;
import com.auto.Autowallpaperchanger.MultiSelectImagesActivity;
import com.auto.Autowallpaperchanger.R;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class ImageAdapterMutilSelect extends BaseAdapter {

    private LinearLayout linearlayout_of_time_spinner;
    private Button c_bt_set;

    /* access modifiers changed from: private */
    public int activity;
    /* access modifiers changed from: private */
    public int count;
    /* access modifiers changed from: private */
    public int count1;
    private int count2;
    private ArrayList<ViewHolder> listView;
    private CompoundButton.OnCheckedChangeListener mChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ImageAdapterMutilSelect.this.mSparseBooleanArray.put(((Integer) buttonView.getTag()).intValue(), isChecked);
            if (ImageAdapterMutilSelect.this.activity == 3) {
                if (isChecked) {
                    ImageAdapterMutilSelect.access$308(ImageAdapterMutilSelect.this);
                } else {
                    ImageAdapterMutilSelect.access$310(ImageAdapterMutilSelect.this);
                }
                if (ImageAdapterMutilSelect.this.count1 > 0) {
                }
            } else if (ImageAdapterMutilSelect.this.activity == 1) {
                if (isChecked) {
                    ImageAdapterMutilSelect.access$408(ImageAdapterMutilSelect.this);
                } else {
                    ImageAdapterMutilSelect.access$410(ImageAdapterMutilSelect.this);
                }
                if (ImageAdapterMutilSelect.this.count > 0) {
                    Your_Album_Fragment.btSetWallpaper.setEnabled(true);
                    Your_Album_Fragment.btSetWallpaper.setVisibility(View.VISIBLE);

                } else {
                    Your_Album_Fragment.btSetWallpaper.setEnabled(false);
                    Your_Album_Fragment.btSetWallpaper.setVisibility(View.GONE);
                }
                if (ImageAdapterMutilSelect.this.count > 1) {
                    Your_Album_Fragment.cSpinnerTime.setEnabled(true);
                    Your_Album_Fragment.spinner_your_album.setVisibility(View.VISIBLE);

                } else {
                    Your_Album_Fragment.cSpinnerTime.setEnabled(false);
                    Your_Album_Fragment.spinner_your_album.setVisibility(View.GONE);

                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mList;
    /* access modifiers changed from: private */
    public SparseBooleanArray mSparseBooleanArray;

    public static class ViewHolder {
        CheckBox checkBox;
        ImageView image;
    }

    static /* synthetic */ int access$308(ImageAdapterMutilSelect x0) {
        int i = x0.count1;
        x0.count1 = i + 1;
        return i;
    }

    static /* synthetic */ int access$310(ImageAdapterMutilSelect x0) {
        int i = x0.count1;
        x0.count1 = i - 1;
        return i;
    }

    static /* synthetic */ int access$408(ImageAdapterMutilSelect x0) {
        int i = x0.count;
        x0.count = i + 1;
        return i;
    }

    static /* synthetic */ int access$410(ImageAdapterMutilSelect x0) {
        int i = x0.count;
        x0.count = i - 1;
        return i;
    }

    public ImageAdapterMutilSelect(Context context, ArrayList<String> imgList, SparseBooleanArray array, int act) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mList = imgList;
        this.activity = act;
        if (array == null) {
            this.mSparseBooleanArray = new SparseBooleanArray();
        } else {
            this.mSparseBooleanArray = array;
            this.count = getCountBoolean();
            this.count2 = getCountBoolean();
        }
        this.listView = new ArrayList<>();
    }

    public ArrayList<String> getCheckedItem() {
        ArrayList<String> mTemArr = new ArrayList<>();
        for (int i = 0; i < this.mList.size(); i++) {
            if (this.mSparseBooleanArray.get(i)) {
                mTemArr.add(this.mList.get(i));
            }
        }
        if (mTemArr.size() < 1) {
            return null;
        }
        return mTemArr;
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int pos) {
        return null;
    }

    public long getItemId(int pos) {
        return (long) pos;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        final ViewHolder gridView;
        if (convertView == null) {
            gridView = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.item_gridview, (ViewGroup) null);
            gridView.image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            gridView.checkBox = (CheckBox) convertView.findViewById(R.id.check_item);





            gridView.image.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (gridView.checkBox.isChecked()) {
                        gridView.checkBox.setChecked(false);




                    } else {
                        gridView.checkBox.setChecked(true);




                    }
                }
            });
            convertView.setTag(gridView);
        } else {
            gridView = (ViewHolder) convertView.getTag();
        }
        switch (this.activity) {
            case 1:
                MainActivity.imageLoader.displayImage("file://" + this.mList.get(pos), gridView.image, MainActivity.options, new SimpleImageLoadingListener() {
                    public void onLoadingComplete(Bitmap loadedImage) {
                      //  super.onLoadingComplete(loadedImage);
                        gridView.image.startAnimation(AnimationUtils.loadAnimation(ImageAdapterMutilSelect.this.mContext, R.anim.scale));
                    }
                });
                break;
            case 3:
                MultiSelectImagesActivity.imageLoader.displayImage("file://" + this.mList.get(pos), gridView.image, MultiSelectImagesActivity.options, new SimpleImageLoadingListener() {
                    public void onLoadingComplete(Bitmap loadedImage) {
                    //    super.onLoadingComplete(loadedImage);
                        gridView.image.startAnimation(AnimationUtils.loadAnimation(ImageAdapterMutilSelect.this.mContext, R.anim.scale));
                    }
                });
                break;
        }
        gridView.checkBox.setTag(Integer.valueOf(pos));
        gridView.checkBox.setChecked(this.mSparseBooleanArray.get(pos));
        gridView.checkBox.setOnCheckedChangeListener(this.mChangeListener);
        this.listView.add(gridView);
        return convertView;
    }

    public int getCounSelected() {
        if (this.activity == 1) {
            return this.count;
        }
        if (this.activity == 2) {
            return this.count2;
        }
        return this.count1;
    }

    private int getCountBoolean() {
        int count3 = 0;
        for (int i = 0; i < getCount(); i++) {
            if (this.mSparseBooleanArray.get(i)) {
                count3++;
            }
        }
        return count3;
    }

    public SparseBooleanArray getBooleanArraySelected() {
        return this.mSparseBooleanArray;
    }

    public void selectAll(boolean checked) {
        for (int i = 0; i < this.listView.size(); i++) {
            this.mSparseBooleanArray.put(i, checked);
            this.listView.get(i).checkBox.setChecked(checked);
        }
    }
}
