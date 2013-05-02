package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Render;

public class AbstractZorgvierkantObjectZoekFilter<T extends ZorgvierkantObject> extends
		AbstractZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private boolean maxZorglijnSet;

	private Integer maxZorglijn;

	private boolean vertrouwelijkAllowed = true;

	private IModel<Deelnemer> deelnemer;

	private IModel<Medewerker> medewerker;

	private Boolean zorgvierkant;

	public AbstractZorgvierkantObjectZoekFilter()
	{
	}

	public AbstractZorgvierkantObjectZoekFilter(Account account, DeelnemerProvider deelnemer,
			String vertrouwelijkSecurityId)
	{
		maxZorglijnSet = true;
		maxZorglijn = account.getMaxZorglijn();
		vertrouwelijkAllowed =
			ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, vertrouwelijkSecurityId,
				deelnemer);
		setDeelnemer(deelnemer.getDeelnemer());
	}

	public void addCriteria(CriteriaBuilder builder)
	{
		if (isMaxZorglijnSet())
		{
			if (getMaxZorglijn() == null)
				builder.addIsNull("zorglijn", true);
			else
				builder.addNullOrLessOrEquals("zorglijn", getMaxZorglijn());
		}
		if (!isVertrouwelijkAllowed())
			builder.addEquals("vertrouwelijk", false);
	}

	public Integer getMaxZorglijn()
	{
		return maxZorglijn;
	}

	public void setMaxZorglijn(Integer maxZorglijn)
	{
		this.maxZorglijn = maxZorglijn;
	}

	public boolean isMaxZorglijnSet()
	{
		return maxZorglijnSet;
	}

	public void setMaxZorglijnSet(boolean maxZorglijnSet)
	{
		this.maxZorglijnSet = maxZorglijnSet;
	}

	public boolean isVertrouwelijkAllowed()
	{
		return vertrouwelijkAllowed;
	}

	public void setVertrouwelijkAllowed(boolean vertrouwelijkAllowed)
	{
		this.vertrouwelijkAllowed = vertrouwelijkAllowed;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public Boolean getZorgvierkant()
	{
		return zorgvierkant;
	}

	public void setZorgvierkant(Boolean zorgvierkant)
	{
		this.zorgvierkant = zorgvierkant;
	}
}
