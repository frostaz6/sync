package de.jetwick.snacktory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: classes3.dex */
public class Converter {
    public static final String ISO = "ISO-8859-1";
    public static final int BUFFER_SIZE = 2048;
    public static final String UTF8 = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(Converter.class);
    private int maxBytes = 500000;
    private String encoding;
    private String url;

    public Converter(String str) {
        this.url = str;
    }

    public static String extractEncoding(String str) {
        String strSubstring = "";
        for (String str2 : str != null ? str.split(";") : new String[0]) {
            String lowerCase = str2.trim().toLowerCase();
            if (lowerCase.startsWith("charset=")) {
                strSubstring = lowerCase.substring(8);
            }
        }
        return strSubstring.length() == 0 ? "ISO-8859-1" : strSubstring;
    }

    protected String detectEncodingFromStream(String str, ByteArrayOutputStream byteArrayOutputStream, BufferedInputStream bufferedInputStream, String str2) throws IOException {
        int valueEnd;
        byte[] bArr = new byte[2048];
        int totalRead = 0;
        while (totalRead < 2048) {
            int bytesRead = bufferedInputStream.read(bArr);
            if (bytesRead < 0) {
                break;
            }
            totalRead += bytesRead;
            byteArrayOutputStream.write(bArr, 0, bytesRead);
        }
        String string = byteArrayOutputStream.toString(str2);
        int iIndexOf = string.indexOf(str);
        int length = str.length();
        if (iIndexOf <= 0) {
            return null;
        }
        int valueStart = iIndexOf + length;
        char cCharAt = string.charAt(valueStart);
        if (cCharAt == '\'') {
            iIndexOf++;
            valueEnd = string.indexOf("'", iIndexOf + length);
        } else if (cCharAt == '\"') {
            iIndexOf++;
            valueEnd = string.indexOf("\"", iIndexOf + length);
        } else {
            int iIndexOf2 = string.indexOf("\"", valueStart);
            if (iIndexOf2 < 0) {
                iIndexOf2 = Integer.MAX_VALUE;
            }
            int iIndexOf3 = string.indexOf(StringUtils.SPACE, valueStart);
            int iMin2 = Math.min(iIndexOf2, iIndexOf3 >= 0 ? iIndexOf3 : Integer.MAX_VALUE);
            int iIndexOf4 = string.indexOf("'", valueStart);
            valueEnd = iIndexOf4 > 0 ? Math.min(iMin2, iIndexOf4) : iMin2;
        }
        int adjustedValueStart = iIndexOf + length;
        if (valueEnd <= adjustedValueStart || valueEnd >= adjustedValueStart + 40) {
            return null;
        }
        String strEncodingCleanup = SHelper.encodingCleanup(string.substring(adjustedValueStart, valueEnd));
        try {
            bufferedInputStream.reset();
            byteArrayOutputStream.reset();
            return strEncodingCleanup;
        } catch (IOException e) {
            LOG.info("Couldn't reset stream to re-read with new encoding " + strEncodingCleanup + StringUtils.SPACE + e.toString());
            return null;
        }
    }

    public String getEncoding() {
        String str = this.encoding;
        return str == null ? "" : str.toLowerCase();
    }

    public Converter setMaxBytes(int maxBytes) {
        this.maxBytes = maxBytes;
        return this;
    }

    public String streamToString(InputStream inputStream) {
        return streamToString(inputStream, this.maxBytes, this.encoding);
    }

    public String streamToString(InputStream inputStream, String str) {
        return streamToString(inputStream, this.maxBytes, str);
    }

    public String streamToString(InputStream inputStream, int maxBytes, String str) {
        BufferedInputStream bufferedInputStream = null;
        this.encoding = str;
        if (str == null || str.isEmpty()) {
            this.encoding = "UTF-8";
        }
        BufferedInputStream bufferedInputStream2 = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream, 2048);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                bufferedInputStream.mark(4096);
                String charsetEncoding = detectEncodingFromStream("charset=", byteArrayOutputStream, bufferedInputStream, this.encoding);
                if (charsetEncoding != null) {
                    this.encoding = charsetEncoding;
                } else {
                    Logger logger = LOG;
                    logger.debug("no charset found in first stage");
                    String encodingAttr = detectEncodingFromStream("encoding=", byteArrayOutputStream, bufferedInputStream, this.encoding);
                    if (encodingAttr != null) {
                        this.encoding = encodingAttr;
                    } else {
                        logger.debug("no charset found in second stage");
                    }
                }
                if (!Charset.isSupported(this.encoding)) {
                    throw new UnsupportedEncodingException(this.encoding);
                }
            } catch (UnsupportedEncodingException e) {
                LOG.info("Using default encoding:UTF-8 problem:" + e.getMessage() + " encoding:" + this.encoding + StringUtils.SPACE + this.url);
                this.encoding = "UTF-8";
            }
            int size = byteArrayOutputStream.size();
            byte[] bArr = new byte[2048];
            while (true) {
                if (size >= maxBytes) {
                    LOG.info("Maxbyte of " + maxBytes + " exceeded! Maybe html is now broken but try it nevertheless. Url: " + this.url);
                    break;
                }
                int bytesRead = bufferedInputStream.read(bArr);
                if (bytesRead < 0) {
                    break;
                }
                size += bytesRead;
                byteArrayOutputStream.write(bArr, 0, bytesRead);
            }
            String string = byteArrayOutputStream.toString(this.encoding);
            try {
                bufferedInputStream.close();
            } catch (Exception unused) {
            }
            return string;
        } catch (SocketTimeoutException e) {
            bufferedInputStream2 = bufferedInputStream;
            LOG.info(e.toString() + " url:" + this.url);
            if (bufferedInputStream2 == null) {
                return "";
            }
            try {
                bufferedInputStream2.close();
                return "";
            } catch (Exception unused2) {
                return "";
            }
        } catch (IOException e) {
            bufferedInputStream2 = bufferedInputStream;
            LOG.warn(e.toString() + " url:" + this.url);
            if (bufferedInputStream2 == null) {
                return "";
            }
            try {
                bufferedInputStream2.close();
            } catch (Exception unused3) {
            }
            return "";
        } catch (Throwable th2) {
            bufferedInputStream2 = bufferedInputStream;
            if (bufferedInputStream2 != null) {
                try {
                    bufferedInputStream2.close();
                } catch (Exception unused4) {
                }
            }
            return "";
        }
    }

    public Converter() {
    }
}
