package nl.topicus.eduarte.app.util.importeren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;

public abstract class AbstractCsvImporterenFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final List<String> lines = new ArrayList<String>();

	private List<String> meldingen;

	private int aantalVerwerkt = 0;

	private int aantalToegevoegd = 0;

	private int aantalOngeldigeRegels = 0;

	private int aantalBestaatAl = 0;

	private int regelnummer;

	public AbstractCsvImporterenFile()
	{
	}

	public void readLines(BufferedReader reader) throws IOException
	{
		// header
		// String line = reader.readLine();
		String line;
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public void importeer()
	{
		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		meldingen = new ArrayList<String>();
		regelnummer = 1;
		for (String line : lines)
		{
			String[] velden = line.split(";", -1);
			aantalVerwerkt++;
			if (validateVelden(velden, regelnummer))
			{
				aantalToegevoegd++;
				verwerkLine(velden);
			}
			regelnummer++;
			if (regelnummer % 50 == 0)
			{
				// Doe een flush en een clear.
				sessionHelper.getHibernateSessionProvider().getSession().flush();
				sessionHelper.clearSession();
			}
		}
	}

	public List<String> getMeldingen()
	{
		return meldingen;
	}

	public List<String> getAlleResultaten()
	{
		List<String> alleResultaten = new ArrayList<String>();
		alleResultaten.add("Aantal regels verwerkt: " + aantalVerwerkt);
		alleResultaten.add("Aantal nieuwe aangemaakt: " + aantalToegevoegd);
		alleResultaten.add("Aantal regels die al bestonden " + aantalBestaatAl);
		alleResultaten.add("Aantal ongeldige regels: " + aantalOngeldigeRegels);
		alleResultaten.addAll(getMeldingen());
		return alleResultaten;
	}

	public void voegOngeldigeMeldingToe(String msg)
	{
		aantalOngeldigeRegels++;
		meldingen.add(msg);
	}

	public void voegBestaatAlMeldingToe(String msg)
	{
		aantalBestaatAl++;
		meldingen.add(msg);
	}

	protected abstract void verwerkLine(String[] velden);

	protected abstract boolean validateVelden(String[] velden, int regel);
}