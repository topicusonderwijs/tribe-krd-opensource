package nl.topicus.cobra.security.openid.client;

import java.io.Serializable;

import org.openid4java.discovery.DiscoveryInformation;

public class OpenIdLoginModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String openId;

	private String openIdIdentifier;

	private String username;

	private DiscoveryInformation discoveryInformation;

	public static final String DISCOVERY_INFORMATION = "openid-disc";

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getOpenIdIdentifier()
	{
		return openIdIdentifier;
	}

	public void setOpenIdIdentifier(String openIdIdentifier)
	{
		this.openIdIdentifier = openIdIdentifier;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setDiscoveryInformation(DiscoveryInformation discoveryInformation)
	{
		this.discoveryInformation = discoveryInformation;

	}

	public DiscoveryInformation getDiscoveryInformation()
	{
		return discoveryInformation;
	}
}
