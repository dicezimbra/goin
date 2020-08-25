package br.com.legacy.utils;

import com.bumptech.glide.load.Key;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Created by appsimples on 11/7/17.
 */

public class LongVersionSignature implements Key {

    private long currentVersion;

    public LongVersionSignature(Long timestamp) {
        this.currentVersion = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LongVersionSignature) {
            LongVersionSignature other = (LongVersionSignature) o;
            return currentVersion == other.currentVersion;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(currentVersion).hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest md) {
        md.update(ByteBuffer.allocate(Long.SIZE).putLong(this.currentVersion).array());
    }
}
