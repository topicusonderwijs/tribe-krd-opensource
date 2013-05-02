package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.rapportage.leerplicht.SoortLeerplichtDeelnemer;

public class LeerplichtRapportageZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisZoekFilter deelnemerFilter;

	private String signaalNaam;

	private String omschrijving;

	@AutoForm(label = "Soort deelnemers", editorClass = EnumCombobox.class)
	private SoortLeerplichtDeelnemer soortDeelnemer;

	@AutoForm(htmlClasses = "unit_40", label = "Aantal klokuren ")
	private int aantalklokuren;

	@AutoForm(htmlClasses = "unit_40", label = "Aantal weken ")
	private int aantalWeken;

	@AutoForm(htmlClasses = "unit_40", label = "Aantal weken achtereenvolgend ")
	private int aantalWekenAchtereenvolgendAfwezig;

	@AutoForm(label = " ongeoorloofd ")
	private boolean ongeoorlooft;

	public LeerplichtRapportageZoekFilter()
	{
	}

	public void setSignaalNaam(String signaalNaam)
	{
		this.signaalNaam = signaalNaam;
	}

	public String getSignaalNaam()
	{
		return signaalNaam;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public int getAantalWeken()
	{
		return aantalWeken;
	}

	public void setAantalWeken(int aantalWeken)
	{
		this.aantalWeken = aantalWeken;
	}

	public VerbintenisZoekFilter getDeelnemerFilter()
	{
		return deelnemerFilter;
	}

	public void setDeelnemerFilter(VerbintenisZoekFilter deelnemerFilter)
	{
		this.deelnemerFilter = deelnemerFilter;
	}

	public void setSoortDeelnemer(SoortLeerplichtDeelnemer soortDeelnemer)
	{
		this.soortDeelnemer = soortDeelnemer;
	}

	public SoortLeerplichtDeelnemer getSoortDeelnemer()
	{
		return soortDeelnemer;
	}

	public void setAantalklokuren(int aantalklokuren)
	{
		this.aantalklokuren = aantalklokuren;
	}

	public int getAantalklokuren()
	{
		return aantalklokuren;
	}

	public void setOngeoorlooft(boolean ongeoorlooft)
	{
		this.ongeoorlooft = ongeoorlooft;
	}

	public boolean isOngeoorlooft()
	{
		return ongeoorlooft;
	}

	public void setAantalWekenAchtereenvolgendAfwezig(int aantalWekenAchtereenvolgendAfwezig)
	{
		this.aantalWekenAchtereenvolgendAfwezig = aantalWekenAchtereenvolgendAfwezig;
	}

	public int getAantalWekenAchtereenvolgendAfwezig()
	{
		return aantalWekenAchtereenvolgendAfwezig;
	}
}
