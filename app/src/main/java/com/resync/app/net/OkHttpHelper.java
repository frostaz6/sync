package com.resync.app.net;

import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.dnsoverhttps.DnsOverHttps;

/* JADX INFO: renamed from: a8.a */
/* JADX INFO: compiled from: OkHttpHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class OkHttpHelper {

    static final ArrayList<Protocol> SUPPORTED_PROTOCOLS = new ArrayList<>(Arrays.asList(Protocol.HTTP_1_1));
    static OkHttpClient defaultClient;
    static OkHttpClient noRedirectClient;
    static OkHttpClient userAgentClient;

    static OkHttpClient redirectClient;

    static OkHttpClient manifestCacheClient;

    static class UserAgentInterceptor implements Interceptor {
        UserAgentInterceptor() {
        }

        @Override // okhttp3.Interceptor
        public Response intercept(Interceptor.Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().header("User-Agent", AccountSingleton.getUserAgent()).build());
        }
    }

    static class DefaultUserAgentInterceptor implements Interceptor {
        DefaultUserAgentInterceptor() {
        }

        @Override // okhttp3.Interceptor
        public Response intercept(Interceptor.Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().header("User-Agent", AccountSingleton.getUserAgent()).build());
        }
    }

    public static OkHttpClient getDefaultClient() {
        Dns dnsF;
        if (defaultClient == null) {
            OkHttpClient.Builder builderAddInterceptor = new OkHttpClient.Builder().fastFallback(true).protocols(SUPPORTED_PROTOCOLS).cache(null).addInterceptor(new DefaultUserAgentInterceptor());
            if (SettingsSingleton.getSettings().doh && (dnsF = buildDohDns(builderAddInterceptor)) != null) {
                builderAddInterceptor = builderAddInterceptor.dns(dnsF);
            }
            defaultClient = builderAddInterceptor.build();
        }
        return defaultClient;
    }

    public static synchronized OkHttpClient getNoRedirectClient() {
        return noRedirectClient;
    }

    public static synchronized OkHttpClient getManifestCacheClient() {
        return manifestCacheClient;
    }

    public static synchronized OkHttpClient getUserAgentClient() {
        return userAgentClient;
    }

    public static synchronized OkHttpClient getRedirectClient() {
        return redirectClient;
    }

    public static Dns buildDohDns(OkHttpClient.Builder builder) {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(InetAddress.getByName("8.8.4.4"));
            arrayList.add(InetAddress.getByName("8.8.8.8"));
            return new DnsOverHttps.Builder().client(builder.build()).url(HttpUrl.get("https://dns.google/dns-query")).bootstrapDnsHosts(arrayList).build();
        } catch (Exception unused) {
            return null;
        }
    }

    public static void initClients() {
        initNoRedirectClient();
        initUserAgentClient();
        initRedirectClient();
        initManifestCacheClient();
    }

    private static void initNoRedirectClient() {
        Dns dnsF;
        OkHttpClient.Builder builderCache = new OkHttpClient.Builder().fastFallback(true).protocols(SUPPORTED_PROTOCOLS).cache(null);
        TimeUnit timeUnit = TimeUnit.SECONDS;
        OkHttpClient.Builder builderFollowRedirects = builderCache.readTimeout(10L, timeUnit).connectTimeout(10L, timeUnit).writeTimeout(10L, timeUnit).retryOnConnectionFailure(false).followSslRedirects(false).followRedirects(false);
        if (SettingsSingleton.getSettings().doh && (dnsF = buildDohDns(builderFollowRedirects)) != null) {
            builderFollowRedirects = builderFollowRedirects.dns(dnsF);
        }
        noRedirectClient = builderFollowRedirects.build();
    }

    private static void initManifestCacheClient() {
        Dns dnsF;
        OkHttpClient.Builder builderCache = new OkHttpClient.Builder().fastFallback(true).protocols(SUPPORTED_PROTOCOLS).cache(new Cache(new File(RedditApplication.getAppContext().getExternalCacheDir(), "manifestCache"), 10485760));
        TimeUnit timeUnit = TimeUnit.SECONDS;
        OkHttpClient.Builder builderFollowRedirects = builderCache.readTimeout(10L, timeUnit).connectTimeout(10L, timeUnit).writeTimeout(10L, timeUnit).retryOnConnectionFailure(true).followSslRedirects(true).followRedirects(true);
        if (SettingsSingleton.getSettings().doh && (dnsF = buildDohDns(builderFollowRedirects)) != null) {
            builderFollowRedirects = builderFollowRedirects.dns(dnsF);
        }
        manifestCacheClient = builderFollowRedirects.build();
    }

    private static void initUserAgentClient() {
        Dns dnsF;
        OkHttpClient.Builder builderCache = new OkHttpClient.Builder().fastFallback(true).protocols(SUPPORTED_PROTOCOLS).cache(null);
        TimeUnit timeUnit = TimeUnit.SECONDS;
        OkHttpClient.Builder builderFollowRedirects = builderCache.readTimeout(10L, timeUnit).connectTimeout(10L, timeUnit).writeTimeout(10L, timeUnit).retryOnConnectionFailure(true).followSslRedirects(true).followRedirects(true);
        if (SettingsSingleton.getSettings().doh && (dnsF = buildDohDns(builderFollowRedirects)) != null) {
            builderFollowRedirects = builderFollowRedirects.dns(dnsF);
        }
        userAgentClient = builderFollowRedirects.addInterceptor(new UserAgentInterceptor()).build();
    }

    private static void initRedirectClient() {
        Dns dnsF;
        OkHttpClient.Builder builderCache = new OkHttpClient.Builder().fastFallback(true).protocols(SUPPORTED_PROTOCOLS).cache(null);
        TimeUnit timeUnit = TimeUnit.SECONDS;
        OkHttpClient.Builder builderFollowRedirects = builderCache.readTimeout(10L, timeUnit).connectTimeout(10L, timeUnit).writeTimeout(10L, timeUnit).retryOnConnectionFailure(true).followSslRedirects(true).followRedirects(true);
        if (SettingsSingleton.getSettings().doh && (dnsF = buildDohDns(builderFollowRedirects)) != null) {
            builderFollowRedirects = builderFollowRedirects.dns(dnsF);
        }
        redirectClient = builderFollowRedirects.build();
    }
}
