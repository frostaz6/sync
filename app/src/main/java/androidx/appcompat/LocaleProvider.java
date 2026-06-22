package androidx.appcompat;

import android.content.Context;
import androidx.core.os.ConfigurationCompat;
import java.util.Locale;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: renamed from: g7.a */
/* JADX INFO: compiled from: LocaleProvider.kt */
/* JADX INFO: loaded from: classes.dex */
public final class LocaleProvider {
    public static final LocaleProvider INSTANCE = new LocaleProvider();
    private static Function1<? super Context, Locale> localeResolver = DefaultLocaleResolver.INSTANCE;

    /* JADX INFO: renamed from: g7.a$a */
    /* JADX INFO: compiled from: LocaleProvider.kt */
    static final class DefaultLocaleResolver extends Lambda<Locale> implements Function1<Context, Locale> {
        public static final DefaultLocaleResolver INSTANCE = new DefaultLocaleResolver();

        DefaultLocaleResolver() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Locale invoke(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            return ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
        }
    }

    private LocaleProvider() {
    }

    public final Locale getLocale(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return localeResolver.invoke(context);
    }
}
