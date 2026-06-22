package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.model.RedditVideoDescription;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.g */
/* JADX INFO: compiled from: RedditVideoRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditVideoRequest extends Request<RedditVideoDescription> {

    final Response.Listener<RedditVideoDescription> responseListener;
    private boolean hd;
    private String videoId;

    class BandwidthComparator implements Comparator<Representation> {
        BandwidthComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Representation representation, Representation representation2) {
            return Integer.compare(representation2.bandwidth, representation.bandwidth);
        }
    }

    class Representation {

        public int bandwidth;
        public String url;

        public Representation(int bandwidth, String url) {
            this.bandwidth = bandwidth;
            this.url = url;
        }
    }

    public RedditVideoRequest(String str, boolean z, Response.Listener<RedditVideoDescription> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        this.hd = z;
        this.videoId = com.resync.app.util.links.LinkHandler.getRedditVideoId(str);
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    public static String buildUrl(String str) {
        return "https://v.redd.it/" + com.resync.app.util.links.LinkHandler.getRedditVideoId(str) + "/DASHPlaylist.mpd";
    }

    private static String findAudioUrl(Element element) {
        NodeList elementsByTagName = element.getElementsByTagName("Representation");
        for (int representationIndex = 0; representationIndex < elementsByTagName.getLength(); representationIndex++) {
            String textContent = ((Element) elementsByTagName.item(representationIndex)).getElementsByTagName("BaseURL").item(0).getTextContent();
            if (StringUtils.containsIgnoreCase(textContent, "DASH_audio") || StringUtils.containsIgnoreCase(textContent, MimeTypes.BASE_TYPE_AUDIO)) {
                return textContent;
            }
        }
        return null;
    }

    private String findBestVideoUrl(Element element) {
        NodeList elementsByTagName = element.getElementsByTagName("Representation");
        Logger.verbose("ELEMENTS: " + elementsByTagName.getLength());
        ArrayList arrayList = new ArrayList();
        int length = elementsByTagName.getLength();
        while (true) {
            length--;
            if (length < 0) {
                Collections.sort(arrayList, new BandwidthComparator());
                return ((Representation) arrayList.get(0)).url;
            }
            Element element2 = (Element) elementsByTagName.item(length);
            arrayList.add(new Representation(Integer.parseInt(element2.getAttribute("bandwidth")), element2.getElementsByTagName("BaseURL").item(0).getTextContent()));
        }
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(RedditVideoDescription redditVideoDescription) {
        Response.Listener<RedditVideoDescription> listener = this.responseListener;
        if (listener != null) {
            listener.onResponse(redditVideoDescription);
        }
    }

    @Override // com.android.volley.Request
    public Response<RedditVideoDescription> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream((str).getBytes("UTF-8"))).getElementsByTagName("AdaptationSet");
            String videoUrl = null;
            String audioUrl = null;
            for (int setIndex = 0; setIndex < elementsByTagName.getLength(); setIndex++) {
                Element element = (Element) elementsByTagName.item(setIndex);
                // Case-INSENSITIVE: Reddit changed the audio BaseURL from "DASH_audio.mp4" to
                // "DASH_AUDIO_128.mp4" (uppercase + bitrate) ~2021. The old case-sensitive contains()
                // failed to match it, so the audio AdaptationSet was misclassified as video and
                // OVERWROTE the real video URL -> downloads produced audio-only files for years.
                String setText = element.getTextContent();
                if (StringUtils.containsIgnoreCase(setText, "DASH_audio") || StringUtils.containsIgnoreCase(setText, MimeTypes.BASE_TYPE_AUDIO) || "audio".equalsIgnoreCase(element.getAttribute("contentType"))) {
                    audioUrl = findAudioUrl(element);
                } else {
                    videoUrl = findBestVideoUrl(element);
                }
            }
            Logger.verbose("RVD", "Video url: " + videoUrl);
            Logger.verbose("RVD", "Audio url: " + audioUrl);
            return Response.success(new RedditVideoDescription(this.videoId, videoUrl, audioUrl), CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
