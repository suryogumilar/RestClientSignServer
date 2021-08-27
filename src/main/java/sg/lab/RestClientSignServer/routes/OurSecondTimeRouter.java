package sg.lab.RestClientSignServer.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.lab.RestClientSignServer.component.OurGetCurrentTime;

//@Component
public class OurSecondTimeRouter extends RouteBuilder{
	@Autowired
	OurGetCurrentTime ourGetCurrentTime;
	
	@Override
	public void configure() throws Exception {
		from("timer:our-second-timer?period=10000")
		.bean(ourGetCurrentTime,"theGetCurrentTimeMeth")
		.to("log:our-second-timer-log");
	}
}
