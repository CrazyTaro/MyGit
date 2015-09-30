package com.henrytaro.xuhaolin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import us.bestapp.henrytaro.seatchoose.draw.interfaces.ISeatDrawInterface;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.seatchoose.entity.example.EgSeatMap;
import us.bestapp.henrytaro.seatchoose.entity.film.FilmSeat;
import us.bestapp.henrytaro.seatchoose.entity.film.FilmSeatMap;
import us.bestapp.henrytaro.seatchoose.entity.show.ShowSeatMap;
import us.bestapp.henrytaro.seatchoose.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.IStageParams;
import us.bestapp.henrytaro.seatchoose.utils.SeatCheckRuleUtils;
import us.bestapp.henrytaro.seatchoose.view.SeatChooseView;
import us.bestapp.henrytaro.seatchoose.view.interfaces.ISeatChooseEvent;


public class MainActivity extends Activity implements ISeatChooseEvent {
    SeatChooseView mChooseview = null;
    Button mBtnSubmit = null;
    ISeatDrawInterface mSeatDataHandle = null;
    AbsMapEntity mMap = null;
    private int[][] mSeatMap = {
//            {4, 5, 1, 1, 1, 3, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3,},//1
//            {1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 3, 1, 1, 3,},//3
//            {1, 3, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1,},//3
//            {3, 1, 1, 1, 1, 3, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3,},//4
//            {1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1,},//5
//            {1, 1, 4, 5, 1, 3, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3,},//6
//            {1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 3, 3, 3, 1, 1, 1, 3, 1, 1, 3,},//1
//            {1, 3, 1, 1, 1, 1, 4, 5, 3, 1, 1, 4, 5, 1, 1, 1, 3, 1, 1, 1,},//8
//            {3, 1, 1, 1, 1, 3, 1, 1, 3, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3,},//3
//            {1, 1, 1, 1, 1, 3, 1, 1, 1, 4, 5, 3, 1, 3, 4, 5, 3, 1, 1, 1,},//10

            //1,04,05,06,01,08,03,10,11,12,13,14,15,16,11,18,13,20,21,22,23,24,25,26,21,28,23,30,31,32,33,34,35,36
            {4, 5, 1, 1, 1, 1, 1, 4, 5, 0, 0, 1, 1, 1, 4, 5, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 4, 5,},//1
            {1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1,},//3
            {1, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 0, 0, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//3
            {3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//4
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 5,},//5
            {1, 1, 3, 1, 4, 5, 1, 1, 1, 0, 0, 4, 5, 1, 1, 1, 3, 1, 4, 5, 1, 1, 1, 3, 1, 4, 5, 1, 1, 1, 3, 1, 4, 5, 1, 1, 1, 3, 1, 4, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//6
            {1, 1, 1, 3, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//1
            {1, 3, 3, 1, 1, 1, 1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 0, 0, 1, 4, 5, 3, 3, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//8
            {3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1,},//3
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 3, 4, 5, 1, 1, 1, 3, 1, 1, 1, 3, 4, 5, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//11
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//11
            {1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 1, 1, 1, 3, 1, 1,},//12
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 1, 1,},//13
            {1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1,},//14
            {1, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 0, 0, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//15
            {3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//16
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//11
            {1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//18
//            {1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 4, 5, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//13
//            {1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//21
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3,},//21
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//22
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 5, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//23
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//24
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//25
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 4, 5, 3, 1, 3, 1, 1, 1, 1, 1,},//26
//            {1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//21
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 4, 5, 3, 1, 1, 3, 1, 1, 1, 3,},//28
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//23
//            {1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//31
//            {1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//31
//            {1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//32
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3,},//33
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//34
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 4, 5, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//35
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//36
    };

    String filmJsonStr = "{\n" +
            "    \"success\": true, \n" +
            "    \"error_code\": \"0\", \n" +
            "    \"message\": \"请求成功\", \n" +
            "    \"data\": {\n" +
            "        \"row\": [\n" +
            "            {\n" +
            "                \"rownum\": \"1\", \n" +
            "                \"rowid\": \"1\", \n" +
            "                \"columns\": \"ZL,01@A@1,02@A@2,03@A@0,04@A@1,05@A@2,06@A@0,07@A@0,08@A@0,09@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"2\", \n" +
            "                \"rowid\": \"2\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@1,06@A@2,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"3\", \n" +
            "                \"rowid\": \"3\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"4\", \n" +
            "                \"rowid\": \"4\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@1,06@A@2,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"5\", \n" +
            "                \"rowid\": \"5\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@1,11@A@2,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"6\", \n" +
            "                \"rowid\": \"6\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@LK@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"7\", \n" +
            "                \"rowid\": \"7\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"8\", \n" +
            "                \"rowid\": \"8\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@LK@0,03@LK@0,04@A@0,05@A@0,06@LK@0,07@A@0,08@A@0,09@A@1,10@A@2\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"9\", \n" +
            "                \"rowid\": \"9\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"10\", \n" +
            "                \"rowid\": \"10\", \n" +
            "                \"columns\": \"01@A@0,02@A@0,ZL,03@A@0,04@A@0,05@A@0,06@A@0,07@LK@0,08@LK@0,09@LK@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"11\", \n" +
            "                \"rowid\": \"11\", \n" +
            "                \"columns\": \"01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0,13@A@0\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }, \n" +
            "    \"source\": \"gewala\"\n" +
            "}";


    String showJsonStr = "{\n" +
            "  'success' : true,\n" +
            "  'error_code': 0,\n" +
            "  'message':\"请求成功\",\n" +
            "  'data':[\n" +
            "   {\"row\":1,\n" +
            "    \"column\":1,\n" +
            "    \"name\":\"1排10座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"avaliable\"},\n" +
            "   {\"row\":1,\n" +
            "    \"column\":2,\n" +
            "    \"name\":\"1排9座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"locked\"},\n" +
            "   {\"row\":1,\n" +
            "    \"column\":3,\n" +
            "    \"name\":\"1排8座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"avaliable\"},\n" +
            "   {\"row\":2,\n" +
            "    \"column\":1,\n" +
            "    \"name\":\"1排10座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"locked\"},\n" +
            "   {\"row\":2,\n" +
            "    \"column\":2,\n" +
            "    \"name\":\"1排9座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"unused\"},\n" +
            "   {\"row\":2,\n" +
            "    \"column\":3,\n" +
            "    \"name\":\"1排8座\",\n" +
            "    \"price\":1.0,\n" +
            "    \"status\":\"avaliable\"}\n" +
            "   ]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseview = (SeatChooseView) findViewById(R.id.view_choose);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);


        FilmSeatMap dataMap = FilmSeatMap.objectFromJsonStr(filmJsonStr);
        ShowSeatMap showMap = ShowSeatMap.objectFromJsonStr(showJsonStr);
        EgSeatMap egMap = new EgSeatMap(mSeatMap);
        mChooseview.setISeatChooseEvent(this);
        mSeatDataHandle = mChooseview.getSeatDrawInterface();
        mSeatDataHandle.setIsShowLog(true, null);


        IGlobleParams globleParams = mSeatDataHandle.getGlobleParams();
        globleParams.setIsDrawThumbnail(true);
        globleParams.setIsDrawSeletedRowColumnNotification(true, globleParams.createNotificationFormat(true, "第", IGlobleParams.FORMAT_STR, "行/第", IGlobleParams.FORMAT_STR, "列"));
        globleParams.setIsDrawRowNumber(true);
        globleParams.setSeatTypeRowCount(2);
        globleParams.setIsShowThumbnailAlways(true);
        globleParams.setIsEnabledQuickShowByClickOnThumbnail(true);
        globleParams.setIsEnabledQuickMoveOnThumbnail(true);
//        globleParams.setIsAutoScaleToScreen(false);
//        globleParams.setIsDrawColumnNumber(true);

        ISeatParams seatParams = mSeatDataHandle.getSeatParams();
        seatParams.setIsDrawStyle(true);
        seatParams.setWidth(40f);
        seatParams.setHeight(40f);

        IStageParams stageParams = mSeatDataHandle.getStageParams();
        stageParams.setStageDescription("深圳橙天嘉禾影城万象城店 6号厅");

//        setImage(seatParams);
        mMap = dataMap;
//        mMap=showMap;
//        mMap=egMap;
        mSeatDataHandle.setSeatDrawMap(mMap);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              List<AbsSeatEntity> selectSeats = mChooseview.getSeletedSeats();
                                              String[] edgeTags = new String[]{FilmSeat.TAG_UNSHOW_SEAT, FilmSeat.TAG_ERROR_SEAT, FilmSeat.TAG_LOCK_SEAT};
                                              String[] enabledTags = new String[]{FilmSeat.TAG_OPTIONAL_SEAT, FilmSeat.TAG_COUPLE_OPTIONAL_SEAT};
                                              if (SeatCheckRuleUtils.isIllegalSeatList(selectSeats, mMap, edgeTags, enabledTags)) {
                                                  Toast.makeText(MainActivity.this, "不能留下单个座位!", Toast.LENGTH_SHORT).show();
                                              } else {
                                                  Toast.makeText(MainActivity.this, "所有座位有效~~~", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }

        );
    }

    private void setImage(ISeatParams params) {
        params.getDrawStyle(BaseSeatParams.TAG_LOCK_SEAT).imageID = R.drawable.zuowei_disabled;
        params.getDrawStyle(BaseSeatParams.TAG_COUPLE_OPTIONAL_SEAT).imageID = R.drawable.zuowei_lovers;
        params.getDrawStyle(BaseSeatParams.TAG_OPTIONAL_SEAT).imageID = R.drawable.zuowei_optional;
        params.getDrawStyle(BaseSeatParams.TAG_SELECTE_SEAT).imageID = R.drawable.zuowei_selected;

        params.setDrawType(ISeatParams.DRAW_TYPE_IMAGE);
    }


    @Override
    public void clickSeatSuccess(AbsSeatEntity seatEntity) {
    }

    @Override
    public void seatStatusChanged(AbsSeatEntity[] seatEntities, boolean isChosen) {
        if (isChosen && seatEntities != null) {

        }
    }

    @Override
    public void seletedFail(boolean isCoupleSeat) {
        if (isCoupleSeat) {
            Toast.makeText(this, "情侣选座失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "选座失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void seletedFull(boolean isCoupleSeat) {
        if (isCoupleSeat) {
            Toast.makeText(this, "情侣座满", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "普通座满", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void scaleMaximum() {

    }
}
