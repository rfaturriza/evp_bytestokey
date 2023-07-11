# EVP\_BytesToKey
JS to kotlin: https://github.com/browserify/EVP_BytesToKey

The insecure [key derivation algorithm from OpenSSL.][1]

**WARNING: DO NOT USE, except for compatibility reasons.**

MD5 is insecure.

Use at least `scrypt` or `pbkdf2-hmac-sha256` instead.


## API
`EVP_BytesToKey(password, salt, keyLen, ivLen)`

* `password` - `ByteArray`, password used to derive the key data.
* `salt` - 8 byte `ByteArray` or `null`, salt is used as a salt in the derivation.
* `keyBits` - `number`, key length in **bits**.
* `ivLen` - `number`, iv length in bytes.

*Returns*: `return Pair(key,iv)`


## Examples
MD5 with `aes-192-cbc`:

```kotlin
val passwordBytes = "myPassword".toByteArray(Charsets.UTF_8)
val (key, iv) = BytesToKey.generate(passwordBytes, null, 192, 16)
// =>
// key: <Buffer a2 d9 67 9c 7f ff 3e 9c f4 30 0a 2c c3 fb ad 79 59 ed ae e8 a4 3c 1a 9d 71 68 ea c4 68 08 a5 7a>,
// iv: <Buffer bf 5e 15 5a 28 94 3b 10 46 31 24 84 60 9c 5f 3c>

val secKey = SecretKeySpec(key, "AES")
val parameterSpec = IvParameterSpec(iv)
val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
cipher.init(Cipher.ENCRYPT_MODE, secKey, parameterSpec)
```

## LICENSE [MIT](LICENSE)
