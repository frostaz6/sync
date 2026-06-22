package com.resync.app.dialogs;

import android.os.Build;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.ScrollToTopOfPreferencesEvent;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: ca.b */
/* JADX INFO: compiled from: ImportThemeInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ImportThemeInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("monet_system") && jSONObject.getBoolean("monet_system") && Build.VERSION.SDK_INT < 31) {
                StaticToast.show("This device does not support automatic theming");
                dismissAllowingStateLoss();
                return;
            }
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Object obj = jSONObject.get(next);
                if (next.startsWith("monet_")) {
                    if (StringUtils.equalsAnyIgnoreCase(next, "monet_color_intensity")) {
                        SettingsSingleton.getInstance().putString(next, obj.toString());
                    } else if (StringUtils.containsIgnoreCase(next, "_color") && (obj instanceof Integer) && ((Integer) obj).intValue() == 0) {
                        SettingsSingleton.getInstance().removeKey(next);
                    } else if (obj instanceof Integer) {
                        SettingsSingleton.getInstance().putInt(next, ((Integer) obj).intValue());
                    }
                    if (obj instanceof Boolean) {
                        SettingsSingleton.getInstance().putBoolean(next, ((Boolean) obj).booleanValue());
                    } else if (obj instanceof Float) {
                        SettingsSingleton.getInstance().putFloat(next, ((Float) obj).floatValue());
                    } else if (obj instanceof String) {
                        SettingsSingleton.getInstance().putString(next, (String) obj);
                    }
                }
                Logger.verbose("THEME", next + " - " + jSONObject.get(next));
            }
        } catch (Exception e) {
            Logger.logException(e);
            StaticToast.showWithContext(getContext(), "Could not load theme");
            return;
        }
        StaticToast.showWithContext(getContext(), "Theme loaded");
        SettingsSingleton.getInstance().reload();
        BusSingleton.getBus().post(new ScrollToTopOfPreferencesEvent());
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Load";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Theme";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Input theme";
    }
}
