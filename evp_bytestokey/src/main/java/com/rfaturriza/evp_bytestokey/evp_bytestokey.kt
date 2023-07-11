import java.security.MessageDigest

class BytesToKey {
    companion object {
        /**
         * Generate key and iv for given password, salt and number of bits to be used
         * to generate key and iv.
         * @param password type of `ByteArray`
         * @param salt type of `ByteArray`
         * @param keyLen type of `Int`
         * @param ivLen type of `Int`
         * @return Pair of key and iv
         */
        private fun generate(
            password: ByteArray,
            salt: ByteArray?,
            keyLen: Int,
            ivLen: Int
        ): Pair<ByteArray, ByteArray> {
            require(!(salt != null && salt.size != 8)) { "salt should be ByteArray with 8 byte length" }

            var ivLenChanged = ivLen
            var newKeyLen = keyLen / 8
            val key = ByteArray(newKeyLen)
            val iv = ByteArray(ivLen)

            var tmp = ByteArray(0)

            while (newKeyLen > 0 || ivLenChanged > 0) {
                val hash = MessageDigest.getInstance("MD5")
                hash.update(tmp)
                hash.update(password)
                if (salt != null) hash.update(salt)
                tmp = hash.digest()

                var used = 0

                if (newKeyLen > 0) {
                    val keyStart = key.size - newKeyLen
                    used = minOf(newKeyLen, tmp.size)
                    System.arraycopy(tmp, 0, key, keyStart, used)
                    newKeyLen -= used
                }

                if (used < tmp.size && ivLenChanged > 0) {
                    val ivStart = iv.size - ivLenChanged
                    val length = minOf(ivLenChanged, tmp.size - used)
                    System.arraycopy(tmp, used, iv, ivStart, length)
                    ivLenChanged -= length
                }
            }

            tmp.fill(0)
            return Pair(key, iv)
        }
    }
}