package us.bestapp.henrytaro.utils;/**
 * Created by xuhaolin on 15/9/8.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by xuhaolin on 15/9/8.
 */
public class SeatResourceUtils {
    public static final String SINGLE_INSTANCE = "single_instance";
    private static HashMap<String, SeatResourceUtils> mInstanceMap = null;

    private int[] mDrawColorArr = null;
    private int[] mThumbnailColorArr = null;
    private Bitmap[] mBitmapArr = null;
    private int[] mResIDArr = null;
    private String[] mDescArr = null;

    private SeatResourceUtils() {
    }

    public static synchronized SeatResourceUtils getInstance(String tag) {
        SeatResourceUtils newInstance = null;
        if (tag == null) {
            tag = SINGLE_INSTANCE;
        }
        if (mInstanceMap == null) {
            mInstanceMap = new HashMap<String, SeatResourceUtils>();
            newInstance = new SeatResourceUtils();
            mInstanceMap.put(SINGLE_INSTANCE, newInstance);
        } else {
            newInstance = new SeatResourceUtils();
            mInstanceMap.put(tag, newInstance);
        }
        return newInstance;
    }

    public static SeatResourceUtils getResourceUtils(String tag) {
        if (mInstanceMap != null) {
            return mInstanceMap.get(tag);
        } else {
            return null;
        }
    }

    public static boolean checkTagEnabled(String tag) {
        if (mInstanceMap == null) {
            return true;
        } else {
            Set<String> keys = mInstanceMap.keySet();
            for (String oldTag : keys) {
                if (tag.equals(oldTag)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 按指定宽高加载资源ID指定的图片到内存中
     *
     * @param context      上下文对象,用于获取资源对象
     * @param imageID      资源ID
     * @param targetWidth  目标图片的宽度(此处一般为座位宽度)
     * @param targetHeight 目标图片的高度(此处一般为座位的高度)
     * @return
     */
    public static Bitmap getScaleBitmap(Context context, int imageID, int targetWidth, int targetHeight) {
        try {
            //以流的形式加载比直接使用ID加载到消耗内存会少一些,并且可以指定宽高进行加载
            //加载资源文件到流
            InputStream in = context.getResources().openRawResource(imageID);
            //设置加载选项
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            //设置目标宽高
            options.outWidth = targetWidth;
            options.outHeight = targetHeight;
            //加载图片
            return BitmapFactory.decodeStream(in, null, options);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加载座位图片
     *
     * @param context      上下文对象,用于加载图片
     * @param imageID      用于加载的资源ID，可为null，<font color="#ff9900"><b>但是两个资源参数必须一个不为null，且isReload为true时，此参数不可为null，否则抛出异常</b></font>
     * @param imageBitmap  用于加载的资源图片数组，可为null，<font color="#ff9900"><b>但是两个资源参数必须一个不为null</b></font>
     * @param targetWidth  加载图片的预期宽度
     * @param targetHeight 加载图片的预期高度
     * @param isReload     是否重新加载,若为true则以imageID为准,重新加载所有的bitmap,若为false则根据bitmap是否存在,若不存在则加载imageID的图片,存在则直接使用bitmap
     *                     <p><font color="#ff9900"><b>此参数为true时，参数imageID不可为null，否则抛出异常</b></font></p>
     * @return
     */
    protected Bitmap[] loadSeatImage(Context context, int[] imageID, Bitmap[] imageBitmap, int targetWidth, int targetHeight, boolean isReload) {
        if (imageID == null && isReload) {
            throw new RuntimeException("资源ID不存在,无法重新加载图片资源");
        }
        //检测imageID是否存在
        if (imageID != null) {
            //不需要重新加载且bitmap不为null
            //只要设置了新的图片资源,第一次加载图片时必然使用新的图片资源进行加载
            if (!isReload && imageBitmap != null) {
                boolean isNullObjeact = false;
                //检测是否bitmap数组为空数组
                for (Bitmap bitmap : imageBitmap) {
                    if (bitmap == null) {
                        isNullObjeact = true;
                        break;
                    }
                }
                if (!isNullObjeact) {
                    //不存在空元素直直接使用该bitmap
                    return imageBitmap;
                }
            }
            //存在空元素则重新加载数据
            imageBitmap = new Bitmap[imageID.length];

            for (int i = 0; i < imageID.length; i++) {
                //按预期宽度比例加载图片
                //用于防止原图片太大加载的内存过大
                Bitmap bitmap = getScaleBitmap(context, imageID[i], targetWidth, targetHeight);
                imageBitmap[i] = bitmap;
            }
            return imageBitmap;
        } else if (imageBitmap != null) {
            return imageBitmap;
        } else {
            //即不存在资源ID,也不存在图片文件
            throw new RuntimeException("不存在可加载的图片资源或者已经加载的图片资源!");
        }
    }

//    public boolean recycleResources() {
//        boolean isSucceed = true;
//        if (this.mBitmapArr != null) {
//            for (Bitmap bitmap : mBitmapArr) {
//                if (bitmap != null) {
//                    try {
//                        bitmap.recycle();
//                    } catch (Exception e) {
//                        isSucceed = false;
//                    }
//                }
//            }
//        }
//        this.mDescArr = null;
//        this.mDrawColorArr = null;
//        this.mResIDArr = null;
//        return isSucceed;
//    }

    public void setColors(int[] colors) {
        this.mDrawColorArr = colors;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.mBitmapArr = bitmaps;
    }

    public void setResIDs(int[] resIDs) {
        this.mResIDArr = resIDs;
    }

    public void setDescriptions(String[] descs) {
        this.mDescArr = descs;
    }

    public int[] getColors() {
        return this.mDrawColorArr;
    }

    public Bitmap[] getBitmaps() {
        return this.mBitmapArr;
    }

    public int[] getResIDs() {
        return this.mResIDArr;
    }

    public String[] getDescriptions() {
        return this.mDescArr;
    }

    public void loadImage(Context context, int targetWidth, int targetHeight, boolean isReload) {
        this.mBitmapArr = loadSeatImage(context, this.mResIDArr, this.mBitmapArr, targetWidth, targetHeight, isReload);
    }
}
