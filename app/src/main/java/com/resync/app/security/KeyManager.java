package com.resync.app.security;

import android.provider.Settings;
import com.resync.app.RedditApplication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;
import com.resync.app.security.Encryption;
import com.resync.app.premium.UltraHelper;

/* JADX INFO: renamed from: u7.g */
/* JADX INFO: compiled from: KeyManager.java */
/* JADX INFO: loaded from: classes.dex */
public class KeyManager {
    private static String bytesToHex(byte[] bArr) {
        StringBuilder sb2 = new StringBuilder();
        for (byte value : bArr) {
            String hexString = Integer.toHexString(value & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb2.append('0');
            }
            sb2.append(hexString);
        }
        return sb2.toString();
    }

    public static String decrypt(String str) {
        return Encryption.create(ultraSkuPassword(), UltraHelper.getPurchaseToken(), new byte[16]).decryptOrNull(str);
    }

    public static String encrypt(String str) {
        return Encryption.create(ultraSkuPassword(), UltraHelper.getPurchaseToken(), new byte[16]).encryptOrNull(str);
    }

    private static String md5Hex(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return String.valueOf(str.hashCode());
        }
    }

    private static String ultraSkuPassword() {
        return md5Hex(UltraHelper.getPurchaseToken() + "_sync_ultra_sku_com.resync.app");
    }

    public static String hashKey(String str) {
        return md5Hex(md5Hex(str + "_sync_com.resync.app"));
    }

    public static String adsEnabledKey() {
        return md5Hex(md5Hex(Settings.Secure.getString(RedditApplication.getAppContext().getContentResolver(), "android_id") + "_sync_com.resync.app"));
    }

    public static String purchaseTokenKey() {
        return md5Hex(md5Hex(Settings.Secure.getString(RedditApplication.getAppContext().getContentResolver(), "android_id") + "_sync_purchase_tokencom.resync.app"));
    }

    public static String ultraSkuKey() {
        return md5Hex(md5Hex(Settings.Secure.getString(RedditApplication.getAppContext().getContentResolver(), "android_id") + "_sync_ultra_sku_com.resync.app"));
    }
}
