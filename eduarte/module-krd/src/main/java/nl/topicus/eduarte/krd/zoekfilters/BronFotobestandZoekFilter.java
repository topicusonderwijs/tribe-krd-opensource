package nl.topicus.eduarte.krd.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand.BronFotoStatus;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand.BronFotoVerwerkingsstatus;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class BronFotobestandZoekFilter extends AbstractZoekFilter<BronFotobestand>
{
	private static final long serialVersionUID = 1L;

	private String bestandsnaam;

	private BronFotoStatus status;

	private BronFotoVerwerkingsstatus verwerkingsstatus;

	private BronFotoType type;

	private Date peildatumInFoto;

	@AutoForm(htmlClasses = "unit_160")
	private IModel<Medewerker> ingelezenDoor;

	public BronFotobestandZoekFilter()
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

	public BronFotoStatus getStatus()
	{
		return status;
	}

	public void setStatus(BronFotoStatus status)
	{
		this.status = status;
	}

	public BronFotoVerwerkingsstatus getVerwerkingsstatus()
	{
		return verwerkingsstatus;
	}

	public void setVerwerkingsstatus(BronFotoVerwerkingsstatus verwerkingsstatus)
	{
		this.verwerkingsstatus = verwerkingsstatus;
	}

	public BronFotoType getType()
	{
		return type;
	}

	public void setType(BronFotoType type)
	{
		this.type = type;
	}

	public Medewerker getIngelezenDoor()
	{
		return getModelObject(ingelezenDoor);
	}

	public void setIngelezenDoor(Medewerker ingelezenDoor)
	{
		this.ingelezenDoor = makeModelFor(ingelezenDoor);
	}

	public Date getPeildatumInFoto()
	{
		return peildatumInFoto;
	}

	public void setPeildatumInFoto(Date peildatumInFoto)
	{
		this.peildatumInFoto = peildatumInFoto;
	}

}
