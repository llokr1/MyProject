package project.project_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSpringApplication.class, args);
	}

}
