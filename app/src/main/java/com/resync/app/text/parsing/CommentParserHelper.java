package com.resync.app.text.parsing;

import android.content.ContentValues;
import android.content.Context;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: renamed from: c9.a */
/* JADX INFO: compiled from: CommentParserHelper.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class CommentParserHelper {
    public static ArrayList<ContentValues> parse(Context context, String json, int level) throws JSONException {
        ArrayList<ContentValues> arrayList = new ArrayList<>();
        JSONArray jSONArray = new JSONArray(json);
        if (level == 0) {
            PostParserHelper.parsePostDefaults(context, 3, jSONArray.getJSONObject(0).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data"), arrayList, true, null);
        }
        JSONArray jSONArray2 = new JSONArray(json).getJSONObject(1).getJSONObject("data").getJSONArray("children");
        for (int i = 0; i < jSONArray2.length(); i++) {
            if (jSONArray2.getJSONObject(i).getString("kind").equals("t1")) {
                String string = jSONArray2.getJSONObject(i).getJSONObject("data").getString("author");
                boolean isBot = StringUtils.equalsIgnoreCase("AutoModerator", string) || StringUtils.equalsIgnoreCase("WholesomeBot", string) || StringUtils.equalsIgnoreCase("PCMRBot", string);
                if (!SettingsSingleton.getSettings().commentsHideAutomod || (SettingsSingleton.getSettings().commentsHideAutomod && !isBot)) {
                    PostParserHelper.parsePost(context, 1, jSONArray2.getJSONObject(i).getJSONObject("data"), arrayList, level, false, true, null);
                }
            } else if (jSONArray2.getJSONObject(i).getString("kind").equals("more")) {
                PostParserHelper.parsePost(context, 9, jSONArray2.getJSONObject(i).getJSONObject("data"), arrayList, level, false, true, null);
            }
        }
        return arrayList;
    }
}
