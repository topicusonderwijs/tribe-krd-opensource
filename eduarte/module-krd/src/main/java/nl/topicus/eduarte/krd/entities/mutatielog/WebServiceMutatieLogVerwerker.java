package nl.topicus.eduarte.krd.entities.mutatielog;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.ws.Service;

import nl.topicus.eduarte.util.IWebServiceClientContext;
import nl.topicus.eduarte.util.WsUtil;

@Entity
public abstract class WebServiceMutatieLogVerwerker<T> extends MutatieLogVerwerker
{
	private static final long serialVersionUID = 1L;

	@Transient
	private transient T remoteService;

	protected <Z extends Service> T getRemoteService(Class<Z> clz, Class<T> endPointInterface,
			IWebServiceClientContext webserviceContext)
	{
		if (remoteService == null)
		{
			remoteService =
				WsUtil.getRemoteService(clz, endPointInterface, webserviceContext,
					getWsdlLocation());
		}

		return remoteService;
	}

	/**
	 * Geeft de WSDL locatie op het classpath (of remote terug).
	 */
	protected abstract URL getWsdlLocation();
}
