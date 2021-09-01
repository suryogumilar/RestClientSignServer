package sg.lab.RestClientSignServer.model;

public class ProcessSodResponse {
	private String data;
	private String signerCert;
	public String getSignerCert() {
		return signerCert;
	}
	public void setSignerCert(String signerCert) {
		this.signerCert = signerCert;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
