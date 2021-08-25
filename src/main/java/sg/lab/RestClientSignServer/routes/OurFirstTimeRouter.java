package sg.lab.RestClientSignServer.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OurFirstTimeRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("timer:our-first-timer")
		.transform().constant("This is Our Message")
		.to("log:our-first-timer-log");
	}

}
