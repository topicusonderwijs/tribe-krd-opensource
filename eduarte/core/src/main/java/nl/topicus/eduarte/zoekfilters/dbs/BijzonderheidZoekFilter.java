package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.model.IModel;

public class BijzonderheidZoekFilter extends AbstractZorgvierkantObjectZoekFilter<Bijzonderheid>
{
	private static final long serialVersionUID = 1L;

	private IModel<BijzonderheidCategorie> categorie;

	private Boolean tonenAlsWaarschuwing;

	private Boolean tonenOpDeelnemerkaart;

	public BijzonderheidZoekFilter()
	{
	}

	public BijzonderheidZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, Bijzonderheid.VERTROUWELIJKE_BIJZONDERHEID);
	}

	public BijzonderheidCategorie getCategorie()
	{
		return getModelObject(categorie);
	}

	public void setCategorie(BijzonderheidCategorie categorie)
	{
		this.categorie = makeModelFor(categorie);
	}

	public Boolean getTonenAlsWaarschuwing()
	{
		return tonenAlsWaarschuwing;
	}

	public void setTonenAlsWaarschuwing(Boolean tonenAlsWaarschuwing)
	{
		this.tonenAlsWaarschuwing = tonenAlsWaarschuwing;
	}

	public Boolean getTonenOpDeelnemerkaart()
	{
		return tonenOpDeelnemerkaart;
	}

	public void setTonenOpDeelnemerkaart(Boolean tonenOpDeelnemerkaart)
	{
		this.tonenOpDeelnemerkaart = tonenOpDeelnemerkaart;
	}

}
