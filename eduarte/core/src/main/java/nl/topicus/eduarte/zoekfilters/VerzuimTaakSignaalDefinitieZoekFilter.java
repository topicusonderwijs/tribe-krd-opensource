package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;
import nl.topicus.eduarte.rapportage.leerplicht.SoortLeerplichtDeelnemer;

/**
 * 
 */
public class VerzuimTaakSignaalDefinitieZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<VerzuimTaakSignaalDefinitie>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisZoekFilter deelnemerFilter;

	@AutoForm(label = "Soort Deelnemers", editorClass = EnumCombobox.class)
	private SoortLeerplichtDeelnemer soortDeelnemer;

	@AutoForm(htmlClasses = "unit_40", label = "Aantal uren afwezig ")
	private int aantalklokuren;

	@AutoForm(htmlClasses = "unit_40", label = "gedurende de laatste ")
	private int aantalWeken;

	@AutoForm(htmlClasses = "unit_40", label = "of aantal Weken achtereenvolgend afwezig ")
	private int aantalWekenAchtereenvolgendAfwezig;

	@AutoForm(label = " Weken. Alleen ongeoorloofd verzuim ")
	private boolean ongeoorlooft;

	public VerzuimTaakSignaalDefinitieZoekFilter()
	{
	}

	@AutoForm(label = "", readOnly = true, htmlClasses = "clear laySeparator10")
	public String getInfo()
	{
		return "";
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
