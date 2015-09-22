package us.bestapp.henrytaro.utils;/**
 * Created by xuhaolin on 15/9/22.
 */

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhaolin on 15/9/22.
 */
public class PathUtils {

    /**
     * 根据座标点数据计算得到路径(最少需要4个数据,才可以构成最少的两个点)
     *
     * @param data 座位点数据(按X坐标,Y坐标的顺序进行)
     * @return
     */
    public static List<PointF> getPathPoint(int[] data) {
        //数据不合法或者数量不够
        if (data == null || (data.length & 1) != 0 || data.length < 4) {
            return null;
        } else {
            //路径点
            List<PointF> pathPoints = new ArrayList<>();
            //起始坐标点
            PointF startPoint = new PointF(data[0], data[1]);
            //第二个坐标点
            PointF tempPoint = new PointF(data[2], data[3]);
            //上一次的坐标点
            PointF lastPoint = new PointF();
            //当前使用的直线斜率
            float tan = 0f;
            //计算直线斜率
            tan = (tempPoint.y - startPoint.y) / (tempPoint.x - startPoint.x);
            //将第二个点记录为上一次的坐标点
            lastPoint.y = tempPoint.y;
            lastPoint.x = tempPoint.x;

            //将起始坐标点添加到路径中
            //必须创建新点对象,否则会被修改
            pathPoints.add(new PointF(startPoint.x, startPoint.y));
            for (int i = 4; i < data.length - 1; i = i + 2) {
                //获取下一个点
                tempPoint.x = data[i];
                tempPoint.y = data[i + 1];

                //判断当前点是否与已知直线在同一直线范围
                if (!isInSameLine(startPoint, tempPoint, tan, 0.05f)) {
                    //若不则将上一次的坐标点添加到路径中
                    pathPoints.add(new PointF(lastPoint.x, lastPoint.y));
                    //将上一次的坐标点作为起始点
                    startPoint.x = lastPoint.x;
                    startPoint.y = lastPoint.y;

                    //计算新的直线斜率
                    tan = (tempPoint.y - startPoint.y) / (tempPoint.x - startPoint.x);
                }
                //保存上一次的坐标点
                lastPoint.x = tempPoint.x;
                lastPoint.y = tempPoint.y;
            }
            //将最后一个点添加到路径中
            pathPoints.add(new PointF(tempPoint.x, tempPoint.y));

            return pathPoints;
        }
    }

