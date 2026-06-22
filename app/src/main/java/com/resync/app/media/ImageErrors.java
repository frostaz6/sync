package com.resync.app.media;

/* JADX INFO: renamed from: h8.b */
/* JADX INFO: compiled from: ImageErrors.java */
/* JADX INFO: loaded from: classes.dex */
public final class ImageErrors {
    public static String getMessage(int errorCode) {
        if (errorCode == 2) {
            return "404 File not found";
        }
        if (errorCode == 1) {
            return "Failed to download image";
        }
        if (errorCode == 3) {
            return "Invalid MIME type";
        }
        if (errorCode == 6) {
            return "Could not open the downloaded GIF";
        }
        if (errorCode == 7) {
            return "Could not open the downloaded image";
        }
        if (errorCode == 8) {
            return "Could not find the downloaded image";
        }
        if (errorCode == 9) {
            return "403 (Forbidden)";
        }
        if (errorCode == 10) {
            return "Could not decode bitmap (try refreshing)";
        }
        if (errorCode == 11) {
            return "Image was too large (>50MB)";
        }
        if (errorCode == 12) {
            return "Could not load Streamable link";
        }
        if (errorCode == 14) {
            return "Could not load video";
        }
        if (errorCode == 15) {
            return "Image was actually a web page!";
        }
        if (errorCode == 16) {
            return "Gfycat removed this file due to a copyright claim";
        }
        if (errorCode == 18) {
            return "Could not load vid.me link";
        }
        if (errorCode == 19) {
            return "File not found";
        }
        if (errorCode == 20) {
            return "EOFException";
        }
        if (errorCode == 21) {
            return "Image download size mismatch";
        }
        if (errorCode == 22) {
            return "Error connecting to Gfycat";
        }
        if (errorCode == 23) {
            return "Error connecting to Eroshare";
        }
        if (errorCode == 24) {
            return "SSL exception (are you on a public network?)";
        }
        if (errorCode == 25) {
            return "The file has been moved with no redirect given";
        }
        if (errorCode == 26) {
            return "This request has timed out";
        }
        if (errorCode == 27) {
            return "Unknown host";
        }
        if (errorCode == 28) {
            return "IO Exception";
        }
        if (errorCode == 29) {
            return "Illegal Argument (bad URL?)";
        }
        if (errorCode == 30) {
            return "streaming videos are not compatible with the legacy player";
        }
        if (errorCode == 31) {
            return "Error loading Tumblr post";
        }
        if (errorCode == 32) {
            return "Error decoding video (unknown issue)";
        }
        if (errorCode == 33) {
            return "Error decoding video (renderer issue)";
        }
        if (errorCode == 34) {
            return "Error decoding video (source issue)";
        }
        if (errorCode == 35) {
            return "Error decoding video (unexpected issue)";
        }
        if (errorCode == 36) {
            return "The image host has disabled hotlinking";
        }
        if (errorCode == 37) {
            return "Glide failed to grab this image";
        }
        if (errorCode == 39) {
            return "Missing URL?";
        }
        if (errorCode == 40) {
            return "Error connecting to Redgifs";
        }
        return "Unknown error (" + errorCode + ")";
    }
}
