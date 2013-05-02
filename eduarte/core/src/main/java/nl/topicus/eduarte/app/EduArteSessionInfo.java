package nl.topicus.eduarte.app;

import nl.topicus.cobra.entities.AdministratiePakket;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;

/**
 * SessionInfo object voor gebruik in de
 * {@link org.apache.wicket.protocol.http.RequestLogger}.
 */
public class EduArteSessionInfo
{
	private static final String ONBEKEND = "onbekend";

	private static final long serialVersionUID = 1L;

	/**
	 * Moeten zelf de sessionId nog loggen, aangezien Wicket dat niet doet als er
	 * SessionInfo aanwezig is.
	 */
	private String sessionId;

	private final WebClientInfo clientInfo;

	private String gebruiker = ONBEKEND;

	private String organisatie = ONBEKEND;

	public EduArteSessionInfo(WebSession session, WebClientInfo clientInfo)
	{
		this.sessionId = session.getId();
		this.clientInfo = clientInfo;
	}

	public void setGebruiker(String gebruiker)
	{
		this.gebruiker = gebruiker;
	}

	public void setOrganisatie(String organisatie)
	{
		this.organisatie = organisatie;
	}

	private String[][] values()
	{
		String[][] values =
			{getApplicatie(), getVersie(), getSessionId(), getClientInfo(), getOrganisatie(),
				getGebruikersnaam(),};

		return values;
	}

	private String[] getApplicatie()
	{
		return new String[] {"applicatie", AdministratiePakket.getPakket().getNaam()};
	}

	private String[] getVersie()
	{
		return new String[] {"versie", AdministratiePakket.getPakket().getVersie()};
	}

	private String[] getClientInfo()
	{
		return new String[] {"clientInfo", getSanitizedClientInfo()};
	}

	/**
	 * Geeft de client info terug als een beter leesbare string met meer relevante
	 * informatie, zoals de user agent.
	 */
	private String getSanitizedClientInfo()
	{
		ClientProperties p = clientInfo.getProperties();
		StringBuilder sb = new StringBuilder("[");
		sb.append("browser=");
		if (p.isBrowserKonqueror())
			sb.append("konquerer");
		else if (p.isBrowserMozillaFirefox())
			sb.append("firefox");
		else if (p.isBrowserMozilla())
			sb.append("mozilla");
		else if (p.isBrowserOpera())
			sb.append("opera");
		else if (p.isBrowserSafari())
			sb.append("safari");
		else if (p.isBrowserInternetExplorer())
			sb.append("msie");
		else
			sb.append("other");

		if (p.getBrowserVersionMajor() >= 0)
		{
			sb.append(",version=").append(p.getBrowserVersionMajor());
			if (p.getBrowserVersionMinor() >= 0)
			{
				sb.append(".").append(p.getBrowserVersionMinor());
			}
		}

		if (p.getRemoteAddress() != null)
			sb.append(",remoteAddres=").append(p.getRemoteAddress());

		sb.append(",userAgent='").append(clientInfo.getUserAgent()).append("'");
		sb.append("]");
		return sb.toString();
	}

	private String[] getOrganisatie()
	{
		return new String[] {"organisatie", organisatie};
	}

	private String[] getGebruikersnaam()
	{
		return new String[] {"username", gebruiker};
	}

	private String[] getSessionId()
	{
		if (sessionId == null)
		{
			Session session = Session.get();
			if (session != null)
				sessionId = session.getId();
		}
		return new String[] {"sessionId", sessionId};
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[");
		String[][] values = values();
		String komma = "";
		for (String[] value : values)
		{
			sb.append(komma);
			sb.append(value[0]);
			sb.append("=");
			sb.append(value[1]);
			komma = ",";
		}
		sb.append("]");
		return sb.toString();
	}
}