    /**
     * 判断endPoint是否位于startPoint与上一次点构成的直线上
     *
     * @param startPoint 起始点
     * @param endPoint   结束点,即当前用于检验的点(其位置是未知的,可能在上一个点与起始点的直线上,也可能不是)
     * @param oldTan     旧的斜率,即起始点与上一次点构成直线的斜率
     * @param offset     允许误差的斜率范围
     * @return
     */
    public static boolean isInSameLine(PointF startPoint, PointF endPoint, float oldTan, float offset) {
        if (startPoint == null) {
            return false;
        } else {
            //计算起始点与当前点的斜率
            float newTan = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
            //斜率在误差范围内则返回在同一直线上,否则返回False
            if (Math.abs(newTan - oldTan) < offset) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 判断某个点是否在指定的区域内<br/>
     * <br/>
     * 计算原理:
     * 1.选定区域外的一点P(此方法中使用原点(0,0))<br/>
     * 2.将单击点与区域外的点P构成直线L<br/>
     * 3.判断直线L是否与区域构成的边有交叉点(所有的边都需要)<br/>
     * 4.判断得到的交叉点数量是否为偶数(必须是偶数此点才在区域内,原理不明...)<br/>
     * 5.取得到交叉点的前两个点,判断单击点是否在前两个点的区域内(此处只能是前两个点,当区域为凸边形时,大部分的点都是可以的,当区域为凹边形时,只有前两个点是必然有效,其它是不确定的)
     *
     * @param clickPoint 单击的点
     * @param points     区域路径
     * @return
     */
    public static boolean isPointInArea(PointF clickPoint, List<PointF> points) {
        //当前点或者路径不合法或不存在
        if (clickPoint == null || points == null || points.size() <= 0) {
            return false;
        } else {
            //创建用于存放交叉点的列表
            List<PointF> intersectPoints = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                if (i + 1 < points.size()) {
                    //判断单击点是否与给定的两个点(构成的线段)在同一线段内
                    //此处判断的即为直线与区域的边的交叉点
                    PointF p = isIntersectToLine(clickPoint, points.get(i), points.get(i + 1));
                    if (p != null) {
                        //若是则返回单击点,并添加到交叉点列表中
                        intersectPoints.add(p);
                    }
                } else {
                    //判断最后一个点与起始点构成的直线
                    PointF p = isIntersectToLine(clickPoint, points.get(i), points.get(0));
                    if (p != null) {
                        intersectPoints.add(p);
                    }
                }
            }

            //判断当前交叉点列表中数据量是否为偶数
            if (intersectPoints.size() > 0 && (intersectPoints.size() & 1) == 0) {
                //获取前两个交叉点
                PointF firstPoint = intersectPoints.get(0);
                PointF secondPoint = intersectPoints.get(1);

                //判断当前的单击点是否在前两个交叉点之间
                boolean checkInX = isPointInLineX(clickPoint, firstPoint, secondPoint);
                boolean checkInY = isPointInLineY(clickPoint, firstPoint, secondPoint);

                if (checkInX && checkInY) {
                    //是则返回true,此点在区域内
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }


    /**
     * 单击点是否与直线交叉
     *
     * @param clickPoint 单击点
     * @param startPoint 起始点
     * @param endPoint   结束点(用于构成直线)
     * @return 若交叉返回单击点, 否则返回null
     */
    public static PointF isIntersectToLine(PointF clickPoint, PointF startPoint, PointF endPoint) {
        if (clickPoint == null || startPoint == null || endPoint == null) {
            return null;
        } else {
            //计算原点与单击点的斜率
            float tanClick = clickPoint.y / clickPoint.x;
            //计算两点的斜率
            float tanLine = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
            //计算两点直线的截距
            float intercept = endPoint.y - tanLine * endPoint.x;

            //创建交叉点
            PointF intersectPoint = new PointF();
            intersectPoint.x = intercept / (tanClick - tanLine);
            intersectPoint.y = intersectPoint.x * tanClick;

            //判断单击点是否在直线的X范围内
            boolean checkInX = isPointInLineX(intersectPoint, startPoint, endPoint);
            //判断单击占是否在直线的Y范围内
            boolean checkInY = isPointInLineY(intersectPoint, startPoint, endPoint);

            if (checkInX && checkInY) {
                return intersectPoint;
            } else {
                return null;
            }
        }
    }

    /**
     * 判断单击点是否在直线的Y范围内
     *
     * @param intersectPoint 交叉点
     * @param startPoint     起始点
     * @param endPoint       结束点(用于构成直线)
     * @return
     */
    public static boolean isPointInLineY(PointF intersectPoint, PointF startPoint, PointF endPoint) {
        if (intersectPoint == null || startPoint == null || endPoint == null) {
            return false;
        } else {
            //计算最小值Y,最大值Y
            float smallYInLine = 0f;
            float largeYInLine = 0f;
            if (startPoint.y < endPoint.y) {
                smallYInLine = startPoint.y;
                largeYInLine = endPoint.y;
            } else {
                smallYInLine = endPoint.y;
                largeYInLine = startPoint.y;
            }

            //判断是否在范围内
            if (intersectPoint.y > smallYInLine && intersectPoint.y < largeYInLine) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断单击点是否在直线的X范围内
     *
     * @param intersectPoint 交叉点
     * @param startPoint     起始点
     * @param endPoint       结束点(用于构成直线)
     * @return
     */
    public static boolean isPointInLineX(PointF intersectPoint, PointF startPoint, PointF endPoint) {
        if (intersectPoint == null || startPoint == null || endPoint == null) {
            return false;
        } else {
            //计算最小值X,最大值X
            float smallXInLine = 0f;
            float largeXInLine = 0f;
            if (startPoint.x < endPoint.x) {
                smallXInLine = startPoint.x;
                largeXInLine = endPoint.x;
            } else {
                smallXInLine = endPoint.x;
                largeXInLine = startPoint.x;
            }

            if (intersectPoint.x > smallXInLine && intersectPoint.x < largeXInLine) {
                return true;
            } else {
                return false;
            }
        }
    }
}
