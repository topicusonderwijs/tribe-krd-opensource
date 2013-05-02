package nl.topicus.eduarte.zoekfilters.dbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.dbs.trajecten.MogelijkeAanleiding;
import nl.topicus.eduarte.entities.dbs.trajecten.MogelijkeAanleiding.MogelijkeAanleidingType;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Render;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class MogelijkeAanleidingZoekFilter extends AbstractZoekFilter<MogelijkeAanleiding>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_160")
	private String omschrijving;

	private IModel<Deelnemer> deelnemer;

	@AutoForm(htmlClasses = "unit_80")
	private Date datum;

	private MogelijkeAanleidingType type;

	private boolean maxZorglijnSet;

	private Integer maxZorglijn;

	private Set<MogelijkeAanleidingType> allowed = new HashSet<MogelijkeAanleidingType>();

	private Set<MogelijkeAanleidingType> vertrouwelijkAllowed =
		new HashSet<MogelijkeAanleidingType>();

	public MogelijkeAanleidingZoekFilter()
	{
	}

	public MogelijkeAanleidingZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		addOrderByProperty("omschrijving");
		addOrderByProperty("type");
		maxZorglijnSet = true;
		maxZorglijn = account.getMaxZorglijn();
		for (MogelijkeAanleidingType curType : MogelijkeAanleidingType.values())
		{
			if (ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, curType.getSecurityId(),
				deelnemer))
				allowed.add(curType);
			if (ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, curType
				.getVertrouwelijkSecurityId(), deelnemer))
				vertrouwelijkAllowed.add(curType);
		}
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
		List<Criterion> ors = new ArrayList<Criterion>();
		if (!vertrouwelijkAllowed.isEmpty())
			ors.add(Restrictions.in("type", vertrouwelijkAllowed));
		if (!allowed.isEmpty())
			ors.add(Restrictions.and(Restrictions.eq("vertrouwelijk", false), Restrictions.in(
				"type", allowed)));
		builder.addOrs(ors);
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public MogelijkeAanleidingType getType()
	{
		return type;
	}

	public void setType(MogelijkeAanleidingType type)
	{
		this.type = type;
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

	public Set<MogelijkeAanleidingType> getAllowed()
	{
		return allowed;
	}

	public void setAllowed(Set<MogelijkeAanleidingType> allowed)
	{
		this.allowed = allowed;
	}

	public Set<MogelijkeAanleidingType> getVertrouwelijkAllowed()
	{
		return vertrouwelijkAllowed;
	}

	public void setVertrouwelijkAllowed(Set<MogelijkeAanleidingType> vertrouwelijkAllowed)
	{
		this.vertrouwelijkAllowed = vertrouwelijkAllowed;
	}
}
