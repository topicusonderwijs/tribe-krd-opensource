/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.AbstractCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

/**
 * Filter om naast de instelling ook op de organisatieeenheid en locatie te filteren. Dit
 * filter heeft opties om te beperken tot een selectie van toegestane organisatieeenheden
 * en locaties.
 * 
 * @author marrink
 * @param <T>
 */
public abstract class AbstractOrganisatieEenheidLocatieZoekFilter<T> extends AbstractZoekFilter<T>
		implements IOrganisatieEenheidLocatieZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	public static enum SelectieMode
	{
		PARENTS_AND_CHILDREN,
		PARENTS,
		CHILDREN;
	}

	@AutoForm(htmlClasses = "unit_160")
	private IModel<OrganisatieEenheid> organisatieEenheid;

	@AutoForm(htmlClasses = "unit_100")
	private IModel<Locatie> locatie;

	private SelectieMode organisatieEenheidSelectie = SelectieMode.PARENTS_AND_CHILDREN;

	/**
	 * Lijst met organisatie-eenheden en locaties waaruit gekozen mag worden.
	 */
	private OrganisatieEenheidLocatieAuthorizationContext authorizationContext;

	public AbstractOrganisatieEenheidLocatieZoekFilter()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getModelObject(organisatieEenheid);
	}

	public void setOrganisatieEenheid(final OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = makeModelFor(organisatieEenheid);
	}

	public void setOrganisatieEenheidModel(IModel<OrganisatieEenheid> organisatieEenheidModel)
	{
		this.organisatieEenheid = organisatieEenheidModel;
	}

	/**
	 * Levert een lijst op van alle organisatie-eenheden die feitelijk geselecteerd zijn
	 * doordat een {@link OrganisatieEenheid} die hoger of lager in de hierarchy staat
	 * geselecteerd is.
	 * 
	 * @return ongesorteerde lijst
	 */
	public List<OrganisatieEenheid> getGeselecteerdeOrganisatieEenheden()
	{
		return getGeselecteerdeOrganisatieEenheden(getOrganisatieEenheid());
	}

	/**
	 * Leverd een lijst op van alle organisatie-eenheden die feitelijk geselecteerd zijn
	 * doordat een {@link OrganisatieEenheid} die hoger in de hierarchy staat geselecteerd
	 * is.
	 * 
	 * @param eenheid
	 *            de organisatie-eenheid waarvan we de kinderen gebruiken
	 * @return ongesorteerde lijst
	 */
	public List<OrganisatieEenheid> getGeselecteerdeOrganisatieEenheden(OrganisatieEenheid eenheid)
	{
		if (eenheid == null)
			return Collections.emptyList();
		switch (getOrganisatieEenheidSelectie())
		{
			case PARENTS:
				return eenheid.getParents();
			case CHILDREN:
				return eenheid.getActieveChildren(getPeildatum());
			default:
				return eenheid.getParentsEnChildren(getPeildatum());
		}
	}

	/**
	 * Geeft alle organisatie-eenheden waarop gezocht moet worden. Als er expliciet een
	 * org.ehd. geselecteerd is, dient deze als basis. Is er geen selectie gemaakt, wordt
	 * er gezocht op alle org.ehd.s waar de gebruiker recht op heeft. Deze methode houdt
	 * al rekening met de 'zoekrichting' (PARENT/CHILDREN).
	 */
	private Set<OrganisatieEenheid> getTeZoekenOrganisatieEenheden()
	{
		List<OrganisatieEenheid> basis = new ArrayList<OrganisatieEenheid>();
		if (getOrganisatieEenheid() == null)
			basis.addAll(getAuthorizationContext().getToegestaneOrganisatieEenheden());
		else
			basis.add(getOrganisatieEenheid());
		Set<OrganisatieEenheid> ret = new HashSet<OrganisatieEenheid>();
		for (OrganisatieEenheid curBasis : basis)
		{
			ret.addAll(getGeselecteerdeOrganisatieEenheden(curBasis));
		}
		return ret;
	}

	private Set<Locatie> getActieveLocaties(Collection<OrganisatieEenheidLocatie> locaties)
	{
		Set<Locatie> ret = new HashSet<Locatie>();
		for (OrganisatieEenheidLocatie curLocatie : locaties)
		{
			if (getPeildatum() == null || curLocatie.isActief(getPeildatum()))
				ret.add(curLocatie.getLocatie());
		}
		return ret;
	}

	private Set<Locatie> getTeZoekenLocaties(OrganisatieEenheid eenheid)
	{
		Set<Locatie> ret = new HashSet<Locatie>();
		if (getLocatie() == null)
		{
			if (getAuthorizationContext().isLocatieLoosAuthorisatie(eenheid))
				return null;
			else
				ret.addAll(getActieveLocaties(getAuthorizationContext().getToegestaneLocaties(
					eenheid)));
		}
		else if (getActieveLocaties(eenheid.getAlleLocaties()).contains(getLocatie()))
			ret.add(getLocatie());
		return ret;
	}

	/**
	 * Voegt criteria toe die filteren op organisatie-eenheid en locatie. Hierbij wordt
	 * uitgegaan van dat de entiteit die gezocht wordt de properties organisatieEenheid en
	 * locatie heeft.
	 * 
	 * @param criteria
	 * @return false als er geen zoekresultaten mogelijk/toegestaan zijn. Het maken van de
	 *         criteria zou dan direct gestaakt moeten worden en null terug moeten geven.
	 */
	public boolean addOrganisatieEenheidLocatieCriteria(Criteria criteria)
	{
		return addOrganisatieEenheidLocatieCriteria("", new CriteriaBuilder(criteria));
	}

	public boolean addOrganisatieEenheidLocatieCriteria(String alias, Criteria criteria)
	{
		return addOrganisatieEenheidLocatieCriteria(alias, new CriteriaBuilder(criteria));
	}

	public boolean addOrganisatieEenheidLocatieCriteria(DetachedCriteria dc)
	{
		return addOrganisatieEenheidLocatieCriteria("", new DetachedCriteriaBuilder(dc));
	}

	/**
	 * Voegt criteria toe die filteren op organisatie-eenheid en locatie. Hierbij wordt
	 * uitgegaan van een koppeltabel tussen de entiteit die gezocht wordt en
	 * organisatie-eenheid en locatie.
	 * 
	 * @param criteria
	 * @param koppelClass
	 * @param koppelClassProperty
	 * @return false als er geen zoekresultaten mogelijk/toegestaan zijn. Het maken van de
	 *         criteria zou dan direct gestaakt moeten worden en null terug moeten geven.
	 */
	public <U extends IOrganisatieEenheidLocatieKoppelEntiteit<U>> boolean addOrganisatieEenheidLocatieDetachedCriteria(
			HibernateDataAccessHelper<T> helper, Criteria criteria, Class<U> koppelClass,
			String koppelClassProperty)
	{
		return addOrganisatieEenheidLocatieDetachedCriteria(helper, criteria, koppelClass,
			koppelClassProperty, "id");
	}

	public <U extends IOrganisatieEenheidLocatieKoppelEntiteit<U>> boolean addOrganisatieEenheidLocatieDetachedCriteria(
			HibernateDataAccessHelper<T> helper, Criteria criteria, Class<U> koppelClass,
			String koppelClassProperty, String selectieProperty)
	{
		if (getAuthorizationContext() == null)
		{
			throw new WicketRuntimeException("AuthorizationContext is niet gezet op "
				+ getClass().getSimpleName() + ". Gebruik filter.setAuthorizationContext("
				+ "new OrganisatieEenheidLocatieAuthorizationContext(componentInstance)) "
				+ "om dit op te lossen");
		}

		boolean geenOrgEhdFiltering =
			getAuthorizationContext().isInstellingClearance()
				&& getGeselecteerdeOrganisatieEenheden().isEmpty() && getLocatie() == null;
		if (geenOrgEhdFiltering)
			return true;
		DetachedCriteria dc = helper.createDetachedCriteria(koppelClass);
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
		if (!addOrganisatieEenheidLocatieCriteria("", dcBuilder))
			return false;

		if (IBeginEinddatumEntiteit.class.isAssignableFrom(koppelClass))
		{
			dcBuilder.addLessOrEquals("begindatum", getPeildatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", getPeildatum());
		}

		setResultCacheable(false);
		dc.setProjection(Projections.property(koppelClassProperty));
		Criterion crit = Subqueries.propertyIn(selectieProperty, dc);

		// if (geenOrgEhdFiltering)
		// {
		// DetachedCriteria dcNotExists = helper.createDetachedCriteria(koppelClass);
		// DetachedCriteriaBuilder dcNEBuilder = new DetachedCriteriaBuilder(dcNotExists);
		// if (IBeginEinddatumEntiteit.class.isAssignableFrom(koppelClass))
		// {
		// dcNEBuilder.addLessOrEquals("begindatum", getPeildatum());
		// dcNEBuilder.addGreaterOrEquals("einddatumNotNull", getPeildatum());
		// }
		// dcNotExists.setProjection(Projections.property(koppelClassProperty));
		// crit = Restrictions.or(crit, Subqueries.propertyNotIn(selectieProperty,
		// dcNotExists));
		// }
		criteria.add(crit);

		return true;
	}

	private boolean addOrganisatieEenheidLocatieCriteria(String alias,
			AbstractCriteriaBuilder builder)
	{
		if (getAuthorizationContext() == null)
		{
			throw new WicketRuntimeException("AuthorizationContext is niet gezet op "
				+ getClass().getSimpleName() + ". Gebruik filter.setAuthorizationContext("
				+ "new OrganisatieEenheidLocatieAuthorizationContext(componentInstance)) "
				+ "om dit op te lossen");
		}

		if (alias == null)
			alias = "";
		if (alias.length() > 0 && !alias.endsWith("."))
			alias += ".";

		if (getAuthorizationContext().isInstellingClearance()
			&& getGeselecteerdeOrganisatieEenheden().isEmpty() && getLocatie() == null)
			return true;

		List<Criterion> ors = new ArrayList<Criterion>();
		Set<OrganisatieEenheid> teZoekenEenheden = getTeZoekenOrganisatieEenheden();
		if (teZoekenEenheden.isEmpty())
			return false;
		for (OrganisatieEenheid curEenheid : teZoekenEenheden)
		{
			Set<Locatie> locaties = getTeZoekenLocaties(curEenheid);
			Criterion orgEhdCrit = Restrictions.eq(alias + "organisatieEenheid", curEenheid);
			if (locaties == null)
			{
				ors.add(orgEhdCrit);
			}
			else if (!locaties.isEmpty())
			{
				Criterion locListCheck =
					Restrictions.and(Restrictions.isNotNull(alias + "locatie"), Restrictions.in(
						alias + "locatie", locaties));
				ors.add(Restrictions.and(orgEhdCrit, locListCheck));
			}
		}
		ors.add(Restrictions.and(Restrictions.in(alias + "organisatieEenheid", teZoekenEenheden),
			Restrictions.isNull(alias + "locatie")));
		builder.addOrs(ors);
		return true;
	}

	public OrganisatieEenheidLocatieAuthorizationContext getAuthorizationContext()
	{
		return authorizationContext;
	}

	public void setAuthorizationContext(
			OrganisatieEenheidLocatieAuthorizationContext authorizationContext)
	{
		this.authorizationContext = authorizationContext;
	}

	public Locatie getLocatie()
	{
		return getModelObject(locatie);
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = makeModelFor(locatie);
	}

	public void setLocatieModel(IModel<Locatie> locatieModel)
	{
		this.locatie = locatieModel;
	}

	public SelectieMode getOrganisatieEenheidSelectie()
	{
		return organisatieEenheidSelectie;
	}

	public void setOrganisatieEenheidSelectie(SelectieMode organisatieEenheidSelectie)
	{
		this.organisatieEenheidSelectie = organisatieEenheidSelectie;
	}
}
