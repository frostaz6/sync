package com.resync.app;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import java.io.InputStream;
import okhttp3.Call;
import com.resync.app.media.preview.WebsitePreviewModel;
import com.resync.app.media.preview.WebsitePreviewModelLoader;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.util.Logger;

@GlideModule
public class YourAppGlideModule extends AppGlideModule {

    class OkHttpFactory extends OkHttpUrlLoader.Factory {
        OkHttpFactory(Call.Factory factory) {
            super(factory);
        }
    }

    @Override // com.bumptech.glide.module.LibraryGlideModule
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpFactory(OkHttpHelper.getDefaultClient()));
        registry.append(WebsitePreviewModel.class, InputStream.class, new WebsitePreviewModelLoader.Factory());
        Logger.verbose("Setting up glide with OkHTTP");
    }

    @Override // com.bumptech.glide.module.AppGlideModule
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        Logger.verbose("Glide default setup: " + context);
        glideBuilder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        glideBuilder.setDiskCache(new InternalCacheDiskCacheFactory(context, "newGlideCache", (long) 734003200));
    }
}
