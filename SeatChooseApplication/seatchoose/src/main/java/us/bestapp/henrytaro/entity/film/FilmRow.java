package us.bestapp.henrytaro.entity.film;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 来自JSON数据中的对象
 */
public class FilmRow extends AbsRowEntity {

    @SerializedName("rownum")
    private int mRowNum;
    @SerializedName("rowid")
    private String mRowId;
    @SerializedName("columns")
    private String mColumns;

    private int mExsitColumnCount = 0;

    private AbsSeatEntity[] mColumnData = null;


    /**
     * 构造函数,设置行对象的基本数据,<font color="#ff9900"><b>解析列数据时会自动计算当前行是否需要绘制及数据是否为空,
     * 但在此处是否绘制及数据是否为空将由参数3/4决定,此构造函数不自动进行解析列信息,需要解析请调用方法{@link #parseData()}</b></font>
     *
     * @param rowNumber 行号
     * @param isDraw    是否绘制此行
     * @param isEmpty   此行数据是否为空
     */
    public FilmRow(int x, int rowNumber, boolean isDraw, boolean isEmpty) {
        super(x, rowNumber, isDraw, isEmpty);
        this.mRowNum = rowNumber;
    }

    /**
     * 构造函数,创建一个空行
     *
     * @param x
     * @param rowNumber
     */
    public FilmRow(int x, int rowNumber) {
        super(x, rowNumber);
        this.mRowNum = rowNumber;
    }


    /**
     * 获取行ID
     *
     * @return
     */
    public String getRowId() {
        return this.mRowId;
    }

    @Override
    public int getRowNumber() {
        this.mRowNumber = this.mRowNum;
        return super.getRowNumber();
    }

    /**
     * 解析已经加载的Json数据,此方法解析列信息时会自动解析行是否绘制及数据是否为空,若之前设定过绘制选项此处将会覆盖
     *
     * @return
     */
    @Override
    public void parseData() {

        if (StringUtils.isNullOrEmpty(mColumns)) {
            this.mIsDraw = false;
            this.mIsEmpty = true;
        } else {
            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            //1级分离数据类型
            String[] columInfo = mColumns.split(",");
            if (mColumns != null) {
                mColumnData = new FilmSeat[columInfo.length];
                for (int i = 0; i < columInfo.length; i++) {
                    //座位解析
                    AbsSeatEntity newSeat = new FilmSeat(this.getX(), i, this.getRowNumber(), columInfo[i]);
                    newSeat.parseData();
                    mColumnData[i] = newSeat;
                    if (newSeat != null && newSeat.isExsit()) {
                        mExsitColumnCount++;
                    }
                }
                this.mIsDraw = true;
                this.mIsEmpty = false;
            } else {
                throw new RuntimeException("column info is unable");
            }
        }
    }

    @Override
    public int getColumnCount() {
        return this.mColumnData.length;
    }


    @Override
    public AbsSeatEntity getSeatEntity(int columnIndex) {
        try {
            return this.mColumnData[columnIndex];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
