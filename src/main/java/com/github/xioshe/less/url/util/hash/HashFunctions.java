package com.github.xioshe.less.url.util.hash;

import java.nio.charset.StandardCharsets;

public class HashFunctions {

    public static final HashFunction MURMUR3 = new Murmur3HashFunction();

    public static final HashFunction CRC32 = new Crc32HashFunction();

    static final class Murmur3HashFunction implements HashFunction {
        private final com.google.common.hash.HashFunction murmurHash3 = com.google.common.hash.Hashing.murmur3_32_fixed();

        @Override
        public long hash(String str) {
            return murmurHash3.hashString(str, StandardCharsets.UTF_8).padToLong();
        }
    }

    static final class Crc32HashFunction implements HashFunction {
        private final com.google.common.hash.HashFunction crc32c = com.google.common.hash.Hashing.crc32c();

        @Override
        public long hash(String str) {
            return crc32c.hashString(str, StandardCharsets.UTF_8).padToLong();
        }
    }
}
