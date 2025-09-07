package project.project_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import project.project_spring.auth.web.controller.AuthControllerTest;

@SpringBootTest(classes = AuthControllerTest.class)
class ProjectSpringApplicationTests {

    @Test
    void contextLoads() {
    }

}
