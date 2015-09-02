package us.bestapp.henrytaro.params;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;

import us.bestapp.henrytaro.params.interfaces.IBaseParamsExport;
import us.bestapp.henrytaro.params.interfaces.ISeatParamsExport;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>座位参数，包括座位绘制需要的各种参数;座位类型及其描述相关参数与在此类设置</p>
 * <p>所有{@code protected}方法及部分{@code public}都是绘制时需要的,对外公开可以进行设置的方法只允许从接口中进行设置,详见{@link ISeatParamsExport}</p>
 */
public final class SeatParams extends BaseParams implements ISeatParamsExport {
    /**
     * 默认座位颜色值
     */
    public static final int DEFAULT_SEAT_COLOR = Color.WHITE;
    /**
     * 默认座位宽度
     */
    public static final float DEFAULT_SEAT_WIDTH = 50f;
    /**
     * 默认座位高度
     */
    public static final float DEFAULT_SEAT_HEIGHT = 50f;
    /**
     * 默认主座位高度
     */
    public static final float DEFAULT_SEAT_MAIN_HEIGHT = DEFAULT_SEAT_HEIGHT * 0.75f;
    /**
     * 默认次座位高度
     */
    public static final float DEFAULT_SEAT_MINOR_HEIGHT = DEFAULT_SEAT_HEIGHT * 0.2f;
    /**
     * 默认主次座位间隔高度
     */
    public static final float DEFAULT_SEAT_HEIGHT_INTERVAL = DEFAULT_SEAT_HEIGHT * 0.05f;
    /**
     * 默认座位列表中座位间的水平间隔宽度
     */
    public static final float DEFAULT_SEAT_HORIZONTAL_INTERVAL = DEFAULT_SEAT_HEIGHT * 0.1f;
    /**
     * 默认座位列表中座位间的垂直间隔宽度
     */
    public static final float DEFAULT_SEAT_VERTICAL_INTERVAL = DEFAULT_SEAT_HEIGHT * 0.8f;
    /**
     * 默认座位类型与其描述文字的间隔宽度
     */
    public static final float DEFAULT_SEAT_TEXT_INTERVAL = 10f;
    /**
     * 默认每个座位类型之前的间隔(此处的座位指包含了其描述文字的全部整体)
     */
    public static final float DEFAULT_SEAT_TYPE_INTERVAL = 50f;
    /**
     * 默认座位圆角度
     */
    public static final float DEFAULT_SEAT_RADIUS = DEFAULT_SEAT_HEIGHT * 0.1f;
    /**
     * 座位默认基本类型,错误座位,即不存在的座位(可能是列表数据不存在,也可能是查询索引超过此列表数据等)
     */
    public static final int SEAT_TYPE_ERRO = -1;
    /**
     * 默认座位类型,分别为<font color="#ff9900"><b>可选,已选,已售</b></font>
     */
    public static int[] DEFAULT_SEAT_TYPE = {1, 2, 3};
    /**
     * 默认座位类型对应的颜色,分别为<br/>
     * <p>
     * 可选=<font color="white"><b>白色</b></font><br/>
     * 已选=<font color="red"><b>红色</b></font><br/>
     * 已售=<font color="#yellow"><b>黄色</b></font><br/>
     * </p>
     */
    public static int[] DEFAULT_SEAT_TYPE_COLOR = {Color.WHITE, Color.RED, Color.YELLOW};
    /**
     * 默认座位类型描述,"可选,已选,已售"
     */
    public static String[] DEFAULT_SEAT_TYPE_DESC = {"可选", "已选", "已售"};
    /**
     * 座位默认基本类型,不可选(可见),<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_DISABLE_SELETED = 3;
    /**
     * 座位默认基本类型,已选,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_SELETED = 2;
    /**
     * 座位默认基本类型,未选,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_UNSELETED = 1;
    /**
     * 座位默认基本类型,不可见,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>,其值与{@link IBaseParamsExport#DRAW_TYPE_NO}保持一致
     */
    public static int SEAT_TYPE_UNSHOW = IBaseParamsExport.DRAW_TYPE_NO;
    //主座位高度, 与次座位一起绘制显示为一个座位,显得好看一点,此参数不对外公开
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    //次座位高度
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    //主次座位之间的间隔
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;

    //座位间水平间隔
    private float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    //座位间垂直间隔
    private float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    //座位与描述文字之间的间隔
    private float mSeatTypeDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    //座位类型之间的间隔
    private float mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;


    //是否绘制座位类型
    private boolean mIsDrawSeatType = true;
    //座位类型数组
    private int[] mSeatTypeArrary = null;
    //座位类型对应的颜色数组
    private int[] mSeatColorArrary = null;
    private int[] mThumbnailColorArray = null;
    //座位类型对应的描述文字
    private String[] mSeatTypeDescription = null;
    //座位类型对应的资源图片ID
    private int[] mSeatImageID = null;
    private Bitmap[] mSeatImageBitmaps = null;

