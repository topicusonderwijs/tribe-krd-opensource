package nl.topicus.eduarte.krd.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil.FotoVerschil;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class BronFotobestandVerschilZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<BronFotobestandVerschil>
{
	private static final long serialVersionUID = 1L;

	private IModel<BronFotobestand> fotobestand;

	private FotoVerschil verschil;

	private String achternaam;

	private Long pgn;

	@AutoForm(htmlClasses = "unit_150")
	private IModel<Opleiding> opleiding;

	private Date teldatum;

	public BronFotobestandVerschilZoekFilter(BronFotobestand fotobestand)
	{
		setFotobestand(fotobestand);
	}

	public BronFotobestand getFotobestand()
	{
		return getModelObject(fotobestand);
	}

	public void setFotobestand(BronFotobestand fotobestand)
	{
		this.fotobestand = makeModelFor(fotobestand);
	}

	public FotoVerschil getVerschil()
	{
		return verschil;
	}

	public void setVerschil(FotoVerschil verschil)
	{
		this.verschil = verschil;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public Long getPgn()
	{
		return pgn;
	}

	public void setPgn(Long pgn)
	{
		this.pgn = pgn;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Date getTeldatum()
	{
		return teldatum;
	}

	public void setTeldatum(Date teldatum)
	{
		this.teldatum = teldatum;
	}

}
