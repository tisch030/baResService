package companyx.ResourceServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ResourceServerApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

}
