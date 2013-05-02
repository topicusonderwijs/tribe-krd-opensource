package nl.topicus.eduarte.util;

/*
 * 
 * Util die checkt of een IP adres voorkomt in een comma separated string met adressen.
 * 
 * Er wordt ook gecheckt op network adressen: als '192.168.1.0' een allowed IP is dan 
 * remoteAddress '192.168.1.50' ook toegestaan.
 *   
 */
public class IpAddressAuthorizationUtil
{
	private String allowedAddressesCommaSeparated;

	private String remoteAddress;

	public IpAddressAuthorizationUtil(String allowedAddressesCommaSeparated, String remoteAddress)
	{
		this.allowedAddressesCommaSeparated = allowedAddressesCommaSeparated;
		this.remoteAddress = remoteAddress;
	}

	public boolean isAuthorized()
	{
		// TODO Als autorisatie op IP niveau nog verder wordt uitgebreid is het misschien
		// handig om dit met minder String magic te doen..

		String[] allowedAddresses = allowedAddressesCommaSeparated.split(",", -1);

		for (String allowedAddress : allowedAddresses)
		{
			allowedAddress = allowedAddress.trim();

			String[] allowedAddressSplit = allowedAddress.split("\\.", -1);

			if (allowedAddressSplit[allowedAddressSplit.length - 1].equals("0"))
			{
				// IPv6 compliant!
				if (remoteAddress.lastIndexOf(".") == -1)
					return false;

				String remoteNetworkAddress =
					remoteAddress.substring(0, remoteAddress.lastIndexOf(".")) + ".0";

				if (remoteNetworkAddress.equals(allowedAddress))
					return true;
			}
			else if (allowedAddress.equals(remoteAddress))
			{
				return true;
			}
		}

		return false;
	}
}
