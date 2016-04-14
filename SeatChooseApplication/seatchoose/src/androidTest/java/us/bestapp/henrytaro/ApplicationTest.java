package us.bestapp.henrytaro;

import android.app.Application;
import android.graphics.Color;
import android.test.ApplicationTestCase;
import android.util.Log;

import us.bestapp.henrytaro.params.GlobalParams;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.baseparams.BaseDrawStyle;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void show() {
        SeatParams params = new SeatParams();
        Log.i("show",params.toString());
        params.seDrawWidth(100);
        params.seDrawHeight(30);
        params.clearDrawStyles();
        params.addNewDrawStyle("test", new BaseDrawStyle("test", true, Color.RED, Color.BLACK, Color.BLUE, "desc", 0, null));
        SeatParams newParams=(SeatParams) params.clone();
        Log.i("show",newParams.toString());
    }

    public void test() {
        String format = new GlobalParams().createNotificationFormat(true, "第", IGlobleParams.FORMAT_STR, "行/第", IGlobleParams.FORMAT_STR, "列");
        String str = String.format(format, 1, 2);
        Log.i("format", str);
    }

    public void testParseJsonStr() {
//            String str =
//                    "{\"rownum\": \"1\", \n\"rowid\": \"1\", \n\"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n}";
//            Gson gson = new Gson();
//            FilmRow row = gson.fromJson(str, FilmRow.class);
//            String rowStr = row.toString();

        String jsonStr = "{\n" +
                "    \"success\": true, \n" +
                "    \"error_code\": \"0\", \n" +
                "    \"message\": \"请求成功\", \n" +
                "    \"data\": {\n" +
                "        \"row\": [\n" +
                "            {\n" +
                "                \"rownum\": \"1\", \n" +
                "                \"rowid\": \"1\", \n" +
                "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
                "            }, \n" +
                "            {\n" +
                "                \"rownum\": \"2\", \n" +
                "                \"rowid\": \"2\", \n" +
                "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
                "            }, \n" +
                "            {\n" +
                "                \"rownum\": \"3\", \n" +
                "                \"rowid\": \"3\", \n" +
                "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
                "            }, \n" +
                "            {\n" +
                "                \"rownum\": \"4\", \n" +
                "                \"rowid\": \"4\", \n" +
                "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
                "            }, \n" +
                "            {\n" +
                "                \"rownum\": \"5\", \n" +
                "                \"rowid\": \"5\", \n" +
                "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
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
                "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@LK@0,03@LK@0,04@A@0,05@A@0,06@LK@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
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

    }

}