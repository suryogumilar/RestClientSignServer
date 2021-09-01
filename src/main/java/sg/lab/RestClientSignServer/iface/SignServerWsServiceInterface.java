package sg.lab.RestClientSignServer.iface;

import java.net.MalformedURLException;

import org.signserver.clientws.InternalServerException_Exception;
import org.signserver.clientws.RequestFailedException_Exception;

import sg.lab.RestClientSignServer.model.ProcessSod;
import sg.lab.RestClientSignServer.model.ProcessSodResponse;

public interface SignServerWsServiceInterface {

	ProcessSodResponse sodRequest(ProcessSod pSod)
			throws MalformedURLException, RequestFailedException_Exception, InternalServerException_Exception;

}
