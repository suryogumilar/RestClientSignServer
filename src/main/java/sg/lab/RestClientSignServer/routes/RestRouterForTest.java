package sg.lab.RestClientSignServer.routes;

import java.util.Map;

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
		restConfiguration()
		//.component("netty4-http")
		.component("servlet")
		.bindingMode(RestBindingMode.json)
		.dataFormatProperty("prettyPrint", "true");
		
		rest("ClientWSService/testhello").description("proces sod")
		.consumes("application/json").produces("application/json")
		.get().outType(TestHelloResponse.class).route()
		.process(this::justHello)
		.to("log:rest-test-log");;
		
	}
	
	private void justHello(Exchange exchange) {
		Map<String, Object> headers = exchange.getIn().getHeaders();
		String headermap = "\n";
		for(String key : headers.keySet()) {
			headermap+=key+":"+headers.get(key)+"\n";
		}
		logger.info("headermap="+headermap);
		
		Object body = exchange.getMessage().getBody();
		TestHelloResponse helloResponse = new TestHelloResponse();
		helloResponse.setHello(""+body);
		exchange.getMessage().setBody(helloResponse);
	}
}
