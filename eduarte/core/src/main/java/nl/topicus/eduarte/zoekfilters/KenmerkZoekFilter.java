package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class KenmerkZoekFilter extends CodeNaamActiefZoekFilter<Kenmerk>
{
	private static final long serialVersionUID = 1L;

	private IModel<KenmerkCategorie> categorie;

	public KenmerkZoekFilter()
	{
		super(Kenmerk.class);
	}

	public KenmerkCategorie getCategorie()
	{
		return getModelObject(categorie);
	}

	public void setCategorie(KenmerkCategorie categorie)
	{
		this.categorie = makeModelFor(categorie);
	}

}
