package us.bestapp.henrytaro.utils;

import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taro on 16/3/24.
 * 检测座位是否合法
 */
public class SeatCheckRuleUtils {


//    /**
//     * 检测选中座位是否为合法位置(座位之间不留下单座).
//     * 座位检测原理如下:查找当前选中座位列表中每一组座位(每一组座位的意义是相邻连在一起被选中的座位)的边界座位,如假设当前座位中存在一组5连座的座位,则该5连座座位的边界两个座位即为边界座位;
//     * (当座位只有1个或者2个时,本身即为边界座位).获取边界座位之后对边界座位进行确定判断,是否与列表边界(如锁定座位,未显示座位,不存在的座位等)相邻,若是此组座位必然有效;否则对该组座位的边界座位进行判断,
//     * 若左右边界均存在2个座位以上的可用有效座位(一般为未选中座位)则该组座位同样有效,否则座位无效
//     *
//     * @param selectedSeatList 选中的座位
//     * @param map              座位数据列表
//     * @param edgeTags         边界座位的标签,这里的边界是由用户指定的,也就是可以把锁定座位及未显示座位的标签作为边界传入,用于检测当前选中座位是否在边界
//     * @param enabledTags      有效可用的标签(一般指未选中的标签,可根据需求增减)
//     * @return 若座位合法, 返回true, 否则返回false;
//     */
//    public static boolean isValidSeatList(List<AbsSeatEntity> selectedSeatList, AbsMapEntity map, String[] edgeTags, String[] enabledTags) {
//        //获取边界座位
//        List<AbsSeatEntity> edgeSeatList = SeatCheckRuleUtils.getEdgeSeatList(selectedSeatList, map);
//        if (edgeSeatList != null) {
//            //检测边界座位
//            for (AbsSeatEntity edgeSeat : edgeSeatList) {
//                //检测边界座位是否在合法边界
//                if (!SeatCheckRuleUtils.isAtMapEdgeOrNearLocks(selectedSeatList, edgeSeat, map, edgeTags)) {
//                    //若不是则检测边界座位是否左右保留两个可用有效座位
//                    if (!SeatCheckRuleUtils.isNearSeatDoubleEnabled(selectedSeatList, edgeSeat, map, enabledTags)) {
//                        return false;
//                    }
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 判断选中座位是否为非法位置(座位之间不留下单座).
     * 座位检测原理如下:查找当前选中座位列表中每一组座位(每一组座位的意义是相邻连在一起被选中的座位)的边界座位,如假设当前座位中存在一组5连座的座位,则该5连座座位的边界两个座位即为边界座位;
     * (当座位只有1个或者2个时,本身即为边界座位).获取边界座位之后对边界座位进行确定判断,是否与列表边界(如锁定座位,未显示座位,不存在的座位等)相邻,若是此组座位必然有效;否则对该组座位的边界座位进行判断,
     * 若左右边界均存在2个座位以上的可用有效座位(一般为未选中座位)则该组座位同样有效,否则座位无效
     *
     * @param selectedSeatList 选中的座位
     * @param map              座位数据列表
     * @param edgeTags         边界座位的标签,这里的边界是由用户指定的,也就是可以把锁定座位及未显示座位的标签作为边界传入,用于检测当前选中座位是否在边界
     * @param enabledTags      有效可用的标签(一般指未选中的标签,可根据需求增减)
     * @return 若座位合法, 返回true, 否则返回false;
     */
    public static boolean isIllegalSeatList(List<AbsSeatEntity> selectedSeatList, AbsMapEntity map, String[] edgeTags, String[] enabledTags) {
        //获取将所有选中座位分组后的边界座位列表
        List<AbsSeatEntity[]> edgeSeatList = SeatCheckRuleUtils.getEdgeSeatArrList(selectedSeatList, map);
        if (edgeSeatList != null) {
            //用于记录所有组是否合法的变量
            boolean isAllGroupEnabled = true;
            //每个组逐一检查
            for (AbsSeatEntity[] edgeSeatArr : edgeSeatList) {
                //当前组是否有效
                boolean isGroupEnabled = false;
                for (AbsSeatEntity edgeSeat : edgeSeatArr) {
                    //检查当前组内每个边界座位是否在边界
                    if (SeatCheckRuleUtils.isAtMapEdgeOrNearLocks(selectedSeatList, edgeSeat, map, edgeTags)) {
                        //是直接不考虑其它边界座位(实际上一个组的边界座位必定最多只有2个)
                        isGroupEnabled = true;
                        break;
                    }
                }
                //若组内边界座位都不在边界
                if (!isGroupEnabled) {
                    //先重置当前组的座位有效性
                    isGroupEnabled = true;
                    for (AbsSeatEntity edgeSeat : edgeSeatArr) {
                        //判断边界座位两边的座位是否都是有效可用的2个座位
                        if (!SeatCheckRuleUtils.isNearSeatDoubleEnabled(selectedSeatList, edgeSeat, map, enabledTags)) {
                            //否则该组座位不可用
                            //若该组所有边界座位都合法,则此值为true,即有效可用
                            isGroupEnabled = false;
                            break;
                        }
                    }
                }
                //对所有的组进行整合(任何一个组的座位无效都会导致当前选中座位无效)
                isAllGroupEnabled = isAllGroupEnabled && isGroupEnabled;
                if (!isAllGroupEnabled) {
                    //任何一个组出现无效,直接返回true(不合法)
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 获取边界座位
     *
     * @param selectedSeatList 选中座位
     * @param map              座位数据列表
     * @return
     * @deprecated
     */
    public static List<AbsSeatEntity> getEdgeSeatList(List<AbsSeatEntity> selectedSeatList, AbsMapEntity map) {
        if (selectedSeatList == null || map == null) {
            return null;
        } else {
            List<AbsSeatEntity> edgeSeatList = new ArrayList<>();
            for (AbsSeatEntity selected : selectedSeatList) {
                //当前选中座位存在边界列表中,则直接检测下一个座位
                if (selected == null || edgeSeatList.contains(selected)) {
                    continue;
                }
                //获取当前座位的最左边界座位,递归
                AbsSeatEntity leftSeat = getLeftEdgeSeat(selectedSeatList, selected, map);
                //获取当前座位的最右边界座位,递归
                AbsSeatEntity rightSeat = getRightEdgeSeat(selectedSeatList, selected, map);
                //若得到的最左边界与最右边界座位是同一座位
                //即该组座位为1个座位的情况
                //将该座位添加到边界列表中,只添加一次
                if (leftSeat.equals(rightSeat)) {
                    edgeSeatList.add(leftSeat);
                } else {
                    //否则将两个边界座位添加到列表中
                    //该组座位有2个以上的座位
                    edgeSeatList.add(leftSeat);
                    edgeSeatList.add(rightSeat);
                }
            }
            //边界座位存在时返回,否则返回null
            if (edgeSeatList.size() > 0) {
                return edgeSeatList;
            } else {
                return null;
            }
        }
    }

    /**
     * 暂时废弃
     *
     * @param selectedSeatList
     * @param map
     * @return
     */
    public static List<AbsSeatEntity[]> getEdgeSeatArrList(List<AbsSeatEntity> selectedSeatList, AbsMapEntity map) {
        if (selectedSeatList == null || map == null) {
            return null;
        } else {
            List<AbsSeatEntity> edgeSeatList = new ArrayList<>();
            List<AbsSeatEntity[]> edgeSeatArrList = new ArrayList<>();
            for (AbsSeatEntity selected : selectedSeatList) {
                if (selected == null || edgeSeatList.contains(selected)) {
                    continue;
                }
                AbsSeatEntity leftSeat = getLeftEdgeSeat(selectedSeatList, selected, map);
                AbsSeatEntity rightSeat = getRightEdgeSeat(selectedSeatList, selected, map);
                if (!edgeSeatList.contains(leftSeat) && !edgeSeatList.contains(rightSeat)) {
                    if (leftSeat.equals(rightSeat)) {
                        edgeSeatList.add(leftSeat);
                        edgeSeatArrList.add(new AbsSeatEntity[]{leftSeat});
                    } else {
                        edgeSeatList.add(leftSeat);
                        edgeSeatList.add(rightSeat);
                        edgeSeatArrList.add(new AbsSeatEntity[]{leftSeat, rightSeat});
                    }
                }
            }
            if (edgeSeatArrList.size() > 0) {
                return edgeSeatArrList;
            } else {
                return null;
            }
        }
    }

    /**
     * 获取给定座位所在该组座位的最左边界座位
     *
     * @param selectedSeatList 选中座位
     * @param seat             当前该组被选中座位中某个座位
     * @param map              座位数据列表
     * @return
     */
    public static AbsSeatEntity getLeftEdgeSeat(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity seat, AbsMapEntity map) {
        if (seat == null || map == null || selectedSeatList == null) {
            return null;
        } else {
            //获取当前座位左边的座位
            AbsSeatEntity leftSeat = map.getSeatEntity(seat.getX(), seat.getY() - 1);
            //判断左边座位是否存在选中座位中
            if (selectedSeatList.contains(leftSeat)) {
                //若是则进行递归,直到在左边的座位不在选中座位中
                //此时该座位即为左边界的座位
                return getLeftEdgeSeat(selectedSeatList, leftSeat, map);
            } else {
                //若左边座位不在选中座位中
                //当前座位即为左边界座位(此处的边界座位是指当前座位所在的被选中座位中的该组座位,同一行,相邻的N列被选中座位)
                return seat;
            }
        }
    }

    /**
     * 获取给定座位所在该组座位的最右边界座位
     *
     * @param seatListseatList 选中座位
     * @param seat             当前该组被选中座位中某个座位
     * @param map              座位数据列表
     * @return
     */
    public static AbsSeatEntity getRightEdgeSeat(List<AbsSeatEntity> seatListseatList, AbsSeatEntity seat, AbsMapEntity map) {
        if (seat == null || map == null || seatListseatList == null) {
            return null;
        } else {
            //获取当前座位的右边座位
            AbsSeatEntity rightSeat = map.getSeatEntity(seat.getX(), seat.getY() + 1);
            //检测右边座位是否存在于选中座位中
            if (seatListseatList.contains(rightSeat)) {
                //若是递归
                return getRightEdgeSeat(seatListseatList, rightSeat, map);
            } else {
                //若不是,返回当前座位(注意是返回当前的座位而不是右边的座位)
                return seat;
            }
        }
    }

    /**
     * 判断边界座位是否在数据列表的边界
     *
     * @param selectedSeatList 选中数据
     * @param edgeSeat         边界座位
     * @param map              座位数据列表
     * @return
     */
    public static boolean isAtMapEdge(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity edgeSeat, AbsMapEntity map) {
        //最后一个参数为用于充当边界的座位类型标签
        //此处置为null,即只检测边界座位是否位于数据列表map的边界(数据之外)
        return isAtMapEdgeOrNearLocks(selectedSeatList, edgeSeat, map, null);
    }

    /**
     * 判断边界座位是否在座位数据列表及指定座位类型的边界(指定座位类型用于充当边界,类似于map的边界存在)
     *
     * @param selectedSeatList 选中座位
     * @param edgeSeat         边界座位
     * @param map              座位数据列表
     * @param edgeTag          边界座位类型标签
     * @return
     */
    public static boolean isAtMapEdgeOrNearLock(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity edgeSeat, AbsMapEntity map, String edgeTag) {
        return isAtMapEdgeOrNearLocks(selectedSeatList, edgeSeat, map, new String[]{edgeTag});
    }

    /**
     * 判断边界座位是否在座位数据列表及指定座位类型的边界(指定座位类型用于充当边界,类似于map的边界存在)
     *
     * @param selectedSeatList 选中座位
     * @param edgeSeat         边界座位
     * @param map              座位数据列表
     * @param edgeTags         边界座位类型标签组
     * @return
     */
    public static boolean isAtMapEdgeOrNearLocks(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity edgeSeat, AbsMapEntity map, String[] edgeTags) {
        if (edgeSeat == null || map == null || selectedSeatList == null) {
            throw new RuntimeException("参数不可为null");
        }
        //获取当前边界座位的位置
        int rowIndex = edgeSeat.getX();
        int columnIndex = edgeSeat.getY();
        //获取左边的座位
        AbsSeatEntity leftSeat = map.getSeatEntity(rowIndex, columnIndex - 1);
        //获取右边的座位
        AbsSeatEntity rightSeat = map.getSeatEntity(rowIndex, columnIndex + 1);


        //左边座位或者右边座位只有有一个是数据列表map的边界,即返回true
        if (leftSeat == null || !leftSeat.isExsit() || rightSeat == null || !rightSeat.isExsit()) {
            return true;
        } else {
            //当前座位即不是左边界也不是右边界
            if (edgeTags == null) {
                //当前不存在其它座位类型作为边界,返回false(此时座位已经不在左边界或右边界)
                return false;
            } else {
                //否则进行检测
                //获取左右两边座位的座位类型
                String leftTag = leftSeat.getDrawStyleTag();
                String rightTag = rightSeat.getDrawStyleTag();
                //将座位类型与给定的边界座位类型比较
                //若为某个指定的充当边界的座位类型则返回true(此时已与边界相邻)
                for (String lock : edgeTags) {
                    if (leftTag.equals(lock) || rightTag.equals(lock)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /**
     * 获取边界座位不相邻选中座位的另一边的座位(即相邻未被选中的座位)
     *
     * @param selectedSeatList 选中座位
     * @param edgeSeat         边界座位
     * @param map              座位数据列表
     * @return
     */
    public static int getNotEdgeSide(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity edgeSeat, AbsMapEntity map) {
        if (selectedSeatList == null || edgeSeat == null || map == null) {
            throw new RuntimeException("参数不可为null");
        }
        //获取当前座位的左边座位
        AbsSeatEntity leftSeat = map.getSeatEntity(edgeSeat.getX(), edgeSeat.getY() - 1);
        //获取当前座位的右边座位
        AbsSeatEntity rightSeat = map.getSeatEntity(edgeSeat.getX(), edgeSeat.getY() + 1);
        //判断左边座位是否存在选中座位中
        if (selectedSeatList.contains(leftSeat)) {
            //返回大于0的数
            //表示右边为非边界座位方向
            return 1;
            //判断右边座位是否存在选中座位中
        } else if (selectedSeatList.contains(rightSeat)) {
            //返回小于0的数
            //表示左边为非边界座位方向
            return -1;
        } else {
            //否则返回0,即左右两边都不存在选中座位(单个座位的情况)
            return 0;
        }
    }

    /**
     * 检测当前座位是否邻近两个可用座位(左边或者右边)
     *
     * @param selectedSeatList 选中座位
     * @param seat             当前座位
     * @param map              座位数据列表
     * @param enbaledTags      可用有效座位的类型标签
     * @return
     */
    public static boolean isNearSeatDoubleEnabled(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity seat, AbsMapEntity map, String[] enbaledTags) {
        if (selectedSeatList == null || seat == null || map == null || enbaledTags == null) {
            throw new RuntimeException("参数不可为null");
        }
        //获取不邻近边界的方向
        int notEdgeSide = getNotEdgeSide(selectedSeatList, seat, map);
        boolean result = true;
        if (notEdgeSide < 0) {
            //检测左边的座位是否为两个有效座位
            result = isNearSeatDoubleEnabled(seat, map, true, 2, enbaledTags);
        } else if (notEdgeSide > 0) {
            //检测右边的座位是否为两个有效座位
            result = isNearSeatDoubleEnabled(seat, map, false, 2, enbaledTags);
        } else {
            //左右两边都非边界,检测两边的座位是否同时为两个有效座位
            result = isNearSeatDoubleEnabled(seat, map, true, 2, enbaledTags) && isNearSeatDoubleEnabled(seat, map, false, 2, enbaledTags);
        }
        return result;
    }

    /**
     * 检测当前座位是否邻近两个有效座位(左边或者右边)
     *
     * @param selectedSeatList 选中座位
     * @param seat             当前座位
     * @param map              座位数据列表
     * @param enableTag        有效座位类型标签
     * @return
     */
    public static boolean isNearSeatDoubleEnabled(List<AbsSeatEntity> selectedSeatList, AbsSeatEntity seat, AbsMapEntity map, String enableTag) {
        return isNearSeatDoubleEnabled(selectedSeatList, seat, map, new String[]{enableTag});
    }

    /**
     * 检测当前座位的某个方向是否为有效座位(通过参数确定检测左边还是右边)
     *
     * @param seat            当前座位
     * @param map             座位数据列表
     * @param isCheckLeft     是否检测左边,true检测左边,false检测右边
     * @param enableSeatCount 需要保留的有效的座位数(默认两位)
     * @param enableTags      有效可用座位的类型标签  @return
     */
    public static boolean isNearSeatDoubleEnabled(AbsSeatEntity seat, AbsMapEntity map, boolean isCheckLeft, int enableSeatCount, String[] enableTags) {
        if (seat == null || map == null || enableTags == null) {
            throw new RuntimeException("参数不可为null");
        }
        //判断要求检测的有效座位数是否在合法范围,不可小于等于0,也不可大于座位表中最大行的座位数
        if (enableSeatCount <= 0 || enableSeatCount > map.getMaxColumnCount()) {
            throw new RuntimeException("有效座位数不可小于0, 也不可大于座位表中最大行的座位数");
        }
        //座位数合法,创建与有效座位数相同的暂存座位数组
        AbsSeatEntity[] checkSeatArr = new AbsSeatEntity[enableSeatCount];
        //获取当前座位的X/Y位置
        int rowIndex = seat.getX();
        int columnIndex = seat.getY();
        //增量,用于向左检测或者向右检测
        int increaseValue = 1;
        int tempColumnIndex = 0;
        //邻近座位是否可用标识
        boolean isNearSeatEnabled = true;
        //向左遍历检测,增量为负数
        if (isCheckLeft) {
            increaseValue = -1;
        } else {
            //向右遍历检测,增量为正数
            increaseValue = 1;
        }
        //获取需要检测的邻近座位
        for (int i = 0; i < enableSeatCount; i++) {
            tempColumnIndex = columnIndex + i * increaseValue;
            checkSeatArr[i] = map.getSeatEntity(rowIndex, tempColumnIndex);
            if (checkSeatArr[i] == null) {
                //若获取到的座位是空的,说明该座位不存在,直接返回不可用(没有达到有效座位数的要求)
                isNearSeatEnabled = false;
            }
        }
        if (!isNearSeatEnabled) {
            return false;
        } else {
            //若所有邻近座位存在,检测其是否为有效座位
            //遍历检测的每个座位标识(每次遍历时对应座位的有效标识)
            boolean isCheckSeatEnable = false;
            for (int i = 0; i < checkSeatArr.length; i++) {
                AbsSeatEntity checkSeat = checkSeatArr[i];
                //遍历所有的有效标签
                for (String enable : enableTags) {
                    //检测当前邻近座位的标签是否有效
                    if (checkSeat.getDrawStyleTag().equals(enable)) {
                        //标签有效此座位有效, 则不需要遍历完所有的标签,直接检测下一个座位
                        isCheckSeatEnable = true;
                        break;
                    }
                }
                //每遍历一个座位检测该座位是否有效,若该座位有效,检测下一座位
                if (isCheckSeatEnable) {
                    continue;
                } else {
                    //该座位无效,此不管其它座位有没有效,都直接返回邻近座位无效
                    isNearSeatEnabled = false;
                    break;
                }
            }
            //返回结果
            return isNearSeatEnabled;
        }


//        //第一个座位
//        AbsSeatEntity firstSeat = null;
//        //第二个座位
//        AbsSeatEntity secondSeat = null;
//        //获取当前座位的位置
//        int rowIndex = seat.getX();
//        int columnIndex = seat.getY();
//        int firstCoumnIndex = 0;
//        int secondColumnIndex = 0;
//        //判断向左边检测还是右边检测
//        if (isCheckLeft) {
//            firstCoumnIndex = columnIndex - 1;
//            secondColumnIndex = columnIndex - 2;
//        } else {
//            firstCoumnIndex = columnIndex + 1;
//            secondColumnIndex = columnIndex + 2;
//        }
//        //获取检测方向的邻近两个座位
//        firstSeat = map.getSeatEntity(rowIndex, firstCoumnIndex);
//        secondSeat = map.getSeatEntity(rowIndex, secondColumnIndex);
//        //两个座位任何一个不存在都说明不为有效座位
//        if (firstSeat == null || secondSeat == null) {
//            return false;
//        } else {
//            //判断获取的两个座位是否有效座位
//            boolean isFirstEnable = false;
//            boolean isSecondEnable = false;
//            for (String enable : enableTags) {
//                //判断第一个座位
//                if (firstSeat.getDrawStyleTag().equals(enable)) {
//                    isFirstEnable = true;
//                }
//                //判断第二个座位
//                if (secondSeat.getDrawStyleTag().equals(enable)) {
//                    isSecondEnable = true;
//                }
//                //当两个座位同时都有为效时,才返回true
//                if (isFirstEnable && isSecondEnable) {
//                    return true;
//                }
//            }
//
//            return false;
//        }
    }
}
