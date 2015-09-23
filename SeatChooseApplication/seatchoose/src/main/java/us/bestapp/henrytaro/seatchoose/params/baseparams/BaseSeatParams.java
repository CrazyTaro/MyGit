package us.bestapp.henrytaro.seatchoose.params.baseparams;/**
 * Created by xuhaolin on 15/9/10.
 */

import android.graphics.Color;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.bestapp.henrytaro.seatchoose.params.interfaces.ISeatParams;

/**
 * Created by xuhaolin on 15/9/10.<br/>
 * 座位绘制参数基本类,该类继承了{@link AbsBaseParams}及对外公开接口{@link ISeatParams},此接口所有的方法用于对外公开提供给用户调用;
 * 其余此类中的 {@code public} 方法均是用于提供给绘制时使用的方法,需要自定义参数类时请继承此方法重写部分方法即可
 * <br/>
 * <font color="#ff9900"><b>需要自定义类型时,请务必考虑是否需要清除原有的样式或者添加新的样式</b></font>
 * <br/>
 * <br/>
 * <font color="#ff9900"><b>关于标签及样式的设定,默认情况下{@link BaseSeatParams}预存了4个样式,
 * 其中包括<br/>
 * {@link #TAG_OPTIONAL_SEAT} 可选标签样式<br/>
 * {@link #TAG_SELECTE_SEAT} 已选标签样式<br/>
 * {@link #TAG_LOCK_SEAT} 锁定标签样式<br/>
 * {@link #TAG_COUPLE_OPTIONAL_SEAT} 情侣标签样式<br/></b></font>
 * <br/>
 * 其余的样式并没有预存,但是一样可以使用{@link #TAG_ERROR_SEAT}/{@link #TAG_UNSHOW_SEAT}两个标签,因为这两个样式默认是不进行绘制的,
 * 获取某个标签对象的样式若不存在的情况下,并不会做任何的绘制,所以需要绘制的样式请确保提供的样式及标签是有效的<br/>
 */
public class BaseSeatParams extends AbsBaseParams implements ISeatParams {

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

    //座位间水平间隔
    protected float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    //座位间垂直间隔
    protected float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    //座位与描述文字之间的间隔
    protected float mDrawStyleDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    //座位类型之间的间隔
    protected float mDrawStyleInterval = DEFAULT_SEAT_TYPE_INTERVAL;

    //设置自动计算适应屏幕
    private boolean mIsSetAutoCalculate = false;

    private Map<String, BaseDrawStyle> mDrawStyles = null;

    //是否按指定顺序绘制座位类型
    protected boolean mIsDrawSeatStyleInOrder = false;
    //座位类型指定顺序
    protected List<String> mTagInOrder = null;
    //是否绘制座位类型
    protected boolean mIsDrawSeatDrawStyle = true;

    protected float mDescriptionSize = DEFAULT_DESCRIPTION_SIZE;
    //移动缩放时用于暂时存放缩放前的数据(以便于正常使用比例缩放)
    protected float[] mValueHolder = null;
    protected boolean mIsValueHold = false;
    //用于保存最原始的数据
    protected OriginalValuesHolder mOriginalHolder = null;

    /**
     * 创建并初始化参数
     */
    public BaseSeatParams() {
        this(DEFAULT_SEAT_WIDTH, DEFAULT_SEAT_HEIGHT, DEFAULT_SEAT_RADIUS, DEFAULT_SEAT_COLOR);
        initial();
    }

