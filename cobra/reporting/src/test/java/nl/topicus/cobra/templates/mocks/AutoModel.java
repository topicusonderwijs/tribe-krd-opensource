package nl.topicus.cobra.templates.mocks;

import nl.topicus.cobra.templates.annotations.Exportable;

@Exportable
public class AutoModel
{
	private String naam;

	private String kleur;

	private Versnellingsbak versnellingsbak;

	public AutoModel(String naam, String kleur, Versnellingsbak versnellingsbak)
	{
		this.naam = naam;
		this.kleur = kleur;
		this.versnellingsbak = versnellingsbak;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	@Exportable
	public String getKleur()
	{
		return kleur;
	}

	@Exportable
	public Versnellingsbak getVersnellingsbak()
	{
		return versnellingsbak;
	}
}
