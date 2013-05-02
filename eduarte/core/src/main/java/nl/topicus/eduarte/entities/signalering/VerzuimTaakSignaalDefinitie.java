package nl.topicus.eduarte.entities.signalering;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;
import nl.topicus.eduarte.rapportage.leerplicht.SoortLeerplichtDeelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VerzuimTaakSignaalDefinitie extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Naam")
	private String signaalNaam;

	private String omschrijving;

	@AutoForm(label = "Soort deelnemers", editorClass = EnumCombobox.class)
	@Enumerated(EnumType.STRING)
	private SoortLeerplichtDeelnemer soortDeelnemer;

	@AutoForm(htmlClasses = "unit_40", label = "Aantal uren afwezig ")
	private int aantalklokuren;

	@AutoForm(htmlClasses = "unit_40", label = "gedurende de laatste ")
	private int aantalWeken;

	@AutoForm(htmlClasses = "unit_40", label = "of aantal weken achtereenvolgend afwezig ")
	private int aantalWekenAanEen;

	@AutoForm(label = " Weken. Alleen ongeoorloofd verzuim ")
	private boolean ongeoorlooft;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "signaalDefinitie")
	@AutoForm(include = false)
	private List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> koppelingen;

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

	public void setAantalWeken(int aantalWeken)
	{
		this.aantalWeken = aantalWeken;
	}

	public int getAantalWeken()
	{
		return aantalWeken;
	}

	public void setAantalWekenAanEen(int aantalWekenAchtereenvolgendAfwezig)
	{
		this.aantalWekenAanEen = aantalWekenAchtereenvolgendAfwezig;
	}

	public int getAantalWekenAanEen()
	{
		return aantalWekenAanEen;
	}

	public void setOngeoorlooft(boolean ongeoorlooft)
	{
		this.ongeoorlooft = ongeoorlooft;
	}

	public boolean isOngeoorlooft()
	{
		return ongeoorlooft;
	}

	public void setKoppelingen(
			List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> koppelingen)
	{
		this.koppelingen = koppelingen;
	}

	public List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> getKoppelingen()
	{
		return koppelingen;
	}

	@Override
	public String toString()
	{
		return signaalNaam;
	}

}