    /**
     * 创建并初始化参数
     *
     * @param defaultWidth  默认宽度
     * @param defaultHeight 默认高度
     * @param defaultRadius 默认圆角弧度
     * @param defaultColor  默认颜色
     */
    public BaseSeatParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor) {
        super(defaultWidth, defaultHeight, defaultRadius, defaultColor);
        initial();
    }

    /**
     * 初始化
     */
    protected void initial() {
        super.setLargeScaleWidth(3 * this.getWidth());
        super.setSmallScaleWidth(0.5f * this.getHeight());
        this.storeOriginalValues(null);

        int selectColor = Color.rgb(228, 24, 99);
        int optionalColor = Color.WHITE;
        int lockColor = Color.rgb(196, 195, 196);
        int coupleColor = Color.rgb(243, 115, 162);

        mDrawStyles = new HashMap<>();
        BaseDrawStyle selectInfo = new BaseDrawStyle(TAG_SELECTE_SEAT, true, selectColor, selectColor, Color.BLACK, "已选", DEFAULT_INT, null);
        BaseDrawStyle optionalInfo = new BaseDrawStyle(TAG_OPTIONAL_SEAT, true, optionalColor, optionalColor, Color.BLACK, "可选", DEFAULT_INT, null);
        BaseDrawStyle lockInfo = new BaseDrawStyle(TAG_LOCK_SEAT, true, lockColor, lockColor, Color.BLACK, "已售", DEFAULT_INT, null);
        BaseDrawStyle coupleInfo = new BaseDrawStyle(TAG_COUPLE_OPTIONAL_SEAT, true, coupleColor, coupleColor, Color.BLACK, "情侣", DEFAULT_INT, null);

        mDrawStyles.put(TAG_SELECTE_SEAT, selectInfo);
        mDrawStyles.put(TAG_OPTIONAL_SEAT, optionalInfo);
        mDrawStyles.put(TAG_LOCK_SEAT, lockInfo);
        mDrawStyles.put(TAG_COUPLE_OPTIONAL_SEAT, coupleInfo);
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
            mValueHolder = new float[6];
        }
        if (!mIsValueHold) {
            //第一次更新数据记录下最原始的数据
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mSeatVerticalInterval;
            mValueHolder[3] = this.mDrawStyleDescInterval;
            mValueHolder[4] = this.mDrawStyleInterval;
            mValueHolder[5] = this.mDescriptionSize;
            mIsValueHold = true;
        }
        //每一次变化都处理为相对原始数据的变化
        this.setWidth(mValueHolder[0] * scaleRate, false);
        this.setHeight(mValueHolder[1] * scaleRate, false);
        this.mSeatVerticalInterval = mValueHolder[2] * scaleRate;
        this.mDrawStyleDescInterval = mValueHolder[3] * scaleRate;
        this.mDrawStyleInterval = mValueHolder[4] * scaleRate;
        this.mDescriptionSize = mValueHolder[5] * scaleRate;

        //若确认更新数据,则将变化后的数据作为永久性数据进行缓存
        if (isTrueSetValue) {
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mSeatVerticalInterval;
            mValueHolder[3] = this.mDrawStyleDescInterval;
            mValueHolder[4] = this.mDrawStyleInterval;
            mValueHolder[5] = this.mDescriptionSize;
            //重置记录标志
            mIsValueHold = false;
        }
    }


    @Override
    public float getSeatHorizontalInterval() {
        if (this.isDrawThumbnail()) {
            return mSeatHorizontalInterval * this.getThumbnailRate();
        } else {
            return mSeatHorizontalInterval;
        }
    }

    @Override
    public void setSeatHorizontalInterval(float mSeatHorizontalInterval) {
        if (mSeatHorizontalInterval == DEFAULT_FLOAT) {
            this.mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
        } else {
            this.mSeatHorizontalInterval = mSeatHorizontalInterval;
        }
    }

    @Override
    public float getSeatVerticalInterval() {
        if (this.isDrawThumbnail()) {
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
    public float getDrawStyleDescInterval() {
        if (this.isDrawThumbnail()) {
            return mDrawStyleDescInterval * this.getThumbnailRate();
        } else {
            return mDrawStyleDescInterval;
        }
    }

    @Override
    public float getDrawStyleInterval() {
        if (this.isDrawThumbnail()) {
            return mDrawStyleInterval * this.getThumbnailRate();
        } else {
            return mDrawStyleInterval;
        }
    }

    @Override
    public void setDrawStyleInterval(float drawStyleInterval) {
        if (drawStyleInterval == DEFAULT_FLOAT) {
            this.mDrawStyleInterval = DEFAULT_SEAT_TYPE_INTERVAL;
        } else {
            this.mDrawStyleInterval = drawStyleInterval;
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
    protected void updateWidthAndHeightWhenSet(float width, float height) {

    }


    @Override
    public int getDrawStyleLength() {
        return mDrawStyles.size();
    }

    @Override
    public void setIsDrawDrawStyle(boolean isDraw) {
        this.mIsDrawSeatDrawStyle = isDraw;
    }

    @Override
    public boolean isDrawDrawStyle() {
        return this.mIsDrawSeatDrawStyle;
    }

    @Override
    public void setDrawStyleDescInterval(float mSeatTextInterval) {
        if (mSeatTextInterval == DEFAULT_FLOAT) {
            this.mDrawStyleDescInterval = DEFAULT_SEAT_TEXT_INTERVAL;
        } else {
            this.mDrawStyleDescInterval = mSeatTextInterval;
        }
    }

    /**
     * 获取用于绘制情侣座的绘制区域,暂时未使用,每个情侣座为两个座位合并在一起
     *
     * @param coupleRectf   区域对象
     * @param drawPositionX 绘制中心X轴
     * @param drawPositionY 绘制中心Y轴
     * @return
     */
    public RectF getCoupleDrawRecf(RectF coupleRectf, float drawPositionX, float drawPositionY) {
        if (coupleRectf == null) {
            coupleRectf = new RectF();
        }

        float coupleWidth = this.getWidth() * 2 + this.getSeatHorizontalInterval();
        float coupleHeight = this.getHeight();
        coupleRectf.left = drawPositionX - coupleWidth / 2;
        coupleRectf.top = drawPositionY - coupleHeight / 2;
        coupleRectf.right = coupleRectf.left + coupleWidth;
        coupleRectf.bottom = coupleRectf.top + coupleHeight;

        return coupleRectf;
    }

    @Override
    public List<String> getDrawStyleTags() {
        List<String> tagList = new ArrayList<>();
        for (String tag : mDrawStyles.keySet()) {
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public BaseDrawStyle getDrawStyle(String typeTag) {
        return mDrawStyles.get(typeTag);
    }

    @Override
    public BaseDrawStyle addNewDrawStyle(String typeTag, BaseDrawStyle newStyle) {
        return mDrawStyles.put(typeTag, newStyle);
    }

    @Override
    public BaseDrawStyle removeDrawStyle(String typeTag) {
        return mDrawStyles.remove(typeTag);
    }

    @Override
    public void clearDrawStyles() {
        mDrawStyles.clear();
    }

    @Override
    public void setIsDrawStyleByOrder(boolean isInOrder, List<String> tagInOrder) {
        this.mIsDrawSeatStyleInOrder = isInOrder;
        if (isInOrder) {
            this.mTagInOrder = tagInOrder;
        } else {
            this.mTagInOrder = null;
        }
    }

    @Override
    public boolean isDrawStyleByOrder() {
        return this.mIsDrawSeatStyleInOrder;
    }

    @Override
    public List<BaseDrawStyle> getDrawStyles(boolean isInOrder) {
        if (isInOrder) {
            if (this.mIsDrawSeatStyleInOrder && this.mTagInOrder != null) {
                List<BaseDrawStyle> orderStyleList = new ArrayList<>();
                for (String tag : mTagInOrder) {
                    orderStyleList.add(mDrawStyles.get(tag));
                }

                return orderStyleList;
            } else {
                return null;
            }
        } else {
            List<BaseDrawStyle> orderStyleList = new ArrayList<>();
            orderStyleList.addAll(mDrawStyles.values());
            return orderStyleList;
        }
    }

//    @Override
//    public boolean isCanScale(float scaleRate) {
//        boolean isAllow = super.isCanScale(scaleRate);
//        //当已经设置了自动计算功能
//        //即使缩放不合理还是允许进行缩放
//        if (mIsSetAutoCalculate) {
//            //当前允许缩放
//            if (isAllow) {
//                //取消自动计算标志,以后所有的缩放不再参考此参数
//                //按正常缩放
//                mIsSetAutoCalculate = false;
//                return isAllow;
//                //当前不允许缩放,但牌自动计算状态,允许缩放
//            } else {
//                return true;
//            }
//        } else {
//            //不根据自动计算功能进行计算
//            return isAllow;
//        }
//    }

    public void setAutoCalculateToFixScreen(float viewWidth, int columnCount) {
        float eachWidth = viewWidth / (columnCount + 2);
        float thisWidth = eachWidth * 0.8f;
        float thisHeight = thisWidth;
        float thisRadius = eachWidth * 0.2f;
        this.setDefault(thisWidth, thisHeight, thisRadius, DEFAULT_SEAT_COLOR);
        this.mIsSetAutoCalculate = true;
    }

    /**
     * 获取座位类型样式的所有文本
     *
     * @param isInOrder 是否按顺序加载,若使用此功能必须是先设定{@link #setIsDrawStyleByOrder(boolean, List)}
     * @return
     */
    public List<String> getDrawStyleDescription(boolean isInOrder) {
        if (isInOrder) {
            if (mIsDrawSeatStyleInOrder && mTagInOrder != null) {
                List<String> typeDescList = new ArrayList<>();
                for (String tag : mTagInOrder) {
                    typeDescList.add(mDrawStyles.get(tag).description);
                }
                return typeDescList;
            } else {
                return null;
            }
        } else {
            List<String> typeDescList = new ArrayList<>();
            for (BaseDrawStyle style : mDrawStyles.values()) {
                typeDescList.add(style.description);
            }
            return typeDescList;
        }
    }

    /**
     * 获取指定类型的对象
     *
     * @param T 继承自BaseSeatParams
     * @return
     */

    private BaseSeatParams getClassObject(Class<? extends BaseSeatParams> T) {
        try {
            return T.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("当前参数类型无法正常创建对象或者不存在默认构造函数");
        }
    }


    /**
     * 获取自动计算并分离的seatParams,用于座位类型的分批绘制
     *
     * @param seatTypeRowCount 座位类型绘制的行数
     * @return
     */
    public BaseSeatParams[] getAutoSeparateParams(Class<? extends BaseSeatParams> T, int seatTypeRowCount) {
        BaseSeatParams[] seatTypeParams = null;
        //座位类型个数
        int seatTypeLength = this.getDrawStyleLength();
        //每行个数
        int eachRowCount = seatTypeLength / seatTypeRowCount;
        //判断特别情况,当前座位类型个数不能分离成指定行数(行数远超过座位类型个数)
        //取消分离情况,按一行绘制
        if (seatTypeRowCount <= 0) {
            seatTypeParams = new BaseSeatParams[1];
            //创建当前变量类型的对象
            seatTypeParams[0] = this.getSelectableClone(this, this.mDrawStyles);
        } else {
            //正常分行
            //创建分离的行数的参数对象
            seatTypeParams = new BaseSeatParams[seatTypeRowCount];
            List<String> originalTagList = null;
            if (mIsDrawSeatStyleInOrder) {
                originalTagList = mTagInOrder;
            } else {
                originalTagList = new ArrayList<>();
                originalTagList.addAll(this.mDrawStyles.keySet());
            }

            for (int i = 0; i < seatTypeRowCount; i++) {
                Map<String, BaseDrawStyle> newStyleMap = new HashMap<>();
                //计算当前行需要处理的座位类型个数
                //当最后一行时,座位个数不是按计算出来每行个数进行处理
                //而是所有的座位数减去前面N行已处理的座位类型个数,剩下的就是最后一行的座位类型个数
                //此部分保证了奇偶情况座位类型都可以正常分配完毕
                int seatTypeCount = (i + 1) == seatTypeRowCount ? (seatTypeLength - i * eachRowCount) : eachRowCount;
                for (int j = 0; j < seatTypeCount; j++) {
                    String tag = originalTagList.get(i * eachRowCount + j);
                    newStyleMap.put(tag, mDrawStyles.get(tag));
                }

                seatTypeParams[i] = this.getSelectableClone(getClassObject(T), newStyleMap);
            }
        }
        return seatTypeParams;
    }

    @Override
    public Object getClone(Object newObj) {
        if (newObj != null && !(newObj.getClass() != this.getClass())) {
            throw new RuntimeException("对象类型错误,无法进行克隆!");
        } else if (newObj == null) {
            newObj = new BaseSeatParams();
        }
        return this.getSelectableClone((BaseSeatParams) newObj, this.mDrawStyles);
    }

    /**
     * a
     *
     * @param newParams
     * @param styleMap
     * @return
     */
    protected BaseSeatParams getSelectableClone(BaseSeatParams newParams, Map<String, BaseDrawStyle> styleMap) {
        //记录原始的缩略图绘制标志
        boolean isThumbnail = this.isDrawThumbnail();
        //将缩略图绘制状态恢复默认
        //当缩略图状态为true时,获取的数据将不是原始数据
        this.setIsDrawThumbnail(false, DEFAULT_INT, DEFAULT_INT);

        //创建新
        if (newParams == null) {
            newParams = new BaseSeatParams();
        }
        //获取默认原始值
        OriginalValuesHolder holder = (OriginalValuesHolder) this.getOriginalValues();

        //设置默认初始值
        newParams.storeOriginalValues(holder);
        //设置当前值
        newParams.setWidth(this.getWidth(), false);
        newParams.setHeight(this.getHeight(), false);
        newParams.setRadius(this.getRadius());
        newParams.setSeatHorizontalInterval(this.getSeatHorizontalInterval());
        newParams.setSeatVerticalInterval(this.getSeatVerticalInterval());
        newParams.setDrawStyleInterval(this.getDrawStyleInterval());
        newParams.setDrawStyleDescInterval(this.getDrawStyleDescInterval());

        //设置其它的参数值
        newParams.setIsDraw(this.isDraw());
        newParams.setIsDrawThumbnail(isThumbnail, 0, 0);
        newParams.setThumbnailRate(this.getThumbnailRate());
        //设置绘制方式
        newParams.setDrawType(this.getDrawType(true));
        newParams.clearDrawStyles();
        if (styleMap != null) {
            for (Map.Entry<String, BaseDrawStyle> entity : styleMap.entrySet()) {
                newParams.addNewDrawStyle(entity.getKey(), entity.getValue());
            }
        }

        this.setIsDrawThumbnail(isThumbnail, DEFAULT_INT, DEFAULT_INT);
        return newParams;
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
    public float setOriginalValuesToReplaceCurrents(float fixScaleRate) {
        float oldScaleRate = 0f;
        float targetScaleRate = fixScaleRate;
//        //是否缩放到默认最大值
//        if (fixScaleRate) {
//            targetScaleRate = 2f;
//        } else {
//            //缩放到最小值
//            targetScaleRate = 1f;
//        }
        //计算缩放后的值与当前值的比例,作为当前缩放比
        oldScaleRate =
                //计算缩放后的宽值(放大原始的3倍或者还原为原始的大小)
                mOriginalHolder.width * targetScaleRate
                        //当前界面值
                        / this.getWidth();

        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mSeatHorizontalInterval = mOriginalHolder.horizontalInterval * targetScaleRate;
        this.mSeatVerticalInterval = mOriginalHolder.verticalInterval * targetScaleRate;
        this.mDrawStyleInterval = mOriginalHolder.typeInterval * targetScaleRate;
        this.mDrawStyleDescInterval = mOriginalHolder.descInterval * targetScaleRate;
        this.mDescriptionSize = mOriginalHolder.descSize * targetScaleRate;

        return oldScaleRate;
    }

    /**
     * 未使用
     *
     * @param targetScaleRate
     */
    public void setNewParamsValues(float targetScaleRate) {
        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mSeatHorizontalInterval = mOriginalHolder.horizontalInterval * targetScaleRate;
        this.mSeatVerticalInterval = mOriginalHolder.verticalInterval * targetScaleRate;
        this.mDrawStyleInterval = mOriginalHolder.typeInterval * targetScaleRate;
        this.mDrawStyleDescInterval = mOriginalHolder.descInterval * targetScaleRate;
        this.mDescriptionSize = mOriginalHolder.descSize * targetScaleRate;
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
            mOriginalHolder.typeInterval = this.getDrawStyleInterval();
            mOriginalHolder.descSize = this.getDescriptionSize();
            mOriginalHolder.descInterval = this.getDrawStyleDescInterval();
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
