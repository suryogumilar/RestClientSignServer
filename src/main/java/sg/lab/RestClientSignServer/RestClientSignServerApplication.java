package sg.lab.RestClientSignServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan("sg.lab.RestClientSignServer")
public class RestClientSignServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestClientSignServerApplication.class, args);
	}

}
