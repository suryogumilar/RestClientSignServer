package sg.lab.RestClientSignServer.routes;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.lab.RestClientSignServer.component.OurGetCurrentTime;

@Component
public class OurFirstRestRouter extends RouteBuilder{
	Logger logger = LoggerFactory.getLogger(OurFirstRestRouter.class);
	@Autowired
	OurGetCurrentTime ourGetCurrentTime;
	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
		.dataFormatProperty("prettyPrint", "true")
		//.contextPath("/singserver/rest/v1")
		//.host("localhost").port(8089)
		;
		
		rest("ClientWSService/processSOD").description("proces sod")
		.consumes("application/json").produces("application/json")
		.post().route().process(this::getBody).bean(ourGetCurrentTime,"theGetCurrentTimeMeth")
		.to("log:our-First_rest-log");
		
		/* post ga bisa
		from("rest:post:ClientWSService/processSOD?produces=application/json&consumes=application/json")
		.log("1. ${body}")
		.process(this::getBody)
		.bean(ourGetCurrentTime,"theGetCurrentTimeMeth")
		.log("2. ${body}")
		.to("log:our-First_rest-log");
		*/
	}
	private void getBody(Exchange exchange) {
		java.util.LinkedHashMap body = (LinkedHashMap) exchange.getMessage().getBody();
		logger.info("get the body {}", body);
		body.put("response", "good");
		exchange.getMessage().setBody(body);
	}
}
