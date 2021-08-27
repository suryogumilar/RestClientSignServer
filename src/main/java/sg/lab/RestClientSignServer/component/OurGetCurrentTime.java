package sg.lab.RestClientSignServer.component;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class OurGetCurrentTime {

	public String theGetCurrentTimeMeth() {
		return "Our time is "+LocalDateTime.now();
	}
}
