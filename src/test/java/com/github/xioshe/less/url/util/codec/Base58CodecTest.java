package com.github.xioshe.less.url.util.codec;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Base58CodecTest {

    Base58Codec encoder = new Base58Codec();

    @Test
    void Encode_single_character() {
        // given
        char[] alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

        for (int i = 0; i < alphabet.length; i++) {
            String encoded = encoder.encode(i);
            assertThat(encoded.length()).isEqualTo(1);
            assertEquals(alphabet[i], encoded.charAt(0));
        }
    }

    @Test
    public void Encode_zero() {
        // given
        long id = 0;

        // when
        String encoded = encoder.encode(id);

        // then
        assertEquals("1", encoded);
    }

    @Test
    public void Encode_positive_number() {
        // given
        long id = 12345;

        // when
        String encoded = encoder.encode(id);

        // then
        assertEquals("4fr", encoded);
    }

    @Test
    public void Encode_large_number() {
        // given
        long id = 2468135791013L;

        // when
        String encoded = encoder.encode(id);

        // then
        assertEquals("27qMi57J", encoded);
    }

    @Test
    void Encode_negative_number() {
        // given
        long id = -1;

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            encoder.encode(id);
        });

        // then
        assertEquals("id must be non-negative", exception.getMessage());
    }

    @Test
    void Decode_single_character() {
        // given
        char[] alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

        for (int i = 0; i < alphabet.length; i++) {
            String code = String.valueOf(alphabet[i]);
            // when
            long decoded = encoder.decode(code);
            // then
            assertEquals(i, decoded);
        }
    }

    @Test
    void Decode_one() {
        // given
        String code = "1";
        long expected = 0;

        // when
        long decoded = encoder.decode(code);

        // then
        assertEquals(expected, decoded);
    }

    @Test
    void Decode_string() {
        assertThat(encoder.decode("4fr")).isEqualTo(12345);
        assertThat(encoder.decode("27qMi57J")).isEqualTo(2468135791013L);
    }

}