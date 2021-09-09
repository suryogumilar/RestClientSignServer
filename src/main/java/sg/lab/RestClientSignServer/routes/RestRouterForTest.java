package sg.lab.RestClientSignServer.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import sg.lab.RestClientSignServer.model.TestHelloResponse;

@Component
public class RestRouterForTest extends RouteBuilder{
	Logger logger = LoggerFactory.getLogger(OurFirstRestRouter.class);

	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
		.dataFormatProperty("prettyPrint", "true");
		
		rest("ClientWSService/testhello").description("proces sod")
		.consumes("application/json").produces("application/json")
		.get().outType(TestHelloResponse.class).route()
		.process(this::justHello)
		.to("log:rest-test-log");;
		
	}
	
	private void justHello(Exchange exchange) {
		Object body = exchange.getMessage().getBody();
		TestHelloResponse helloResponse = new TestHelloResponse();
		helloResponse.setHello(""+body);
		exchange.getMessage().setBody(helloResponse);
	}
}
