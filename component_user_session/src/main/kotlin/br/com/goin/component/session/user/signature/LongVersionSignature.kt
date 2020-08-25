package br.com.goin.component.session.user.signature

import com.bumptech.glide.load.Key

import java.nio.ByteBuffer
import java.security.MessageDigest

class LongVersionSignature(private val currentVersion: Long) : Key {

    override fun equals(o: Any?): Boolean {
        if (o is LongVersionSignature) {
            val other = o as LongVersionSignature?
            return currentVersion == other!!.currentVersion
        }
        return false
    }

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(currentVersion).hashCode()
    }

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(java.lang.Long.SIZE).putLong(this.currentVersion).array())
    }
}
