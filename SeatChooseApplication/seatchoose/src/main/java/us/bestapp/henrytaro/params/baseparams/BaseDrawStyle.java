package us.bestapp.henrytaro.params.baseparams;/**
 * Created by xuhaolin on 15/9/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by xuhaolin on 15/9/16.
 * 此类用于存放座位及舞台绘制的相关参数,如颜色/图片/描述文字等
 */
public class BaseDrawStyle {
    /**
     * 样式标签
     */
    public String tag = null;
    /**
     * 是否绘制此样式
     */
    public boolean isDraw = true;
    /**
     * 样式绘制颜色
     */
    public int drawColor = BaseSeatParams.DEFAULT_SEAT_COLOR;
    /**
     * 样式对应的缩略图绘制颜色
     */
    public int thumbnailColor = BaseSeatParams.DEFAULT_SEAT_COLOR;
    /**
     * 样式描述文本颜色
     */
    public int descColor = Color.BLACK;
    /**
     * 样式描述文本
     */
    public String description = null;
    /**
     * 样式资源ID
     */
    public int imageID = BaseSeatParams.DEFAULT_INT;
    /**
     * 样式资源
     */
    public Bitmap bitmap = null;

    /**
     * 构造函数,用于创建一个样式
     *
     * @param tag            此样式的标签,此处的标签与此样式对应,即与{@link us.bestapp.henrytaro.params.interfaces.ISeatParams#addNewDrawStyle(String, BaseDrawStyle)}中的tag是同一个标签
     *                       (建议使用同一个标签,实际上此标签并没有被使用到,但会随此样式返回到某些接口中),可能需要被用户使用到(如重写某些绘制方法等)
     * @param isDraw         此样式是否需要绘制,此变量主要是用于区域需要绘制及不需要绘制的样式,但实际上若一个标签没有对应的样式则该标签所属的座位将不会被绘制
     * @param drawColor      此样式使用的指定绘制颜色
     * @param thumbnailColor 此样式使用的指定缩略图绘制颜色
     * @param descColor      此样式描述文本的颜色
     * @param description    此样式的描述文本
     * @param imageID        此样式使用的图片资源ID(若需要绘制图片型的座位)
     * @param bitmap         此样式使用的图片资源,可为null
     */
    public BaseDrawStyle(String tag, boolean isDraw, int drawColor, int thumbnailColor, int descColor, String description, int imageID, Bitmap bitmap) {
        this.tag = tag;
        this.isDraw = isDraw;
        this.drawColor = drawColor;
        this.thumbnailColor = thumbnailColor;
        this.descColor = descColor;
        this.description = description;
        this.imageID = imageID;
        this.bitmap = bitmap;
    }

    /**
     * 设置或修改参数,参数意义同构造函数
     *
     * @param tag
     * @param isDraw
     * @param drawColor
     * @param thumbnailColor
     * @param descColor
     * @param description
     * @param imageID
     * @param bitmap
     */
    public void setParams(String tag, boolean isDraw, int drawColor, int thumbnailColor, int descColor, String description, int imageID, Bitmap bitmap) {
        this.tag = tag;
        this.isDraw = isDraw;
        this.drawColor = drawColor;
        this.thumbnailColor = thumbnailColor;
        this.descColor = descColor;
        this.description = description;
        this.imageID = imageID;
        this.bitmap = bitmap;
    }


    /**
     * 加载资源,此处的图片资源加载可能结果为null(当加载不成功时)
     *
     * @param context      上下文对象
     * @param isReload     是否强制重新加载,若此参数为true,则以imageID为准重新加载(不管bitmap是否存在),
     *                     若参数为false,则存在bitmap时直接返回bitmap,否则加载imageID
     * @param targetWidth  加载的目标宽度
     * @param targetHeight 加载图片的目标高度
     */
    public Bitmap loadImage(Context context, boolean isReload, int targetWidth, int targetHeight) {
        this.bitmap = loadImage(context, this.imageID, this.bitmap, targetWidth, targetHeight, isReload);
        return this.bitmap;
    }


    /**
     * 加载图片资源,可能返回null(加载不成功的情况)
     *
     * @param context
     * @param imageID      资源ID
     * @param imageBitmap  图片资源
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @param isReload     是否强制重新加载
     * @return
     */
    protected Bitmap loadImage(Context context, int imageID, Bitmap imageBitmap, int targetWidth, int targetHeight, boolean isReload) {
        //检测imageID是否存在
        if (isReload) {
            return getScaleBitmap(context, imageID, targetWidth, targetHeight);
        } else if (imageBitmap != null) {
            return imageBitmap;
        } else {
            return getScaleBitmap(context, imageID, targetWidth, targetHeight);
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
            Log.i("error", ex.getMessage());
            return null;
        }
    }
}
