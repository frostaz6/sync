package com.resync.app.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: wc.f */
/* JADX INFO: compiled from: ImageUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public final class ImageUtils {

    public static class JpegInfo {

        public final int width;
        public final int height;
        public final char[] comment;
        public final String format;

        public JpegInfo(int width, int height, char[] comment, String format) {
            this.width = width;
            this.height = height;
            this.comment = comment;
            this.format = format;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.format);
            stringBuffer.append(StringUtils.SPACE);
            stringBuffer.append(this.width);
            stringBuffer.append("x");
            stringBuffer.append(this.height);
            if (this.comment != null) {
                stringBuffer.append(" (");
                stringBuffer.append(this.comment);
                stringBuffer.append(")");
            }
            return stringBuffer.toString();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final JpegInfo readJpegInfo(File file) throws IOException {
        char[] comment = null;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] header = new byte[2];
            if (bufferedInputStream.read(header, 0, 2) < 2) {
                throw new RuntimeException("Not a valid Jpeg file!");
            }
            if (header[0] != -1 || header[1] != -40) {
                throw new RuntimeException("Not a valid Jpeg file!");
            }
            int marker = 0;
            boolean foundDimensions = false;
            boolean foundComment = false;
            int width = 0;
            int height = 0;
            while (marker != 218 && (!foundDimensions || !foundComment)) {
                while (marker != 255) {
                    marker = bufferedInputStream.read();
                }
                while (marker == 255) {
                    marker = bufferedInputStream.read();
                }
                int segmentLength = (bufferedInputStream.read() * 256) + bufferedInputStream.read();
                if (segmentLength < 2) {
                    throw new RuntimeException("Not a valid Jpeg file!");
                }
                if (marker >= 192 && marker <= 195) {
                    bufferedInputStream.read();
                    height = (bufferedInputStream.read() * 256) + bufferedInputStream.read();
                    width = bufferedInputStream.read() + (bufferedInputStream.read() * 256);
                    for (int i = 0; i < (segmentLength - 2) - 5; i++) {
                        bufferedInputStream.read();
                    }
                    foundDimensions = true;
                } else if (marker == 254) {
                    int commentLength = segmentLength - 2;
                    comment = new char[commentLength];
                    for (int i = 0; i < commentLength; i++) {
                        comment[i] = (char) bufferedInputStream.read();
                    }
                    foundComment = true;
                } else {
                    for (int i = 0; i < segmentLength - 2; i++) {
                        bufferedInputStream.read();
                    }
                }
            }
            JpegInfo info = new JpegInfo(width, height, comment, "jpeg");
            try {
                bufferedInputStream.close();
            } catch (IOException unused) {
            }
            return info;
        } catch (IOException | RuntimeException th2) {
            try {
                bufferedInputStream.close();
            } catch (IOException unused2) {
            }
            throw th2;
        }
    }
}
