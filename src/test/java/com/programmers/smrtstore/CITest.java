package com.programmers.smrtstore;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CITest {

    @Test
    void hello() {
        String expect = "hello";
        assertThat(expect).isEqualTo("hello");
    }

    @Test
    void world_fail() {
        String invalid = "wwoooorld";
        assertThat(invalid).isEqualTo("world");
    }
}
