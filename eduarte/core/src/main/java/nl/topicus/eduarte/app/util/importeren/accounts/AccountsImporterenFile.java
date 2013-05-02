package nl.topicus.eduarte.app.util.importeren.accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Accounts kunnen in Soundscape geimporteerd worden vanuit een csv-bestand. Dit
 * csv-bestand moet het volgende formaat hebben:
 * 
 * <pre>
 * Id;Gebruikersnaam;Wachtwoord;Email;Rollen
 * ... data ...
 * </pre>
 * 
 * @author loite
 */
public class AccountsImporterenFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String bestandsNaam;

	private final List<String> lines = new ArrayList<String>();

	/**
	 * @param reader
	 * @param naam
	 * @throws IOException
	 */
	public AccountsImporterenFile(BufferedReader reader, String naam) throws IOException
	{
		this.bestandsNaam = naam;
		String line = reader.readLine();
		// controleer dat dit een Untis header is.
		if (!(line.equalsIgnoreCase("Id;Gebruikersnaam;Wachtwoord;Email;Rollen")))
		{
			throw new IllegalArgumentException(
				"Het bestand werd niet herkend als een geldig account importbestand.");
		}
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public String getBestandsNaam()
	{
		return bestandsNaam;
	}

	public List<String> getLines()
	{
		return lines;
	}
}
