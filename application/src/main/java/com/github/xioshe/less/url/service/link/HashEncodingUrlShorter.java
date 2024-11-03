package com.github.xioshe.less.url.service.link;

import com.github.xioshe.less.url.util.codec.Encoder;
import com.github.xioshe.less.url.util.hash.HashFunction;
import com.google.common.base.Strings;


public class HashEncodingUrlShorter implements UrlShorter {

    private final Encoder encoder;
    private final HashFunction hashFunction;

    public HashEncodingUrlShorter(Encoder encoder, HashFunction hashFunction) {
        this.encoder = encoder;
        this.hashFunction = hashFunction;
    }

    @Override
    public String shorten(String originalUrl) {
        if (Strings.isNullOrEmpty(originalUrl)) {
            throw new IllegalArgumentException("originalUrl cannot be null or empty");
        }
        long hash = hashFunction.hash(originalUrl);
        String encodedUrl = encoder.encode(hash);
        return encodedUrl.length() > 6 ? encodedUrl.substring(0, 6) : encodedUrl;
    }
}
