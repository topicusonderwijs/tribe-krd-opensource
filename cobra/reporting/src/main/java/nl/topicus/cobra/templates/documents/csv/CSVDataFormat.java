package nl.topicus.cobra.templates.documents.csv;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormat;

/**
 * Dataformat-tabel voor het omzetten van een gebruikt datum- of getalformaat pattern naar
 * een index. Gedraagt zich static, maar moet vanwege de DataFormat-interface
 * geinstantieerd worden.
 * 
 * @author hop
 */
public class CSVDataFormat implements DataFormat
{
	private static List<String> formattingPatterns = new ArrayList<String>();

	/**
	 * Zet datum- of getalformaat pattern om naar index; voegt deze toe indien de lijst
	 * dit pattern nog niet bevatte.
	 */
	@Override
	public synchronized short getFormat(String pattern)
	{
		if (!formattingPatterns.contains(pattern))
			formattingPatterns.add(pattern);

		return (short) formattingPatterns.indexOf(pattern);
	}

	/**
	 * Zet index om naar het pattern dat erbij hoort.
	 * 
	 * @return element met gegeven index. Null indien niet gevonden.
	 */
	@Override
	public synchronized String getFormat(short index)
	{
		if (index >= 0 && index < formattingPatterns.size())
			return formattingPatterns.get(index);

		return null;
	}
}
