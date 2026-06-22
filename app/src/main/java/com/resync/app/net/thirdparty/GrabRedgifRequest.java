package com.resync.app.net.thirdparty;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.singleton.AccountSingleton;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.util.links.LinkHandler;

/**
 * Resolves a RedGifs link to a playable MP4 URL.
 *
 * <p>ReSync rewrite: RedGifs changed their API — the old {@code api.redgifs.com/v2/gifs/<id>} response
 * (which the original code parsed for {@code urls.hd}/{@code urls.sd}) now returns only
 * {@code urls.html} (an iframe link), so the old parse failed with "No value for hd" and every RedGifs
 * video errored. Instead we fetch the public {@code /ifr/<id>} embed page (no auth/token/IP step needed)
 * and read the direct media URL from its {@code og:video} meta tag, then derive the canonical
 * (audio-bearing) variant by stripping the {@code -silent} suffix. The playable file requires the
 * CamelCase name, which the JSON API no longer exposes but the embed page does.
 */
public class GrabRedgifRequest extends Request<String> {

    private final RedgifRequestWrapper wrapper;

    // Matches https://media.redgifs.com/<CamelCaseName>[-silent|-mobile].mp4 in the embed page HTML.
    private static final Pattern MEDIA_PATTERN =
            Pattern.compile("https://media\\.redgifs\\.com/([A-Za-z0-9]+)(?:-silent|-mobile)?\\.mp4");

    public GrabRedgifRequest(RedgifRequestWrapper redgifRequestWrapper) {
        super(0, buildUrl(redgifRequestWrapper), redgifRequestWrapper.getErrorListener());
        this.wrapper = redgifRequestWrapper;
        setShouldCache(true);
    }

    static final String buildUrl(RedgifRequestWrapper redgifRequestWrapper) {
        return "https://www.redgifs.com/ifr/" + LinkHandler.getGfycatId(redgifRequestWrapper.getGifUrl());
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = AccountSingleton.getDefaultHeaders();
        // The embed page is server-rendered for real browsers; present a desktop UA so the og:video
        // meta tag is included.
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36");
        return headers;
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String html = new String(networkResponse.data);
            Matcher m = MEDIA_PATTERN.matcher(html);
            if (m.find()) {
                String name = m.group(1);   // CamelCase base name, e.g. WideIncomparableHamadryas
                // The plain "<name>.mp4" is the HD file (includes audio when the source has it);
                // "-silent"/"-mobile" are derivatives. Prefer the canonical HD file.
                String url = "https://media.redgifs.com/" + name + ".mp4";
                return Response.success(url, LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
            }
            return Response.error(new VolleyError("No media URL in RedGifs embed page"));
        } catch (Exception e) {
            return Response.error(new VolleyError("Error parsing"));
        }
    }

    @Override // com.android.volley.Request
    public void deliverResponse(String str) {
        this.wrapper.deliverResponse(str);
    }
}
