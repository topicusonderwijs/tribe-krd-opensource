package nl.topicus.cobra.web.components.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleCSVFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String bestandsnaam;

	private final List<String> lines = new ArrayList<String>();

	public SimpleCSVFile(String bestandsnaam, BufferedReader reader) throws IOException
	{
		this.bestandsnaam = bestandsnaam;
		String line;
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public List<String> getLines()
	{
		return lines;
	}
}