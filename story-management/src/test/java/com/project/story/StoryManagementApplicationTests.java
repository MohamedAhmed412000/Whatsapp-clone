package com.project.story;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.grpc.server.enabled=false"
})
class StoryManagementApplicationTests {

    @Test
    void contextLoads() {
    }

}
