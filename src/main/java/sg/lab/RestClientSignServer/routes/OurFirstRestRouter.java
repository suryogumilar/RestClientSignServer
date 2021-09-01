package sg.lab.RestClientSignServer.routes;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestBindingMode;
import org.signserver.clientws.InternalServerException_Exception;
import org.signserver.clientws.RequestFailedException_Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.lab.RestClientSignServer.component.OurGetCurrentTime;
import sg.lab.RestClientSignServer.iface.SignServerWsServiceInterface;
import sg.lab.RestClientSignServer.model.ProcessSod;
import sg.lab.RestClientSignServer.model.ProcessSodResponse;

@Component
public class OurFirstRestRouter extends RouteBuilder{
	Logger logger = LoggerFactory.getLogger(OurFirstRestRouter.class);
	@Autowired
	OurGetCurrentTime ourGetCurrentTime;
	@Autowired
	SignServerWsServiceInterface signServerWsService;
	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
		.dataFormatProperty("prettyPrint", "true")
		//.contextPath("/singserver/rest/v1")
		//.host("localhost").port(8089)
		;
		
		rest("ClientWSService/processSOD").description("proces sod")
		.consumes("application/json").produces("application/json")
		.post()
		.outType(ProcessSodResponse.class)
		.type(ProcessSod.class)
		.route()//.process(this::getBody)
		.process(this::getProcessSodFromRequestWS)
		//.bean(ourGetCurrentTime,"theGetCurrentTimeMeth")
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
	protected void getBody(Exchange exchange) {
		java.util.LinkedHashMap body = (LinkedHashMap) exchange.getMessage().getBody();
		logger.info("get the body {}", body);
		body.put("response", "good");
		exchange.getMessage().setBody(body);
	}
	protected void getProcessSodFromRequest(Exchange exchange) {
		ProcessSod pod = (ProcessSod)exchange.getMessage().getBody();
		logger.info("pod = "+pod);
		ProcessSodResponse podResp = new ProcessSodResponse();
		podResp.setData("datanya");
		exchange.getMessage().setBody(podResp);
	}
	private void getProcessSodFromRequestWS(Exchange exchange) {
		ProcessSod pod = (ProcessSod)exchange.getMessage().getBody();
		logger.info("pod = "+pod);
		
		try {
			ProcessSodResponse sodRequest = signServerWsService.sodRequest(pod);
			exchange.getMessage().setBody(sodRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			exchange.setException(e);
		} catch (RequestFailedException_Exception e) {
			e.printStackTrace();
			exchange.setException(e);
		} catch (InternalServerException_Exception e) {
			e.printStackTrace();
			exchange.setException(e);
		}
		
	}
}
