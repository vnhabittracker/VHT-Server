package habit.tracker.habittracker.common.util;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

import habit.tracker.habittracker.R;

public class AppDefaultConfig {
    private Map<String, String> sourceMap;

    public void readFromXml(Context context) throws IOException, XmlPullParserException {
        sourceMap = XmlAppHelper.readFromAnXML(context, R.xml.app_default);
    }

    public int getUserLevel(int score) {
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level1))) {
            return 1;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level2))) {
            return 2;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level3))) {
            return 3;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level4))) {
            return 4;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level5))) {
            return 5;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level6))) {
            return 6;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level7))) {
            return 7;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level8))) {
            return 8;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level9))) {
            return 9;
        }
        if (score < Integer.parseInt(sourceMap.get(XmlAppHelper.level0))) {
            return 10;
        }
        return 11;
    }
}
