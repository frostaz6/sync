package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.model.Kinds;
import com.resync.app.model.Message;
import com.resync.app.helpers.TimestampHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.a0 */
/* JADX INFO: compiled from: OAuthMessagesRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthMessagesRequest extends RedditRequest<Message[]> {

    public static final String TAG = OAuthMessagesRequest.class.toString();
    final int inboxType;

    public OAuthMessagesRequest(Context context, int inboxType, String str, Response.Listener<Message[]> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, inboxType, str), listener, errorListener);
        this.inboxType = inboxType;
        setTag(TAG);
    }

    private static String buildUrl(Context context, int inboxType, String str) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SettingsSingleton.getOauthUrl(context));
        sb2.append("message/");
        switch (inboxType) {
            case 0:
                sb2.append("inbox");
                break;
            case 1:
                sb2.append("unread");
                break;
            case 2:
                sb2.append("messages");
                break;
            case 3:
                sb2.append("sent");
                break;
            case 4:
                sb2.append("comments");
                break;
            case 5:
                sb2.append("selfreply");
                break;
            case 6:
                sb2.append("mentions");
                break;
            case 7:
                sb2.append("moderator");
                break;
            case 8:
                sb2.append("moderator/unread");
                break;
        }
        // mark=false: just VIEWING a tab must not mark the messages read as a side-effect (the bare
        // /message/unread.json marks-read on read, so the next refresh comes back empty). Matches
        // OAuthNewMessagesRequest, which already passes mark=false.
        sb2.append(".json?mark=false");
        if (str != null) {
            sb2.append("&after=");
            sb2.append(str);
        }
        return sb2.toString();
    }

    private void parseMessage(ArrayList<Message> arrayList, String str, int depth, JSONObject jSONObject) throws JSONException {
        JSONObject jSONObjectOptJSONObject;
        Message message = new Message();
        message.kind = Kinds.fromPrefix(jSONObject.getString("kind"));
        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
        message.id = jSONObject2.getString("id");
        message.timeAgo = TimestampHelper.getRelativeTime(jSONObject2.getInt("created_utc"));
        message.author = jSONObject2.getString("author");
        message.dest = jSONObject2.getString("dest");
        message.subreddit = jSONObject2.getString("subreddit");
        message.subject = jSONObject2.getString("subject");
        String strReplaceAll = jSONObject2.getString("body").replaceAll("&gt;", ">");
        message.bodyRaw = strReplaceAll;
        message.bodyProcessed = RedditHelper.processMarkdownToHtml(null, strReplaceAll);
        message.isNew = jSONObject2.getBoolean("new");
        message.parentId = str;
        message.linkTitle = WebsafeUtils.unescapeHtml(jSONObject2.optString("link_title"));
        message.context = jSONObject2.optString("context");
        message.depth = depth;
        int inboxType = this.inboxType;
        if (inboxType == 7 || inboxType == 8) {
            message.comment = true;
        }
        arrayList.add(message);
        JSONObject jSONObjectOptJSONObject2 = jSONObject2.optJSONObject("replies");
        if (jSONObjectOptJSONObject2 == null || (jSONObjectOptJSONObject = jSONObjectOptJSONObject2.optJSONObject("data")) == null) {
            return;
        }
        JSONArray jSONArray = jSONObjectOptJSONObject.getJSONArray("children");
        for (int replyIndex = 0; replyIndex < jSONArray.length(); replyIndex++) {
            parseMessage(arrayList, str, depth + 2, jSONArray.getJSONObject(replyIndex));
        }
    }

    @Override // com.android.volley.Request
    public Response<Message[]> parseNetworkResponse(NetworkResponse networkResponse) {
        String str = new String(networkResponse.data);
        try {
            // optString(...,null) NOT getString: Unread/DM listings routinely have "after": null
            // (single page), and getString() THROWS on a JSON null -> ParseError -> the tab renders
            // empty even though messages were returned. This was the core "Unread/DMs show nothing".
            String string = new JSONObject(str).getJSONObject("data").optString("after", null);
            JSONArray jSONArrayOptJSONArray = new JSONObject(str).getJSONObject("data").optJSONArray("children");
            ArrayList<Message> arrayList = new ArrayList<>();
            for (int childIndex = 0; childIndex < jSONArrayOptJSONArray.length(); childIndex++) {
                parseMessage(arrayList, string, 0, jSONArrayOptJSONArray.getJSONObject(childIndex));
            }
            return Response.success((Message[]) arrayList.toArray(new Message[arrayList.size()]), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
