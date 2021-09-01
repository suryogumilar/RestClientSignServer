package sg.lab.RestClientSignServer.component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.signserver.clientws.ClientWSService;
import org.signserver.clientws.DataGroup;
import org.signserver.clientws.InternalServerException_Exception;
import org.signserver.clientws.Metadata;
import org.signserver.clientws.RequestFailedException_Exception;
import org.signserver.clientws.SodRequest;
import org.signserver.clientws.SodResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sg.lab.RestClientSignServer.iface.SignServerWsServiceInterface;
import sg.lab.RestClientSignServer.model.ProcessSod;
import sg.lab.RestClientSignServer.model.ProcessSodResponse;

@Service
public class SignServerWsService implements SignServerWsServiceInterface{
	Logger logger = LoggerFactory.getLogger(SignServerWsService.class);
	public ProcessSodResponse sodRequest(ProcessSod pSod) throws MalformedURLException, RequestFailedException_Exception, InternalServerException_Exception {
		URL wsdlLocation = new URL(System.getenv("WSDL_LOC"));
		ClientWSService ws = new ClientWSService(wsdlLocation);
		String worker=System.getenv("WORKER_NAME");
		SodRequest sodData = new SodRequest();
		
		String[] dataGroupList = pSod.getDataGroup();
		int ii = 1;
		for (String dg : dataGroupList) {
			DataGroup dg1 = new DataGroup();
			dg1.setId(ii++);
			dg1.setValue(Base64.decodeBase64(dg));
			sodData.getDataGroup().add(dg1);
		}
		sodData.setUnicodeVersion(System.getenv("UNICODE_VERSION"));
		List<Metadata> metadata = null;
		SodResponse sodResponse = ws.getClientWSPort().processSOD(worker, metadata , sodData);
		
		
		ProcessSodResponse resp = new ProcessSodResponse();
		resp.setData(new String(Base64.encodeBase64(sodResponse.getData())));
		resp.setSignerCert(new String(Base64.encodeBase64(sodResponse.getSignerCertificate())));
		return resp;
	}
}