    private float mDescriptionSize = DEFAULT_DESCRIPTION_SIZE;
    //移动缩放时用于暂时存放缩放前的数据(以便于正常使用比例缩放)
    private float[] mValueHolder = null;
    private boolean mIsValueHold = false;
    //用于保存最原始的数据
    private OriginalValuesHolder mOriginalHolder = null;

    /**
     * 创建并初始化参数
     */
    public SeatParams() {
        super(DEFAULT_SEAT_WIDTH, DEFAULT_SEAT_HEIGHT, DEFAULT_SEAT_RADIUS, DEFAULT_SEAT_COLOR);
        initial();
    }

    //初始化
    private void initial() {
        super.setLargeScaleRate(16);
        super.setSmallScaleRate(0.2f);
        resetSeatTypeWithColor();
        mSeatTypeDescription = DEFAULT_SEAT_TYPE_DESC;
        this.storeOriginalValues(null);
    }

    @Override
    public Bitmap[] getImageBitmap() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatImageBitmaps != null) {
            Bitmap[] newArr = new Bitmap[mSeatImageBitmaps.length];
            System.arraycopy(mSeatImageBitmaps, 0, newArr, 0, mSeatImageBitmaps.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public int[] getImageID() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatImageID != null) {
            int[] newArr = new int[mSeatImageID.length];
            System.arraycopy(mSeatImageID, 0, newArr, 0, mSeatImageID.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public void setSeatTypeConstant(int seleted, int unSeleted, int unShow, int disableSeleted) {
        SEAT_TYPE_SELETED = seleted;
        SEAT_TYPE_UNSELETED = unSeleted;
        SEAT_TYPE_UNSHOW = unShow;
        SEAT_TYPE_DISABLE_SELETED = disableSeleted;
    }

    @Override
    public void resetSeatTypeConstant() {
        SEAT_TYPE_SELETED = 2;
        SEAT_TYPE_UNSELETED = 1;
        SEAT_TYPE_DISABLE_SELETED = 3;
        SEAT_TYPE_UNSHOW = IBaseParamsExport.DRAW_TYPE_NO;
    }

    @Override
    public void setIsDrawSeatType(boolean isDrawSeatType) {
        this.mIsDrawSeatType = isDrawSeatType;
    }

    @Override
    public boolean isDrawSeatType() {
        return mIsDrawSeatType;
    }

    /**
     * <font color="#ff9900"><b>此方法不对用户开放使用,设置参数请忽略此方法</b></font><br/>
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate      新的缩放比
     * @param isTrueSetValue 是否将此次缩放结果记录为永久结果
     */
    @Override
    public void setScaleRate(float scaleRate, boolean isTrueSetValue) {
        //创建缓存数据对象
        if (mValueHolder == null) {
            mValueHolder = new float[7];
        }
        if (!mIsValueHold) {
            //第一次更新数据记录下最原始的数据
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mSeatHeightInterval;
            mValueHolder[3] = this.mSeatVerticalInterval;
            mValueHolder[4] = this.mSeatTypeDescInterval;
            mValueHolder[5] = this.mSeatTypeInterval;
            mValueHolder[6] = this.mDescriptionSize;
            mIsValueHold = true;
        }
        //每一次变化都处理为相对原始数据的变化
        this.setWidth(mValueHolder[0] * scaleRate, false);
        this.setHeight(mValueHolder[1] * scaleRate, false);
        this.mSeatHeightInterval = mValueHolder[2] * scaleRate;
        this.mSeatVerticalInterval = mValueHolder[3] * scaleRate;
        this.mSeatTypeDescInterval = mValueHolder[4] * scaleRate;
        this.mSeatTypeInterval = mValueHolder[5] * scaleRate;
        this.mDescriptionSize = mValueHolder[6] * scaleRate;
        //自动计算主次座位高度
        this.autoCalculateSeatShapeHeight(this.getHeight());

        //若确认更新数据,则将变化后的数据作为永久性数据进行缓存
        if (isTrueSetValue) {
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mSeatHeightInterval;
            mValueHolder[3] = this.mSeatVerticalInterval;
            mValueHolder[4] = this.mSeatTypeDescInterval;
            mValueHolder[5] = this.mSeatTypeInterval;
            mValueHolder[6] = this.mDescriptionSize;
            //重置记录标志
            mIsValueHold = false;
        }
    }


    @Override
    public float getSeatHorizontalInterval() {
        if (this.getIsDrawThumbnail()) {
            return mSeatHorizontalInterval * this.getThumbnailRate();
        } else {
            return mSeatHorizontalInterval;
        }
    }

    @Override
    public void setSeatHorizontalInterval(float mSeatHorizontalInterval) {
        if (mSeatHorizontalInterval == DEFAULT_FLOAT) {
            this.mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
        } else {
            this.mSeatHorizontalInterval = mSeatHorizontalInterval;
        }
    }

    @Override
    public float getSeatVerticalInterval() {
        if (this.getIsDrawThumbnail()) {
            return mSeatVerticalInterval * this.getThumbnailRate();
        } else {
            return mSeatVerticalInterval;
        }
    }

    @Override
    public void setSeatVerticalInterval(float mSeatVerticalInterval) {
        if (mSeatVerticalInterval == DEFAULT_FLOAT) {
            this.mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
        } else {
            this.mSeatVerticalInterval = mSeatVerticalInterval;
        }
    }

    @Override
    public float getSeatTypeDescInterval() {
        if (this.getIsDrawThumbnail()) {
            return mSeatTypeDescInterval * this.getThumbnailRate();
        } else {
            return mSeatTypeDescInterval;
        }
    }

    @Override
    public float getSeatTypeInterval() {
        if (this.getIsDrawThumbnail()) {
            return mSeatTypeInterval * this.getThumbnailRate();
        } else {
            return mSeatTypeInterval;
        }
    }

    @Override
    public void setSeatTypeInterval(float mSeatTypeInterval) {
        if (mSeatTypeInterval == DEFAULT_FLOAT) {
            this.mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;
        } else {
            this.mSeatTypeInterval = mSeatTypeInterval;
        }
    }

    /**
     * 获取当前绘制的座位的颜色,<font color="#ff9900"><b>注意此处是当前绘制的座位的颜色,该颜色值只用于当前绘制的座位,此座位包括了普通座位及绘制座位类型时的示例座位</b></font><br/>
     * <p>若使用默认的座位绘制方式,则应该保证在每次座位绘制之前设置该值,否则可能会使后面大量的座位使用同一个颜色值</p>
     *
     * @return
     */
    @Override
    public int getColor() {
        return super.getColor();
    }

    /**
     * 设置绘制时使用的座位颜色，<font color="#ff9900"><b>该颜色并没有特别的意义，但绘制时使用的颜色必定是此颜色</b></font><br/>
     * <p><font color="#ff9900"><b>在任何一次绘制座位之前都必须考虑是否需要调用此方法，否则绘制使用的颜色将是上一次绘制使用的颜色</b></font></p>
     *
     * @param seatColor
     */
    @Override
    public void setColor(int seatColor) {
        super.setColor(seatColor);
    }

    @Override
    public int[] getSeatTypeArrary() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatTypeArrary != null) {
            int[] newArr = new int[mSeatTypeArrary.length];
            System.arraycopy(mSeatTypeArrary, 0, newArr, 0, mSeatTypeArrary.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public int[] getSeatColorArrary() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatColorArrary != null) {
            int[] newArr = new int[mSeatColorArrary.length];
            System.arraycopy(mSeatColorArrary, 0, newArr, 0, mSeatColorArrary.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public int[] getThumbnailColorArrary() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mThumbnailColorArray != null) {
            int[] newArr = new int[mThumbnailColorArray.length];
            System.arraycopy(mThumbnailColorArray, 0, newArr, 0, mThumbnailColorArray.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public String[] getSeatTypeDescription() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatTypeDescription != null) {
            String[] newArr = new String[mSeatTypeDescription.length];
            System.arraycopy(mSeatTypeDescription, 0, newArr, 0, mSeatTypeDescription.length);
            return newArr;
        } else {
            return null;
        }
    }

    @Override
    public int getSeatTypeLength() {
        if (mSeatTypeArrary != null) {
            return mSeatTypeArrary.length;
        } else {
            return 0;
        }
    }

    /**
     * 设置绘制类型,<font color="#ff9900"><b>设置绘制方式为图像类型时,必须存在图片资源或者图像资源,否则抛出异常</b></font>,设置此方法前应该先设置图片资源ID{@link #setImage(int[])}或图片对象{@link #setImage(Bitmap[])}
     * <p>
     * {@link #DRAW_TYPE_DEFAULT},默认绘制方式,使用图形及颜色<br/>
     * {@link #DRAW_TYPE_IMAGE},图片绘制方式,使用图片进行填充<br/>
     * </p>
     *
     * @param drawType
     */
    @Override
    public void setDrawType(int drawType) {
        if (drawType == DRAW_TYPE_IMAGE && (mSeatImageID == null || mSeatImageBitmaps == null)) {
            throw new RuntimeException("设置绘制方式为图像类型时,必须存在图片资源或者图像资源!");
        } else {
            super.setDrawType(drawType);
        }
    }

    @Override
    public void setImage(int[] imageID) {
        if (mSeatTypeArrary == null) {
            throw new RuntimeException("座位类型数组不可为null，请调用方法设置座位类型数组seatTypeArr");
        }
        mSeatImageID = super.setImage(imageID, mSeatTypeArrary.length, mSeatImageID);
    }


    @Override
    public void setImage(Bitmap[] imageBitmap) {
        if (mSeatTypeArrary == null) {
            throw new RuntimeException("座位类型数组不可为null，请调用方法设置座位类型数组seatTypeArr");
        }
        mSeatImageBitmaps = super.setImage(imageBitmap, mSeatTypeArrary.length, mSeatImageBitmaps);
    }

    @Override
    public void setThumbnailSeatColor(int[] colorArr) {
        if (mSeatTypeArrary == null) {
            throw new RuntimeException("座位类型数组不可为null，请调用方法设置座位类型数组seatTypeArr");
        }
        if (colorArr == null && mSeatTypeArrary.length != colorArr.length) {
            throw new RuntimeException("参数不可为null,且参数值的length必须与座位类型length相同,可通过方法getSeatTypeLength()获取");
        }
        mThumbnailColorArray = new int[colorArr.length];
        System.arraycopy(colorArr, 0, mThumbnailColorArray, 0, colorArr.length);
    }


    /**
     * 设置座位的高度,此方法的高度直接用于绘制图片座位的高度;且该方法会自动计算默认绘制方式的主次座位部分的高度
     *
     * @param height
     */
    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        this.autoCalculateSeatShapeHeight(height);
    }

    @Override
    public void setSeatTypeDescInterval(float mSeatTextInterval) {
        if (mSeatTextInterval == DEFAULT_FLOAT) {
            this.mSeatTypeDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
        } else {
            this.mSeatTypeDescInterval = mSeatTextInterval;
        }
    }


    /**
     * 根据座位类型来确定座位是否需要绘制，当座位类型为{@link #SEAT_TYPE_UNSHOW}时，不绘制该座位
     * <p><font color="#ff9900"><b>{@link #SEAT_TYPE_UNSHOW}该静态变量可改变设置,默认0为不绘制类型,可自定义设置</b></font></p>
     *
     * @param seatType 座位类型
     */
    public void setIsDraw(int seatType) {
        if (seatType == SEAT_TYPE_UNSHOW) {
            super.setIsDraw(false);
        } else {
            super.setIsDraw(true);
        }
    }

    /**
     * <p><b>通常情况不建议使用该方法,座位是否绘制由座位的类型决定(存在不可绘制{@link #SEAT_TYPE_UNSHOW}的座位类型),
     * 是否绘制一般由座位的类型决定,通过{@link #setIsDraw(int)}决定座位是否绘制</b></p>
     */
    @Override
    public void setIsDraw(boolean isDraw) {
        super.setIsDraw(isDraw);
    }

    @Override
    public void setDefaultSeatType(int firstSeatType, int secondSeatType, int thirdSeatType) {
        if (DEFAULT_SEAT_TYPE != null && DEFAULT_SEAT_TYPE.length == 3) {
            DEFAULT_SEAT_TYPE[0] = firstSeatType;
            DEFAULT_SEAT_TYPE[1] = secondSeatType;
            DEFAULT_SEAT_TYPE[2] = thirdSeatType;
        } else {
            throw new RuntimeException("默认座位类型不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    @Override
    public void resetDefaultSeatParams() {
        DEFAULT_SEAT_TYPE = new int[]{1, 2, 3};
        DEFAULT_SEAT_TYPE_COLOR = new int[]{Color.BLACK, Color.RED, Color.YELLOW};
        DEFAULT_SEAT_TYPE_DESC = new String[]{"可选", "已选", "已售"};
    }

    @Override
    public void setDefaultSeatColor(int firstColor, int secondColor, int thirdColor) {
        //确保座位类型回到默认状态
        if (DEFAULT_SEAT_TYPE_COLOR != null && DEFAULT_SEAT_TYPE_COLOR.length == 3) {
            DEFAULT_SEAT_TYPE_COLOR[0] = firstColor;
            DEFAULT_SEAT_TYPE_COLOR[1] = secondColor;
            DEFAULT_SEAT_TYPE_COLOR[2] = thirdColor;
        } else {
            throw new RuntimeException("默认座位类型颜色不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    @Override
    public void setDefaultSeatTypeDescription(String firstDesc, String secondDesc, String thirdDesc) {
        //确保座位类型回来默认状态
        if (DEFAULT_SEAT_TYPE_DESC != null && DEFAULT_SEAT_TYPE_DESC.length == 3) {
            DEFAULT_SEAT_TYPE_DESC[0] = firstDesc;
            DEFAULT_SEAT_TYPE_DESC[1] = secondDesc;
            DEFAULT_SEAT_TYPE_DESC[2] = thirdDesc;
        } else {
            throw new RuntimeException("默认座位描述变量不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    @Override
    public void setExtraSeatTypeWithColor(int[] seatExtraTypeArr, int[] colorExtraArr, int[] thumbnailColorExtraArr, String[] seatTypeExtraDesc) {
        if (seatExtraTypeArr != null && colorExtraArr != null && seatExtraTypeArr.length == colorExtraArr.length) {
            if (thumbnailColorExtraArr != null && thumbnailColorExtraArr.length != seatExtraTypeArr.length) {
                throw new RuntimeException("缩略图座位颜色thumbnailColorArr参数不为null时其length必须与座位类型length相同,\n否则请将该参数值置为null,则将引用colorExtraArr的作为其值");
            }
            if (seatTypeExtraDesc != null && seatTypeExtraDesc.length != seatExtraTypeArr.length) {
                throw new RuntimeException("座位类型描述不可为null,设置额外的座位类型描述length应与额外的座位类型length一致");
            }

            //创建新数组
            int[] newSeatTypeArr = new int[3 + seatExtraTypeArr.length];
            int[] newSeatColorArr = new int[3 + seatExtraTypeArr.length];
            int[] newThumbnailColorArr = new int[3 + seatExtraTypeArr.length];
            String[] newSeatTypeDescription = new String[3 + seatExtraTypeArr.length];
            //载入默认类型及颜色参数
            System.arraycopy(DEFAULT_SEAT_TYPE, 0, newSeatTypeArr, 0, DEFAULT_SEAT_TYPE.length);
            System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, newSeatColorArr, 0, DEFAULT_SEAT_TYPE_COLOR.length);
            System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, newThumbnailColorArr, 0, DEFAULT_SEAT_TYPE_COLOR.length);
            System.arraycopy(DEFAULT_SEAT_TYPE_DESC, 0, newSeatTypeDescription, 0, DEFAULT_SEAT_TYPE_DESC.length);

            //添加新增额外的类型与颜色参数
            for (int i = 3; i < newSeatTypeArr.length; i++) {
                newSeatTypeArr[i] = seatExtraTypeArr[i - 3];
                newSeatColorArr[i] = colorExtraArr[i - 3];
                newThumbnailColorArr[i] = colorExtraArr[i - 3];
                newSeatTypeDescription[i] = seatTypeExtraDesc[i - 3];
            }
            if (thumbnailColorExtraArr != null) {
                for (int i = 3; i < newSeatTypeArr.length; i++) {
                    newThumbnailColorArr[i] = thumbnailColorExtraArr[i - 3];
                }
            }

            mSeatTypeArrary = newSeatTypeArr;
            mSeatColorArrary = newSeatColorArr;
            mThumbnailColorArray = newThumbnailColorArr;
            mSeatTypeDescription = newSeatTypeDescription;
        } else {
            throw new RuntimeException("设置额外座位类型及颜色失败,请确认参数不可为null且参数值的length必须相同");
        }
    }

    @Override
    public void setSeatTypeWithImage(int[] seatTypeArr, int[] thumbnailColorArr, int[] imageID) {
        if (seatTypeArr != null && thumbnailColorArr != null && seatTypeArr.length == thumbnailColorArr.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);

            mThumbnailColorArray = new int[thumbnailColorArr.length];
            System.arraycopy(thumbnailColorArr, 0, mThumbnailColorArray, 0, thumbnailColorArr.length);
            super.setImage(imageID, seatTypeArr.length, mSeatImageID);
        } else {
            throw new RuntimeException("设置新座位类型及图片ID失败,请确认参数不可为null及缩略图座位颜色thumbnailColorArr参数的length与座位类型length相同");
        }
    }

    @Override
    public void setSeatTypeWithImage(int[] seatTypeArr, int[] thumbnailColorArr, Bitmap[] imageBitmap) {

        if (seatTypeArr != null && thumbnailColorArr != null && seatTypeArr.length == thumbnailColorArr.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);

            mThumbnailColorArray = new int[thumbnailColorArr.length];
            System.arraycopy(thumbnailColorArr, 0, mThumbnailColorArray, 0, thumbnailColorArr.length);
            super.setImage(imageBitmap, seatTypeArr.length, mSeatImageBitmaps);
        } else {
            throw new RuntimeException("设置新座位类型及图片ID失败,请确认参数不可为null及缩略图座位颜色thumbnailColorArr参数的length与座位类型length相同");
        }
    }

    @Override
    public void setAllSeatTypeWithColor(int[] seatTypeArr, int[] colorArr, int[] thumbnailColorArr, String[] seatTypeDesc) {
        if (seatTypeArr != null && colorArr != null && seatTypeArr.length == colorArr.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);
            mSeatColorArrary = new int[colorArr.length];
            System.arraycopy(colorArr, 0, mSeatColorArrary, 0, colorArr.length);

            //缩略图颜色值不为null且长度与座位类型相同
            if (thumbnailColorArr != null && thumbnailColorArr.length == seatTypeArr.length) {
                //设置独立的缩略图座位颜色
                mThumbnailColorArray = new int[thumbnailColorArr.length];
                System.arraycopy(thumbnailColorArr, 0, mThumbnailColorArray, 0, thumbnailColorArr.length);
            } else if (thumbnailColorArr == null) {
                //缩略图座位颜色为null,以colorArr作为其值
                mThumbnailColorArray = new int[colorArr.length];
                System.arraycopy(colorArr, 0, mThumbnailColorArray, 0, colorArr.length);
            } else {
                throw new RuntimeException("缩略图座位颜色不为null时,其length必须与新设置的seatTypeArr的length一致;\n否则请将此参数值设置为null,则将引用colorArr作为缩略图座位的颜色值");
            }

            if ((seatTypeDesc != null && seatTypeDesc.length == seatTypeArr.length)) {
                mSeatTypeDescription = new String[seatTypeDesc.length];
                System.arraycopy(seatTypeDesc, 0, mSeatTypeDescription, 0, seatTypeDesc.length);
            } else {
                throw new RuntimeException("座位类型描述不可为null,设置座位类型描述length应与座位类型length一致");
            }

            super.setDrawType(DRAW_TYPE_DEFAULT);
        } else {
            throw new RuntimeException("设置新座位类型及颜色失败,请确认参数不可为null且参数值的length必须相同");
        }
    }

    @Override
    public void resetSeatTypeWithColor() {
        //载入默认类型及颜色参数
        mSeatTypeArrary = new int[DEFAULT_SEAT_TYPE.length];
        mSeatColorArrary = new int[DEFAULT_SEAT_TYPE_COLOR.length];
        mThumbnailColorArray = new int[DEFAULT_SEAT_TYPE_COLOR.length];
        System.arraycopy(DEFAULT_SEAT_TYPE, 0, mSeatTypeArrary, 0, DEFAULT_SEAT_TYPE.length);
        System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, mSeatColorArrary, 0, DEFAULT_SEAT_TYPE_COLOR.length);
        System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, mThumbnailColorArray, 0, DEFAULT_SEAT_TYPE_COLOR.length);

        super.setDrawType(DRAW_TYPE_DEFAULT);
    }

    /**
     * 获取自动计算并分离的seatParams,用于座位类型的分批绘制
     *
     * @param seatTypeRowCount 座位类型绘制的行数
     * @return
     */
    public SeatParams[] getAutoSeparateParams(int seatTypeRowCount) {
        SeatParams[] seatTypeParams = null;
        //座位类型个数
        int seatTypeLength = this.getSeatTypeLength();
        //每行个数
        int eachRowCount = seatTypeLength / seatTypeRowCount;
        //判断特别情况,当前座位类型个数不能分离成指定行数(行数远超过座位类型个数)
        //取消分离情况,按一行绘制
        if (seatTypeRowCount <= 0) {
            seatTypeParams = new SeatParams[1];
            seatTypeParams[0] = this;
        } else {
            //正常分行
            //创建分离的行数的参数对象
            seatTypeParams = new SeatParams[seatTypeRowCount];

            for (int i = 0; i < seatTypeRowCount; i++) {
                //计算当前行需要处理的座位类型个数
                //当最后一行时,座位个数不是按计算出来每行个数进行处理
                //而是所有的座位数减去前面N行已处理的座位类型个数,剩下的就是最后一行的座位类型个数
                //此部分保证了奇偶情况座位类型都可以正常分配完毕
                int seatTypeCount = (i + 1) == seatTypeRowCount ? (seatTypeLength - i * eachRowCount) : eachRowCount;
                //创建必须使用的参数数组
                int[] seatTypeArr = new int[seatTypeCount];
                int[] thumbnailColorArr = new int[seatTypeCount];
                String[] descArr = new String[seatTypeCount];

                //创建可能需要使用的参数数组
                int[] colorArr = null;
                int[] imageIDArr = null;
                Bitmap[] bitmapArr = null;

                //复制必须使用的参数数组
                System.arraycopy(mSeatTypeArrary, i * eachRowCount, seatTypeArr, 0, seatTypeCount);
                System.arraycopy(mThumbnailColorArray, i * eachRowCount, thumbnailColorArr, 0, seatTypeCount);
                System.arraycopy(mSeatTypeDescription, i * eachRowCount, descArr, 0, seatTypeCount);

                //实例化并复制可能需要使用的参数数组
                if (mSeatColorArrary != null) {
                    colorArr = new int[seatTypeCount];
                    System.arraycopy(mSeatColorArrary, i * eachRowCount, colorArr, 0, seatTypeCount);
                }
                if (mSeatImageID != null) {
                    imageIDArr = new int[seatTypeCount];
                    System.arraycopy(mSeatImageID, i * eachRowCount, imageIDArr, 0, seatTypeCount);
                }
                if (mSeatImageBitmaps != null) {
                    bitmapArr = new Bitmap[seatTypeCount];
                    System.arraycopy(mSeatImageBitmaps, i * eachRowCount, bitmapArr, 0, seatTypeCount);
                }

                //按提供参数进行克隆
                seatTypeParams[i] = (SeatParams) this.getSelectableClone(seatTypeArr, colorArr, thumbnailColorArr, descArr, imageIDArr, bitmapArr);
            }
        }

        return seatTypeParams;
    }

    @Override
    public Object getClone() {
        //按自身完整地参数数组进行克隆
        return this.getSelectableClone(this.mSeatTypeArrary, this.mSeatColorArrary, this.mThumbnailColorArray, this.mSeatTypeDescription,
                this.mSeatImageID, this.mSeatImageBitmaps);
    }

    /**
     * 可选参数的自我复制,对象的复制以参数为准(不检测出错情况,参数不合法依然会报错)
     *
     * @param seatTypeArr       座位类型
     * @param seatColorArr      座位颜色
     * @param thumbnailColorArr 缩略图座位颜色
     * @param descArr           描述文本
     * @param imageIDArr        座位图片资源ID
     * @param bitmapArr         座位图片资源Bitmap
     * @return
     */
    protected Object getSelectableClone(int[] seatTypeArr, int[] seatColorArr, int[] thumbnailColorArr, String[] descArr, int[] imageIDArr, Bitmap[] bitmapArr) {
        //记录原始的缩略图绘制标志
        boolean isThumbnail = this.getIsDrawThumbnail();
        //将缩略图绘制状态恢复默认
        //当缩略图状态为true时,获取的数据将不是原始数据
        this.setIsDrawThumbnail(false, DEFAULT_INT, DEFAULT_INT);

        //创建新
        SeatParams newObj = new SeatParams();
        //获取默认原始值
        OriginalValuesHolder holder = (OriginalValuesHolder) this.getOriginalValues();

        //设置默认初始值
        newObj.storeOriginalValues(holder);
        //设置当前值
        newObj.setWidth(this.getWidth(), false);
        newObj.setHeight(this.getHeight(), false);
        newObj.setRadius(this.getRadius());
        newObj.setSeatHorizontalInterval(this.getSeatHorizontalInterval());
        newObj.setSeatVerticalInterval(this.getSeatVerticalInterval());
        newObj.setSeatTypeInterval(this.getSeatTypeInterval());
        newObj.setSeatTypeDescInterval(this.getSeatTypeDescInterval());

        //设置其它的参数值
        newObj.setIsDraw(this.getIsDraw());
        newObj.setIsDrawThumbnail(isThumbnail, 0, 0);
        newObj.setThumbnailRate(this.getThumbnailRate());
        newObj.setAllSeatTypeWithColor(seatTypeArr, seatColorArr, thumbnailColorArr, descArr);
        newObj.autoCalculateSeatShapeHeight(this.getHeight());
        //设置图片资源
        newObj.setImage(imageIDArr);
        newObj.setImage(bitmapArr);
        //设置绘制方式
        //此部分必须在最后设置,因为一旦设置了图片资源,则会默认将绘制方式修改为图片绘制模式
        newObj.setDrawType(this.getDrawType(true));

        this.setIsDrawThumbnail(isThumbnail, DEFAULT_INT, DEFAULT_INT);
        return newObj;
    }

    /**
     * 加载座位图片
     *
     * @param context  上下文对象,用于加载图片
     * @param isReload 是否重新加载,若为true则以imageID为准,重新加载所有的bitmap,若为false则根据bitmap是否存在,若不存在则加载imageID的图片,存在则直接使用bitmap
     */
    protected void loadSeatImage(Context context, boolean isReload) {
        mSeatImageBitmaps = super.loadSeatImage(context, mSeatImageID, mSeatImageBitmaps, (int) this.getWidth(), (int) this.getHeight(), isReload);
    }

    /**
     * 根据座位类型获取座位类型对应的颜色
     *
     * @param seatType 座位类型
     *                 <p>默认座位类型
     *                 可选座位<br/>
     *                 已选座位<br/>
     *                 已售座位<br/>
     *                 </p>
     * @return 返回对应的座位颜色, 若查询不到对应的座位类型颜色则返回默认颜色值 {@link #DEFAULT_SEAT_COLOR}
     */
    public int getSeatColorByType(int seatType) {
        int[] colorArr = null;
        if (this.getIsDrawThumbnail()) {
            colorArr = mThumbnailColorArray;
        } else {
            colorArr = mSeatColorArrary;
        }

        if (mSeatTypeArrary != null) {
            for (int i = 0; i < mSeatTypeArrary.length; i++) {
                if (seatType == mSeatTypeArrary[i]) {
                    return colorArr[i];
                }
            }
            return DEFAULT_SEAT_COLOR;
        } else {
            throw new RuntimeException("获取座位类型对应的颜色值失败!查询不到对应的座位类型或不存在该座位类型");
        }
    }

    /**
     * 根据座位类型获取座位绘制的图片
     *
     * @param context  上下文对象,用于加载图片
     * @param seatType 座位类型
     * @return 返回座位类型对应的图片, 可能为null
     */
    public Bitmap getSeatBitmapByType(Context context, int seatType) {
        loadSeatImage(context, false);
        if (mSeatTypeArrary != null) {
            for (int i = 0; i < mSeatTypeArrary.length; i++) {
                if (seatType == mSeatTypeArrary[i]) {
                    return mSeatImageBitmaps[i];
                }
            }
            return null;
        } else {
            throw new RuntimeException("获取座位类型对应的图片失败!查询不到对应的座位类型或不存在该座位类型");
        }
    }

    /**
     * 根据座位高度自动计算座位绘制的主座位与次座位高度
     *
     * @param seatHeight
     */
    protected void autoCalculateSeatShapeHeight(float seatHeight) {
        float newRadius = seatHeight * 0.1f;
        this.setRadius(newRadius > 20f ? 20f : newRadius);
        this.mMainSeatHeight = seatHeight * 0.75f;
        this.mMinorSeatHeight = seatHeight * 0.2f;
        this.mSeatHeightInterval = seatHeight * 0.05f;

        this.mSeatHorizontalInterval = seatHeight * 0.2f;
        this.mSeatVerticalInterval = seatHeight * 0.8f;
    }

    /**
     * 获取计算后的图片绘制区域(即一个完整地座位占用的区域)
     *
     * @param imageRecft    图片绘制区域,可为null,用于重复利用
     * @param drawPositionX 绘制的X轴中心位置
     * @param drawPositionY 绘制的Y轴中心位置
     * @return
     */
    public RectF getSeatDrawImageRecf(RectF imageRecft, float drawPositionX, float drawPositionY) {
        if (imageRecft == null) {
            imageRecft = new RectF();
        }
        float imageWidth = this.getWidth();
        float imageHeight = this.getHeight();
        imageRecft.left = drawPositionX - imageWidth / 2;
        imageRecft.right = imageRecft.left + imageWidth;
        imageRecft.top = drawPositionY - imageHeight / 2;
        imageRecft.bottom = imageRecft.top + imageHeight;
        return imageRecft;
    }

    /**
     * 获取主座位绘制矩形或者次座位绘制矩形
     *
     * @param seatRectf     座位绘制矩形对象
     * @param drawPositionX 绘制座位的中心X轴位置
     * @param drawPositionY 绘制座位的中心Y轴位置
     * @param isMainSeat    true为获取主座位,false为获取次座位
     * @return
     */
    public RectF getSeatDrawDefaultRectf(RectF seatRectf, float drawPositionX, float drawPositionY, boolean isMainSeat) {
        if (seatRectf == null) {
            seatRectf = new RectF();
        }
        seatRectf.left = drawPositionX - this.getWidth() / 2;
        seatRectf.right = seatRectf.left + this.getWidth();

        seatRectf.top = drawPositionY - this.getHeight() / 2;
        seatRectf.bottom = seatRectf.top + this.mMainSeatHeight;

        if (!isMainSeat) {
            seatRectf.top = seatRectf.bottom + this.mSeatHeightInterval + this.mMinorSeatHeight / 2;
            seatRectf.bottom = seatRectf.top + this.mMinorSeatHeight;
        }

        return seatRectf;
    }

    @Override
    public float getScaleRateCompareToOriginal() {
        if (mOriginalHolder != null) {
            //计算当前值与原始值的缩放比
            return this.getWidth() / mOriginalHolder.width;
        } else {
            return DEFAULT_FLOAT;
        }
    }

    @Override
    public float setOriginalValuesToReplaceCurrents(boolean isSetEnlarge) {
        float oldScaleRate = 0f;
        float targetScaleRate = 0f;
        //是否缩放到默认最大值
        if (isSetEnlarge) {
            targetScaleRate = 3f;
        } else {
            //缩放到最小值
            targetScaleRate = 1f;
        }
        //计算缩放后的值与当前值的比例,作为当前缩放比
        oldScaleRate = mOriginalHolder.width / this.getWidth();

        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mSeatHorizontalInterval = mOriginalHolder.horizontalInterval * targetScaleRate;
        this.mSeatVerticalInterval = mOriginalHolder.verticalInterval * targetScaleRate;
        this.mSeatTypeInterval = mOriginalHolder.typeInterval * targetScaleRate;
        this.mSeatTypeDescInterval = mOriginalHolder.descInterval * targetScaleRate;
        this.mDescriptionSize = mOriginalHolder.descSize * targetScaleRate;
        //计算座位参数
        this.autoCalculateSeatShapeHeight(this.getHeight());

        return oldScaleRate;
    }


    @Override
    public void storeOriginalValues(Object copyObj) {
        if (mOriginalHolder == null) {
            mOriginalHolder = new OriginalValuesHolder();
        }
        if (copyObj == null) {
            mOriginalHolder.width = this.getWidth();
            mOriginalHolder.height = this.getHeight();
            mOriginalHolder.radius = this.getRadius();
            mOriginalHolder.horizontalInterval = this.getSeatHorizontalInterval();
            mOriginalHolder.verticalInterval = this.getSeatVerticalInterval();
            mOriginalHolder.typeInterval = this.getSeatTypeInterval();
            mOriginalHolder.descSize = this.getDescriptionSize();
            mOriginalHolder.descInterval = this.getSeatTypeDescInterval();
        } else if (copyObj instanceof OriginalValuesHolder) {
            OriginalValuesHolder newHolder = (OriginalValuesHolder) copyObj;
            mOriginalHolder.width = newHolder.width;
            mOriginalHolder.height = newHolder.height;
            mOriginalHolder.radius = newHolder.radius;
            mOriginalHolder.horizontalInterval = newHolder.horizontalInterval;
            mOriginalHolder.verticalInterval = newHolder.verticalInterval;
            mOriginalHolder.typeInterval = newHolder.typeInterval;
            mOriginalHolder.descInterval = newHolder.descInterval;
            mOriginalHolder.descSize = newHolder.descSize;
        } else {
            throw new RuntimeException("参数类型出错,请根据注释提醒进行传参");
        }
    }

    @Override
    public Object getOriginalValues() {
        return mOriginalHolder;
    }

    /**
     * 原始座位数据的保存,此处的原始是指<font color="#ff9900"><b>座位默认设定的宽高或者用户设定的宽高,即第一次运行并显示出来的界面即为原始界面;
     * 当用户对宽高做修改时,也会重新记录此数据</b></font>
     */
    protected class OriginalValuesHolder {
        public float width = 0f;
        public float height = 0f;
        public float radius = 0f;
        public float horizontalInterval = 0f;
        public float verticalInterval = 0f;
        public float typeInterval = 0f;
        public float descInterval = 0f;
        public float descSize = 0f;
    }

}
