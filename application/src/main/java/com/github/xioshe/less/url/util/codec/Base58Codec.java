package com.github.xioshe.less.url.util.codec;


import java.util.HashMap;
import java.util.Map;

/**
 * Base 62（0-9 a-z A-Z）去掉容易混淆的字符 '0' 'O' 'l' 'I'
 */
public class Base58Codec implements Codec {

    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final Map<Character, Integer> CHAR_VALUES = new HashMap<>();

    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            CHAR_VALUES.put(ALPHABET[i], i);
        }
    }

    private static final int BASE = 58;

    @Override
    public String encode(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non-negative");
        }

        // Special case for 0
        if (id == 0) {
            return "1";
        }

        StringBuilder sb = new StringBuilder();
        long num = id;
        while (num > 0) {
            int remainder = (int) (num % BASE);
            sb.append(ALPHABET[remainder]);
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    @Override
    public long decode(String code) {
        if (code.isEmpty()) {
            return -1;
        }
        long result = 0;
        for (char c : code.toCharArray()) {
            int digit = CHAR_VALUES.getOrDefault(c, -1);
            if (digit == -1) {
                return -1;
            }
            result *= BASE;
            result += digit;
        }
        return result;
    }

}
