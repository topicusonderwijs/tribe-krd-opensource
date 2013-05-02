package nl.topicus.eduarte.util;

import static nl.topicus.cobra.util.StringUtil.*;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import nl.topicus.eduarte.entities.Entiteit;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

/**
 * Generieke utility voor gebruik in webservice gerelateerde classes.
 */
public class WsUtil
{
	public static <T extends Entiteit> T getElementWithId(Class<T> clz, Collection<T> list, Long id)
	{
		if (id != null)
		{
			for (T t : list)
			{
				if (id.equals(t.getId()))
					return t;
			}
			throw new WebServiceException("Onbekende " + clz.getSimpleName().toLowerCase()
				+ " id: " + id);
		}
		return null;
	}

	/**
	 * Bepaalt en configureert een webservice client op basis van de webserviceContext en
	 * de wsdlLocation. Deze methode heeft een aantal features:
	 * <ul>
	 * <li>flexibel endpoint address: bepaalt de endpoint address van de webservice aan de
	 * hand van de applicatie configuratie. Hierdoor kan er gewisseld worden van endpoint
	 * address door een URL aan te passen in de applicatie.</li>
	 * <li>https: Zorgt ervoor dat als er <tt>https</tt> gebruikt wordt, er ook van een
	 * SSL transport gebruikgemaakt wordt.</li>
	 * </ul>
	 */
	public static <T, Z extends Service> T getRemoteService(Class<Z> serviceClass,
			Class<T> portClass, IWebServiceClientContext webserviceContext, URL wsdlLocation)
	{
		QName serviceName = webserviceContext.getServiceName();
		if (serviceName == null)
		{
			throw new IllegalStateException("Webservice " + serviceClass.getName()
				+ " is niet goed geconfigureerd: de serviceName kon niet bepaald worden");
		}
		URL endpointAddress = webserviceContext.getEndpointAddress();
		if (endpointAddress == null)
		{
			throw new IllegalStateException("Webservice " + serviceClass.getName()
				+ " is niet goed geconfigureerd: de endpointAddress is niet ingevuld");
		}
		String address = endpointAddress.toString();
		if (isEmpty(address))
		{
			throw new IllegalStateException("Webservice " + serviceClass.getName()
				+ " is niet goed geconfigureerd: de endpointAddress is niet ingevuld");
		}

		T remoteService = null;
		try
		{
			Constructor<Z> constructor = serviceClass.getConstructor(URL.class, QName.class);
			Z instance = constructor.newInstance(wsdlLocation, serviceName);
			remoteService = instance.getPort(portClass);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		Client client = ClientProxy.getClient(remoteService);
		EndpointReferenceType refType = new EndpointReferenceType();
		AttributedURIType addressType = new AttributedURIType();
		addressType.setValue(address);
		refType.setAddress(addressType);

		Endpoint cxfEndpoint = client.getEndpoint();

		cxfEndpoint.getEndpointInfo().setAddress(refType);
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		conduit.getTarget().setAddress(addressType);

		BindingProvider provider = (BindingProvider) remoteService;
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);

		if (address.startsWith("https"))
		{
			TLSClientParameters tlsParams = new TLSClientParameters();
			tlsParams.setSecureSocketProtocol("SSL");
			conduit.setTlsClientParameters(tlsParams);
		}

		return remoteService;
	}
}
