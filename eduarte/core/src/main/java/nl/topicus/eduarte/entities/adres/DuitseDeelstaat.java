package nl.topicus.eduarte.entities.adres;

import nl.topicus.cobra.util.StringUtil;

public enum DuitseDeelstaat
{
	Bremen("01"),
	NederSaksen("02"),
	Noord_Rijnland_Westfalen("03"),
	Overig("00");

	private String code;

	@Override
	public String toString()
	{
		return StringUtil.firstCharUppercaseOfEachWord(StringUtil.convertCamelCase(name()))
			.replace('_', '-');
	}

	private DuitseDeelstaat(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public boolean isHOBekostigbaar()
	{
		return this != Overig;
	}

}
