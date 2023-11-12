package evveryday.evveryday;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableAdminServer
@EnableJpaAuditing
@SpringBootApplication
public class EvverydayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvverydayApplication.class, args);
	}

}
