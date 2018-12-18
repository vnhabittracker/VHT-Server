package habit.tracker.habittracker.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlAppHelper {
    private static final String USER_TAG = "user";
    public static final String USAGE = "usage";
    public static final String CURRENT_USAGE_CHAIN = "current_usage_chain";
    public static final String BEST_USAGE_CHAIN = "best_usage_chain";
    public static final String USER_SCORE = "user_score";

    public static final String level1 = "level1";
    public static final String level2 = "level1";
    public static final String level3 = "level1";
    public static final String level4 = "level1";
    public static final String level5 = "level1";
    public static final String level6 = "level1";
    public static final String level7 = "level1";
    public static final String level8 = "level1";
    public static final String level9 = "level1";
    public static final String level0 = "level1";
    public static final String level11 = "level1";

    public static Map<String, String> readFromAnXML(Context context, int resource) throws IOException, XmlPullParserException {
        Map<String, String> map = new HashMap<>();
        Resources res = context.getResources();
        XmlResourceParser xpp = res.getXml(resource);
        xpp.next();
        int eventType = xpp.getEventType();
        String key = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                key = xpp.getName();
            } else if (eventType == XmlPullParser.TEXT) {
                map.put(key, xpp.getText());
            }
            eventType = xpp.next();
        }
        return map;
    }
}
