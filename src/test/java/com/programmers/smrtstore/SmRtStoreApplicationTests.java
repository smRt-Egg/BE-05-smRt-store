package com.programmers.smrtstore;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(RedisTestConfig.class)
class SmRtStoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
