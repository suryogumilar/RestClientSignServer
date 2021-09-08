package sg.lab.RestClientSignServer.model;

public class ProcessSodException {
	private String faultString;
	private String causeString;

	public String getFaultString() {
		return faultString;
	}

	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}

	public String getCauseString() {
		return causeString;
	}

	public void setCauseString(String causeString) {
		this.causeString = causeString;
	}
}
