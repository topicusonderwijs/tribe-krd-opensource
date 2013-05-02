package nl.topicus.cobra.templates.mocks;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.inwords.Dutch;

@Exportable
public class MockDeelnemer
{
	private String naam;

	private String straat;

	private Integer huisnummer;

	private String postcode;

	private String woonplaats;

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getStraat()
	{
		return straat;
	}

	public void setStraat(String straat)
	{
		this.straat = straat;
	}

	@Exportable
	public Integer getHuisnummer()
	{
		return huisnummer;
	}

	public void setHuisnummer(Integer huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	@Exportable
	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	@Exportable
	public String getWoonplaats()
	{
		return woonplaats;
	}

	public void setWoonplaats(String woonplaats)
	{
		this.woonplaats = woonplaats;
	}

	public static List<MockDeelnemer> createDeelnemerList(int aantal)
	{
		Dutch dutch = new Dutch();
		List<MockDeelnemer> result = new ArrayList<MockDeelnemer>();
		String aantalInWords = StringUtil.firstCharUppercase(dutch.toWords(aantal));
		for (int i = 1; i <= aantal; i++)
		{
			MockDeelnemer deelnemer = new MockDeelnemer();
			String inWords = StringUtil.firstCharUppercase(dutch.toWords(i));
			deelnemer.setNaam(inWords + "\tvan " + aantalInWords);
			deelnemer.setStraat(inWords + "\r\nstraat");
			deelnemer.setHuisnummer(i);
			deelnemer.setPostcode("" + (1000 + i) + "AB");
			deelnemer.setWoonplaats(inWords + "\ndorp");
			result.add(deelnemer);
		}
		return result;
	}
}
