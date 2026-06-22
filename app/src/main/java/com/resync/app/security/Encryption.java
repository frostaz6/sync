package com.resync.app.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.resync.app.util.Base64;

/* JADX INFO: renamed from: oe.b */
/* JADX INFO: compiled from: Encryption.java */
/* JADX INFO: loaded from: classes3.dex */
public class Encryption {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final EncryptionResult config;

    /* JADX INFO: renamed from: oe.b$a, reason: case insensitive filesystem */
    /* JADX INFO: compiled from: Encryption.java */
    public static class EncryptionResult {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private byte[] salt;
        private int keyLength;
        private int base64Flags;
        private int iterationCount;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private String saltText;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private String password;
        private String cipherTransformation;
        private String keyAlgorithm;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private String charsetName;
        private String secretKeyFactoryAlgorithm;
        private String digestAlgorithm;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private String secureRandomAlgorithm;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private SecureRandom secureRandom;
        private IvParameterSpec ivParameterSpec;

        /* JADX INFO: Access modifiers changed from: private */
        public SecureRandom getSecureRandom() {
            return this.secureRandom;
        }

        private String getSecureRandomAlgorithm() {
            return this.secureRandomAlgorithm;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getCipherTransformation() {
            return this.cipherTransformation;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getBase64Flags() {
            return this.base64Flags;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getCharsetName() {
            return this.charsetName;
        }

        public static EncryptionResult create(String str, String str2, byte[] bArr) {
            return new EncryptionResult().setSalt(bArr).setPassword(str).setSaltText(str2).setKeyLength(128).setKeyAlgorithm("AES").setCharsetName("UTF8").setIterationCount(1).setDigestAlgorithm("SHA1").setBase64Flags(0).setCipherTransformation("AES/CBC/PKCS5Padding").setSecureRandomAlgorithm("SHA1PRNG").setSecretKeyFactoryAlgorithm("PBKDF2WithHmacSHA1");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getDigestAlgorithm() {
            return this.digestAlgorithm;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getIterationCount() {
            return this.iterationCount;
        }

        private byte[] getSalt() {
            return this.salt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IvParameterSpec getIvParameterSpec() {
            return this.ivParameterSpec;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getPassword() {
            return this.password;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getKeyAlgorithm() {
            return this.keyAlgorithm;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getKeyLength() {
            return this.keyLength;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getSaltText() {
            return this.saltText;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getSecretKeyFactoryAlgorithm() {
            return this.secretKeyFactoryAlgorithm;
        }

        public EncryptionResult setCipherTransformation(String str) {
            this.cipherTransformation = str;
            return this;
        }

        public EncryptionResult setBase64Flags(int base64Flags) {
            this.base64Flags = base64Flags;
            return this;
        }

        public EncryptionResult setCharsetName(String str) {
            this.charsetName = str;
            return this;
        }

        public EncryptionResult setDigestAlgorithm(String str) {
            this.digestAlgorithm = str;
            return this;
        }

        public EncryptionResult setIterationCount(int iterationCount) {
            this.iterationCount = iterationCount;
            return this;
        }

        public EncryptionResult setSalt(byte[] bArr) {
            this.salt = bArr;
            return this;
        }

        public EncryptionResult setIvParameterSpec(IvParameterSpec ivParameterSpec) {
            this.ivParameterSpec = ivParameterSpec;
            return this;
        }

        public EncryptionResult setPassword(String str) {
            this.password = str;
            return this;
        }

        public EncryptionResult setKeyAlgorithm(String str) {
            this.keyAlgorithm = str;
            return this;
        }

        public EncryptionResult setKeyLength(int keyLength) {
            this.keyLength = keyLength;
            return this;
        }

        public EncryptionResult setSaltText(String str) {
            this.saltText = str;
            return this;
        }

        public EncryptionResult setSecretKeyFactoryAlgorithm(String str) {
            this.secretKeyFactoryAlgorithm = str;
            return this;
        }

        public EncryptionResult setSecureRandom(SecureRandom secureRandom) {
            this.secureRandom = secureRandom;
            return this;
        }

        public EncryptionResult setSecureRandomAlgorithm(String str) {
            this.secureRandomAlgorithm = str;
            return this;
        }

        public Encryption build() throws NoSuchAlgorithmException {
            setSecureRandom(SecureRandom.getInstance(getSecureRandomAlgorithm()));
            setIvParameterSpec(new IvParameterSpec(getSalt()));
            return new Encryption(this, null);
        }
    }

    /* synthetic */ Encryption(EncryptionResult config, Encryption_a encryption_a) {
        this(config);
    }

    public static Encryption create(String str, String str2, byte[] bArr) {
        try {
            return EncryptionResult.create(str, str2, bArr).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SecretKey deriveKey(char[] cArr) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return new SecretKeySpec(SecretKeyFactory.getInstance(this.config.getSecretKeyFactoryAlgorithm()).generateSecret(new PBEKeySpec(cArr, this.config.getSaltText().getBytes(this.config.getCharsetName()), this.config.getIterationCount(), this.config.getKeyLength())).getEncoded(), this.config.getKeyAlgorithm());
    }

    private char[] hashPassword(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(this.config.getDigestAlgorithm());
        messageDigest.update(str.getBytes(this.config.getCharsetName()));
        return Base64.encodeToString(messageDigest.digest(), 1).toCharArray();
    }

    public String decrypt(String str) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        if (str == null) {
            return null;
        }
        byte[] bArrA = Base64.decode(str, this.config.getBase64Flags());
        SecretKey secretKeyF = deriveKey(hashPassword(this.config.getPassword()));
        Cipher cipher = Cipher.getInstance(this.config.getCipherTransformation());
        cipher.init(2, secretKeyF, this.config.getIvParameterSpec(), this.config.getSecureRandom());
        return new String(cipher.doFinal(bArrA));
    }

    public String decryptOrNull(String str) {
        try {
            return decrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encrypt(String str) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        if (str == null) {
            return null;
        }
        SecretKey secretKeyF = deriveKey(hashPassword(this.config.getPassword()));
        byte[] bytes = str.getBytes(this.config.getCharsetName());
        Cipher cipher = Cipher.getInstance(this.config.getCipherTransformation());
        cipher.init(1, secretKeyF, this.config.getIvParameterSpec(), this.config.getSecureRandom());
        return Base64.encodeToString(cipher.doFinal(bytes), this.config.getBase64Flags());
    }

    public String encryptOrNull(String str) {
        try {
            return encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Encryption(EncryptionResult config) {
        this.config = config;
    }
}
