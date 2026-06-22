package com.resync.app.util;

import java.io.UnsupportedEncodingException;
import kotlin.UByte;

/* JADX INFO: renamed from: pe.a */
/* JADX INFO: compiled from: Base64.java */
/* JADX INFO: loaded from: classes3.dex */
public class Base64 {

    private static abstract class Coder {

        byte[] output;
        int op;

        private Coder() {
        }
    }

    private static class Decoder extends Coder {

        private static final int[] DECODE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int state;
        private int value;

        private final int[] alphabet;

        Decoder(int flags, byte[] output) {
            super();
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        boolean process(byte[] input, int offset, int len, boolean finish) {
            int initialState = this.state;
            if (initialState == 6) {
                return false;
            }
            int end = len + offset;
            int initialValue = this.value;
            byte[] output = this.output;
            int[] decode = this.alphabet;
            int op = 0;
            int value = initialValue;
            int state = initialState;
            int p = offset;
            while (p < end) {
                if (state == 0) {
                    while (true) {
                        int next = p + 4;
                        if (next > end || (value = (decode[input[p] & UByte.MAX_VALUE] << 18) | (decode[input[p + 1] & UByte.MAX_VALUE] << 12) | (decode[input[p + 2] & UByte.MAX_VALUE] << 6) | decode[input[p + 3] & UByte.MAX_VALUE]) < 0) {
                            break;
                        }
                        output[op + 2] = (byte) value;
                        output[op + 1] = (byte) (value >> 8);
                        output[op] = (byte) (value >> 16);
                        op += 3;
                        p = next;
                    }
                    if (p >= end) {
                        break;
                    }
                }
                int next2 = p + 1;
                int d = decode[input[p] & UByte.MAX_VALUE];
                if (state != 0) {
                    if (state == 1) {
                        if (d < 0) {
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        d |= value << 6;
                    } else if (state == 2) {
                        if (d < 0) {
                            if (d == -2) {
                                output[op] = (byte) (value >> 4);
                                op++;
                                state = 4;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                        }
                        d |= value << 6;
                    } else if (state != 3) {
                        if (state != 4) {
                            if (state == 5 && d != -1) {
                                this.state = 6;
                                return false;
                            }
                        } else if (d == -2) {
                            state++;
                        } else if (d != -1) {
                            this.state = 6;
                            return false;
                        }
                    } else if (d >= 0) {
                        int combined = d | (value << 6);
                        output[op + 2] = (byte) combined;
                        output[op + 1] = (byte) (combined >> 8);
                        output[op] = (byte) (combined >> 16);
                        op += 3;
                        value = combined;
                        state = 0;
                    } else if (d == -2) {
                        output[op + 1] = (byte) (value >> 2);
                        output[op] = (byte) (value >> 10);
                        op += 2;
                        state = 5;
                    } else if (d != -1) {
                        this.state = 6;
                        return false;
                    }
                    state++;
                    value = d;
                } else if (d >= 0) {
                    state++;
                    value = d;
                } else if (d != -1) {
                    this.state = 6;
                    return false;
                }
                p = next2;
            }
            if (!finish) {
                this.state = state;
                this.value = value;
                this.op = op;
                return true;
            }
            if (state == 1) {
                this.state = 6;
                return false;
            }
            if (state == 2) {
                output[op] = (byte) (value >> 4);
                op++;
            } else if (state == 3) {
                int op2 = op + 1;
                output[op] = (byte) (value >> 10);
                op = op2 + 1;
                output[op2] = (byte) (value >> 2);
            } else if (state == 4) {
                this.state = 6;
                return false;
            }
            this.state = state;
            this.op = op;
            return true;
        }
    }

    private static class Encoder extends Coder {
        private static final byte[] ENCODE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        private static final byte[] ENCODE_WEBSAFE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        private final byte[] tail;
        int tailLen;

        private int count;

        final boolean do_padding;
        final boolean do_newline;
        final boolean do_cr;

        private final byte[] alphabet;

        Encoder(int flags, byte[] output) {
            super();
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            boolean doNewline = (flags & 2) == 0;
            this.do_newline = doNewline;
            this.do_cr = (flags & 4) != 0;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = doNewline ? 19 : -1;
        }

        void process(byte[] input, int offset, int inputLen, boolean finish) {
            byte[] alphabet = this.alphabet;
            byte[] output = this.output;
            int op = 0;
            int count = this.count;
            int p = offset;
            int len = inputLen + offset;
            int v = -1;
            switch (this.tailLen) {
                case 1:
                    if (p + 2 <= len) {
                        v = ((this.tail[0] & 255) << 16) | ((input[p++] & 255) << 8) | (input[p++] & 255);
                        this.tailLen = 0;
                    }
                    break;
                case 2:
                    if (p + 1 <= len) {
                        v = ((this.tail[0] & 255) << 16) | ((this.tail[1] & 255) << 8) | (input[p++] & 255);
                        this.tailLen = 0;
                    }
                    break;
            }
            if (v != -1) {
                output[op++] = alphabet[(v >> 18) & 63];
                output[op++] = alphabet[(v >> 12) & 63];
                output[op++] = alphabet[(v >> 6) & 63];
                output[op++] = alphabet[v & 63];
                if (--count == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                    count = 19;
                }
            }
            while (p + 3 <= len) {
                v = ((input[p] & 255) << 16) | ((input[p + 1] & 255) << 8) | (input[p + 2] & 255);
                output[op] = alphabet[(v >> 18) & 63];
                output[op + 1] = alphabet[(v >> 12) & 63];
                output[op + 2] = alphabet[(v >> 6) & 63];
                output[op + 3] = alphabet[v & 63];
                p += 3;
                op += 4;
                if (--count == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                    count = 19;
                }
            }
            if (finish) {
                if (p - this.tailLen == len - 1) {
                    int t = 0;
                    v = ((this.tailLen > 0 ? this.tail[t++] : input[p++]) & 255) << 4;
                    this.tailLen -= t;
                    output[op++] = alphabet[(v >> 6) & 63];
                    output[op++] = alphabet[v & 63];
                    if (this.do_padding) {
                        output[op++] = 61;
                        output[op++] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }
                        output[op++] = 10;
                    }
                } else if (p - this.tailLen == len - 2) {
                    int t = 0;
                    v = ((this.tailLen > 1 ? this.tail[t++] : input[p++]) & 255) << 10;
                    v |= ((this.tailLen > 0 ? this.tail[t++] : input[p++]) & 255) << 2;
                    this.tailLen -= t;
                    output[op++] = alphabet[(v >> 12) & 63];
                    output[op++] = alphabet[(v >> 6) & 63];
                    output[op++] = alphabet[v & 63];
                    if (this.do_padding) {
                        output[op++] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }
                        output[op++] = 10;
                    }
                } else if (this.do_newline && op > 0 && count != 19) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }
                    output[op++] = 10;
                }
            } else {
                if (p == len - 1) {
                    byte[] tailBuf = this.tail;
                    int tailPos = this.tailLen;
                    this.tailLen = tailPos + 1;
                    tailBuf[tailPos] = input[p];
                } else if (p == len - 2) {
                    byte[] tailBuf = this.tail;
                    int tailPos = this.tailLen;
                    this.tailLen = tailPos + 1;
                    tailBuf[tailPos] = input[p];
                    int tailPos2 = this.tailLen;
                    this.tailLen = tailPos2 + 1;
                    tailBuf[tailPos2] = input[p + 1];
                }
            }
            this.op = op;
            this.count = count;
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    private static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    private static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[(len * 3) / 4]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        }
        int outputLen = decoder.op;
        byte[] output = decoder.output;
        if (outputLen == output.length) {
            return output;
        }
        byte[] trimmed = new byte[outputLen];
        System.arraycopy(output, 0, trimmed, 0, outputLen);
        return trimmed;
    }

    private static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    private static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int outputLen = (len / 3) * 4;
        if (!encoder.do_padding) {
            int remainder = len % 3;
            if (remainder == 1) {
                outputLen += 2;
            } else if (remainder == 2) {
                outputLen += 3;
            }
        } else if (len % 3 > 0) {
            outputLen += 4;
        }
        if (encoder.do_newline && len > 0) {
            outputLen += (((len - 1) / 57) + 1) * (encoder.do_cr ? 2 : 1);
        }
        encoder.output = new byte[outputLen];
        encoder.process(input, offset, len, true);
        return encoder.output;
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
