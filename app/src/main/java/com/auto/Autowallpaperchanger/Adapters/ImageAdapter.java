package com.auto.Autowallpaperchanger.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.auto.Autowallpaperchanger.Fragments.Available_Fragment;
import com.auto.Autowallpaperchanger.R;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter implements FasterLoadImage {
    private static String TAG = "ImageAdapter";
    /* access modifiers changed from: private */
    public int count;
    private ArrayList<ViewHolder> listView = new ArrayList<>();
    /* access modifiers changed from: private */
    public Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    SparseBooleanArray mSparseBooleanArray;
    private int[] mThumbIds;
    SharedPreferences preferences;

    public static class ViewHolder {
        CheckBox checkBox;
        ImageView image;
    }

    static /* synthetic */ int access$008(ImageAdapter x0) {
        int i = x0.count;
        x0.count = i + 1;
        return i;
    }

    static /* synthetic */ int access$010(ImageAdapter x0) {
        int i = x0.count;
        x0.count = i - 1;
        return i;
    }

    public ImageAdapter(Context context, int[] thumb, SparseBooleanArray array) {
        this.mContext = context;
        this.mThumbIds = thumb;
        if (array == null) {
            this.mSparseBooleanArray = new SparseBooleanArray();
        } else {
            this.mSparseBooleanArray = array;
            this.count = getCountBoolean();
            Log.d("ImageAdater", "count" + this.count);
        }
        this.mMemoryCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 8) {
            /* access modifiers changed from: protected */
            public int sizeOf(String key, Bitmap bitmap) {
                if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= 12) {
                    return bitmap.getByteCount();
                }
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    public int getCount() {
        return this.mThumbIds.length;
    }

    public Object getItem(int pos) {
        return null;
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        final ViewHolder gridView;
        @SuppressLint("WrongConstant") LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (convertView == null) {
            gridView = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_gridview, (ViewGroup) null);
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
        Animation animation = AnimationUtils.loadAnimation(this.mContext, R.anim.scale);
        loadBitmap(this.mThumbIds[pos], gridView.image);
        gridView.image.startAnimation(animation);
        gridView.checkBox.setTag(Integer.valueOf(pos));
        gridView.checkBox.setChecked(this.mSparseBooleanArray.get(pos));
        gridView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageAdapter.this.mSparseBooleanArray.put(((Integer) buttonView.getTag()).intValue(), isChecked);
                if (isChecked) {
                    ImageAdapter.access$008(ImageAdapter.this);
                } else {
                    ImageAdapter.access$010(ImageAdapter.this);
                }
                if (ImageAdapter.this.count > 0) {
                    Available_Fragment.btSetWallpaper.setEnabled(true);
                    Available_Fragment.btSetWallpaper.setVisibility(View.VISIBLE);
                } else {
                    Available_Fragment.btSetWallpaper.setEnabled(false);
                    Available_Fragment.btSetWallpaper.setVisibility(View.GONE);
                }
                if (ImageAdapter.this.count > 1) {
                    Available_Fragment.spinner_time.setEnabled(true);
                    Available_Fragment.spinner_available.setVisibility(View.VISIBLE);
                } else {
                    Available_Fragment.spinner_time.setEnabled(false);
                    Available_Fragment.spinner_available.setVisibility(View.GONE);
                }
            }
        });
        this.listView.add(gridView);
        return convertView;
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorderTask> bitmapWorderTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorderTask bitmapWorderTask) {
            super(res, bitmap);
            this.bitmapWorderTaskReference = new WeakReference<>(bitmapWorderTask);
        }

        public BitmapWorderTask getBitmapWorderTask() {
            return (BitmapWorderTask) this.bitmapWorderTaskReference.get();
        }
    }

    class BitmapWorderTask extends AsyncTask<Integer, Void, Bitmap> {
        public int data = 0;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorderTask(ImageView imageView) {
            this.imageViewReference = new WeakReference<>(imageView);
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Integer... params) {
            this.data = params[0].intValue();
            Bitmap bitmap = ImageAdapter.this.decodeSampleedBitmapFromResource(ImageAdapter.this.mContext.getResources(), this.data);
            ImageAdapter.this.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            ImageView imageView;
            if (this.imageViewReference != null && bitmap != null && (imageView = (ImageView) this.imageViewReference.get()) != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public ArrayList<Integer> getItemSelectedList() {
        ArrayList<Integer> mItemSelected = new ArrayList<>();
        for (int i = 0; i < this.mThumbIds.length; i++) {
            if (this.mSparseBooleanArray.get(i)) {
                mItemSelected.add(Integer.valueOf(this.mThumbIds[i]));
            }
        }
        return mItemSelected;
    }

    public int getCounSelected() {
        return this.count;
    }

    private int getCountBoolean() {
        int count2 = 0;
        for (int i = 0; i < getCount(); i++) {
            if (this.mSparseBooleanArray.get(i)) {
                count2++;
                Log.e("ImageAdater", "" + count2);
            }
        }
        return count2;
    }

    public SparseBooleanArray getBooleanArraySelected() {
        return this.mSparseBooleanArray;
    }

    public Bitmap decodeSampleedBitmapFromResource(Resources res, int resId) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        while (options.inSampleSize <= 16) {
            try {
                bitmap = BitmapFactory.decodeResource(res, resId, options);
                break;
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "Out of memory!");
                options.inSampleSize++;
            }
        }
        return bitmap;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        Log.e("outH/W ", height + "/" + width);
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    public void loadBitmap(int resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            new BitmapWorderTask(imageView).execute(new Integer[]{Integer.valueOf(resId)});
        }
    }

    public Bitmap getBipmapFromMemoryCache(String key) {
        return this.mMemoryCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBipmapFromMemoryCache(key) == null) {
            this.mMemoryCache.put(key, bitmap);
        }
    }

    public BitmapWorderTask getBitmapWorderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorderTask();
            }
        }
        return null;
    }

    public boolean cancelPotentialWork(int data, ImageView imageView) {
        BitmapWorderTask bitmapWorderTask = getBitmapWorderTask(imageView);
        if (bitmapWorderTask == null) {
            return true;
        }
        if (bitmapWorderTask.data == data) {
            return false;
        }
        bitmapWorderTask.cancel(true);
        return true;
    }

    public void selectAll(boolean checked) {
        for (int i = 0; i < this.listView.size(); i++) {
            this.mSparseBooleanArray.put(i, checked);
            this.listView.get(i).checkBox.setChecked(checked);
        }
    }
}
