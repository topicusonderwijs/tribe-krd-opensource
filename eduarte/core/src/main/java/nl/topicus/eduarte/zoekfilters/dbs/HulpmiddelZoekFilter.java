package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Hulpmiddel;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.model.IModel;

public class HulpmiddelZoekFilter extends CodeNaamActiefZoekFilter<Hulpmiddel>
{
	private static final long serialVersionUID = 1L;

	private IModel<BijzonderheidCategorie> bijzonderheidCategorie;

	public HulpmiddelZoekFilter()
	{
		super(Hulpmiddel.class);
	}

	public BijzonderheidCategorie getBijzonderheidCategorie()
	{
		return getModelObject(bijzonderheidCategorie);
	}

	public void setBijzonderheidCategorie(BijzonderheidCategorie bijzonderheidCategorie)
	{
		this.bijzonderheidCategorie = makeModelFor(bijzonderheidCategorie);
	}
}
