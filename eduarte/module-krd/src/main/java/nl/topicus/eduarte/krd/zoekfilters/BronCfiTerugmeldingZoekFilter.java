package nl.topicus.eduarte.krd.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding.BronCFIStatus;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BronCfiTerugmeldingZoekFilter extends AbstractZoekFilter<BronCfiTerugmelding>
{
	private static final long serialVersionUID = 1L;

	private String bestandsnaam;

	private BronCFIStatus status;

	private Date peildatumInBestand;

	@AutoForm(htmlClasses = "unit_160")
	private IModel<Medewerker> ingelezenDoor;

	public BronCfiTerugmeldingZoekFilter()
	{
		addOrderByProperty("inleesdatum");
		setAscending(false);
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public Medewerker getIngelezenDoor()
	{
		return getModelObject(ingelezenDoor);
	}

	public void setIngelezenDoor(Medewerker ingelezenDoor)
	{
		this.ingelezenDoor = makeModelFor(ingelezenDoor);
	}

	public void setStatus(BronCFIStatus status)
	{
		this.status = status;
	}

	public BronCFIStatus getStatus()
	{
		return status;
	}

	public void setPeildatumInBestand(Date peildatumInBestand)
	{
		this.peildatumInBestand = peildatumInBestand;
	}

	public Date getPeildatumInBestand()
	{
		return peildatumInBestand;
	}

}
