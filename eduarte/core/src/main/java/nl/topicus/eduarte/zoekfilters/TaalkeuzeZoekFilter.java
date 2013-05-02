package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalkeuze;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class TaalkeuzeZoekFilter extends AbstractZoekFilter<Taalkeuze>
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenis;

	private IModel<ModerneTaal> taal;

	private String taalTitel;

	private Boolean voorgedefinieerd;

	public Boolean getVoorgedefinieerd()
	{
		return voorgedefinieerd;
	}

	public void setVoorgedefinieerd(Boolean voorgedefinieerd)
	{
		this.voorgedefinieerd = voorgedefinieerd;
	}

	public ModerneTaal getTaal()
	{
		return getModelObject(taal);
	}

	public void setTaal(ModerneTaal taal)
	{
		this.taal = makeModelFor(taal);
	}

	public String getTaalTitel()
	{
		return taalTitel;
	}

	public void setTaalTitel(String taalTitel)
	{
		this.taalTitel = taalTitel;
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

}
