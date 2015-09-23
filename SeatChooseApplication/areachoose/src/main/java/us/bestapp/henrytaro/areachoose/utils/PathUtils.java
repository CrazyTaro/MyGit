package us.bestapp.henrytaro.areachoose.utils;/**
 * Created by xuhaolin on 15/9/22.
 */

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhaolin on 15/9/22.
 */
public class PathUtils {

    public static int[] corData = {
            380, 795, 381, 795, 382, 795, 383, 795, 384, 795, 385, 795, 386, 795, 387, 795, 388, 795,
            389, 795, 390, 795, 391, 795, 392, 795, 393, 795, 394, 795, 395, 795, 396, 795, 397, 795,
            398, 795, 399, 795, 400, 795, 401, 795, 402, 795, 403, 795, 404, 795, 405, 795, 406, 795,
            407, 795, 408, 795, 409, 794, 410, 794, 411, 794, 412, 794, 413, 794, 414, 794, 415, 794,
            416, 794, 417, 794, 418, 794, 419, 793, 420, 793, 421, 793, 422, 793, 423, 793, 424, 793,
            425, 793, 426, 793, 427, 793, 428, 793, 429, 793, 430, 793, 431, 793, 432, 793, 433, 793,
            434, 792, 435, 792, 436, 792, 437, 792, 438, 791, 439, 791, 440, 791, 441, 791, 442, 791,
            443, 791, 444, 791, 445, 790, 446, 790, 447, 790, 448, 790, 449, 790, 450, 790, 451, 790,
            452, 790, 453, 789, 454, 789, 455, 789, 456, 789, 457, 789, 458, 788, 459, 788, 460, 788,
            461, 788, 462, 787, 463, 787, 464, 787, 465, 787, 466, 787, 467, 786, 468, 786, 469, 786,
            470, 786, 471, 786, 472, 785, 473, 785, 474, 785, 475, 784, 476, 784, 477, 784, 478, 784,
            479, 784, 480, 783, 481, 783, 482, 783, 483, 783, 484, 782, 485, 782, 486, 781, 487, 781,
            488, 781, 489, 781, 490, 780, 491, 780, 492, 780, 493, 779, 494, 779, 495, 778, 496, 778,
            497, 777, 498, 777, 499, 777, 500, 776, 501, 776, 502, 776, 503, 776, 504, 775, 504, 774,
            504, 773, 503, 772, 503, 771, 502, 770, 502, 769, 501, 768, 501, 767, 500, 766, 500, 765,
            499, 764, 499, 763, 499, 762, 498, 761, 498, 760, 497, 759, 497, 758, 496, 757, 496, 756,
            495, 755, 495, 754, 494, 753, 494, 752, 493, 751, 493, 750, 492, 749, 492, 748, 492, 747,
            491, 746, 491, 745, 490, 744, 490, 743, 489, 742, 489, 741, 488, 740, 488, 739, 488, 738,
            487, 737, 487, 736, 487, 735, 486, 734, 486, 733, 485, 732, 485, 731, 484, 730, 484, 729,
            483, 728, 482, 727, 482, 726, 481, 725, 481, 724, 480, 723, 480, 722, 480, 721, 479, 720,
            479, 719, 479, 718, 478, 717, 478, 716, 477, 715, 476, 714, 475, 714, 474, 715, 473, 715,
            472, 716, 471, 716, 470, 716, 469, 717, 468, 717, 467, 718, 466, 718, 465, 719, 464, 719,
            463, 719, 462, 719, 461, 720, 460, 720, 459, 720, 458, 720, 457, 721, 456, 721, 455, 721,
            454, 721, 453, 722, 452, 722, 451, 722, 450, 723, 449, 723, 448, 723, 447, 723, 446, 724,
            445, 724, 444, 724, 443, 724, 442, 724, 441, 725, 440, 725, 439, 725, 438, 726, 437, 726,
            436, 726, 435, 726, 434, 726, 433, 727, 432, 727, 431, 727, 430, 727, 429, 727, 428, 727,
            427, 727, 426, 727, 425, 728, 424, 728, 423, 728, 422, 728, 421, 728, 420, 728, 419, 728,
            418, 729, 417, 729, 416, 729, 415, 729, 414, 729, 413, 729, 412, 729, 411, 729, 410, 729,
            409, 729, 408, 729, 407, 729, 406, 729, 405, 729, 404, 729, 403, 729, 402, 729, 401, 730,
            400, 730, 399, 730, 398, 730, 397, 730, 396, 730, 395, 730, 394, 730, 393, 730, 392, 729,
            391, 729, 390, 729, 389, 729, 388, 729, 387, 729, 386, 729, 385, 729, 384, 729, 383, 729,
            382, 729, 381, 729, 380, 729, 379, 729, 378, 729, 377, 729, 376, 729, 375, 729, 374, 728,
            373, 728, 372, 728, 371, 728, 370, 728, 369, 728, 368, 728, 367, 727, 366, 727, 365, 727,
            364, 727, 363, 727, 362, 727, 361, 727, 360, 726, 359, 726, 358, 726, 357, 726, 356, 726,
            355, 726, 354, 726, 353, 725, 352, 725, 351, 725, 350, 725, 349, 725, 348, 724, 347, 724,
            346, 724, 345, 723, 344, 723, 343, 723, 342, 723, 341, 722, 340, 722, 339, 722, 338, 722,
            337, 722, 336, 721, 335, 721, 334, 721, 333, 721, 332, 720, 331, 720, 330, 719, 329, 719,
            328, 719, 327, 718, 326, 718, 325, 718, 324, 718, 323, 717, 322, 717, 321, 717, 320, 716,
            319, 716, 318, 715, 317, 715, 316, 716, 316, 717, 315, 718, 315, 719, 314, 720, 314, 721,
            313, 722, 313, 723, 312, 724, 312, 725, 311, 726, 311, 727, 310, 728, 310, 729, 310, 730,
            309, 731, 309, 732, 308, 733, 308, 734, 307, 735, 307, 736, 306, 737, 306, 738, 305, 739,
            305, 740, 304, 741, 304, 742, 303, 743, 303, 744, 302, 745, 302, 746, 301, 747, 301, 748,
            300, 749, 300, 750, 300, 751, 299, 752, 299, 753, 298, 754, 298, 755, 297, 756, 297, 757,
            296, 758, 295, 759, 295, 760, 294, 761, 294, 762, 294, 763, 293, 764, 293, 765, 292, 766,
            292, 767, 291, 768, 291, 769, 290, 770, 290, 771, 290, 772, 289, 773, 289, 774, 288, 775,
            287, 776, 288, 776, 289, 777, 290, 777, 291, 777, 292, 778, 293, 778, 294, 778, 295, 779,
            296, 779, 297, 779, 298, 780, 299, 780, 300, 780, 301, 781, 302, 781, 303, 782, 304, 782,
            305, 782, 306, 782, 307, 783, 308, 783, 309, 783, 310, 783, 311, 784, 312, 784, 313, 785,
            314, 785, 315, 785, 316, 785, 317, 786, 318, 786, 319, 786, 320, 786, 321, 786, 322, 787,
            323, 787, 324, 787, 325, 788, 326, 788, 327, 788, 328, 788, 329, 788, 330, 789, 331, 789,
            332, 789, 333, 789, 334, 789, 335, 789, 336, 789, 337, 790, 338, 790, 339, 790, 340, 790,
            341, 790, 342, 790, 343, 791, 344, 791, 345, 791, 346, 791, 347, 792, 348, 792, 349, 792,
            350, 792, 351, 792, 352, 792, 353, 793, 354, 793, 355, 793, 356, 793, 357, 793, 358, 793,
            359, 793, 360, 793, 361, 793, 362, 794, 363, 794, 364, 794, 365, 794, 366, 794, 367, 794,
            368, 794, 369, 794, 370, 794, 371, 794, 372, 794, 373, 794, 374, 794, 375, 794, 376, 794,
            377, 794, 378, 794, 379, 794};


    /**
     * 根据给定的坐标点,创建由坐标点构建的路径
     *
     * @param points
     * @return
     */
    public Path getPath(List<PointF> points) {
        if (points == null || points.size() <= 0) {
            return null;
        }

        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            PointF p = points.get(i);
            path.lineTo(p.x, p.y);
        }
        path.close();
        return path;
    }

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
