package sg.lab.RestClientSignServer.routes;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.signserver.clientws.InternalServerException_Exception;
import org.signserver.clientws.RequestFailedException_Exception;
import javax.wsdl.WSDLException;
import javax.xml.ws.WebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.lab.RestClientSignServer.component.OurGetCurrentTime;
import sg.lab.RestClientSignServer.iface.SignServerWsServiceInterface;
import sg.lab.RestClientSignServer.model.ProcessSod;
import sg.lab.RestClientSignServer.model.ProcessSodException;
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
		restConfiguration().component("servlet")
		//.component("netty4-http")
		.bindingMode(RestBindingMode.json)
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
		Map<String, Object> headers = exchange.getIn().getHeaders();
		String headermap = "\n";
		for(String key : headers.keySet()) {
			headermap+=key+":"+headers.get(key)+"\n";
		}
		logger.info("headermap="+headermap);
		
		ProcessSod pod = (ProcessSod)exchange.getMessage().getBody();
		logger.info("pod = "+pod);
		
		try {
			ProcessSodResponse sodRequest = signServerWsService.sodRequest(pod);
			exchange.getMessage().setBody(sodRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//exchange.setException(e);
			setExceptionMessage(e, exchange);
		} catch (RequestFailedException_Exception e) {
			e.printStackTrace();
			//exchange.setException(e);
			setExceptionMessage(e, exchange);
		} catch (InternalServerException_Exception e) {
			e.printStackTrace();
			//exchange.setException(e);
			setExceptionMessage(e, exchange);
		}catch (ServiceConstructionException e) {
			e.printStackTrace();
			//exchange.setException(e);
			setExceptionMessage(e, exchange);
		}catch(WebServiceException e) {
			e.printStackTrace();
			//exchange.setException(e);
			setExceptionMessage(e, exchange);
		}
		
	}
	private void setExceptionMessage(Exception e, Exchange exchange) {
		Throwable cause = e.getCause();
		ProcessSodException soe = new ProcessSodException();
		soe.setFaultString(e.getMessage());
		soe.setCauseString("");
		if(cause!=null) {
			if(cause instanceof ServiceConstructionException) {
				ServiceConstructionException sce = (ServiceConstructionException)cause;
				if(null != sce.getCause() && sce.getCause() instanceof WebServiceException) {
					WebServiceException wse = (WebServiceException) sce.getCause();
					if(null != wse.getCause() && wse.getCause() instanceof WSDLException) {
						WSDLException wdslexc = (WSDLException) wse.getCause();
						soe.setCauseString(wdslexc.getMessage());
					}else {
						soe.setCauseString(wse.getMessage());						
					}
				}else if(null != sce.getCause() && sce.getCause() instanceof WSDLException){
					WSDLException wdslexc = (WSDLException) sce.getCause();
					soe.setCauseString(wdslexc.getMessage());
				}else {
					soe.setCauseString(sce.getMessage());
				}
				
			}else if(cause instanceof RequestFailedException_Exception){
				RequestFailedException_Exception rfe = (RequestFailedException_Exception)cause;
				soe.setCauseString(rfe.getMessage());
			}else {
				soe.setCauseString(cause.getMessage());
			}
		}
		exchange.getMessage().setBody(soe);
	}
}
