/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.shortcut.ShortcutEnabledComponent;
import nl.topicus.cobra.web.components.shortcut.ShortcutRegister;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.core.principals.Snelzoeken;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerkaartPage;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * Snelzoekform voor deelnemers.
 * 
 * Note: deze class staat in web.pages omdat de principal scan niet door web.components
 * loopt. Laten staan dus.
 * 
 * @author loite
 */
@InPrincipal(Snelzoeken.class)
public class SnelZoekForm extends Form<Void> implements ShortcutEnabledComponent
{
	private IModel<String> deelnemerModel = new Model<String>();

	private TextField<String> txtField;

	private static final long serialVersionUID = 1L;

	public SnelZoekForm(String id)
	{
		super(id);
		ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(SnelZoekForm.class));
		add(txtField = new TextField<String>("registratieNummer", deelnemerModel));
	}

	@Override
	protected void onSubmit()
	{
		String val = deelnemerModel.getObject();
		if (StringUtil.isNotEmpty(val))
		{
			performQuery(val.replace("*", "%"));
		}
	}

	private void performQuery(String val)
	{
		if (StringUtil.isNumeric(val))
		{
			// probeer eerst met alleen deelnemernummer.
			Deelnemer dln =
				DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).getByDeelnemernummer(
					Integer.valueOf(val));
			if (!new DeelnemerSecurityCheck(new ClassSecurityCheck(DeelnemerkaartPage.class), dln)
				.isActionAuthorized(Render.class))
			{
				error("U heeft geen rechten om deze deelnemer te bekijken.");
				return;
			}
			if (dln != null)
			{
				setResponsePage(new DeelnemerkaartPage(dln));
				return;
			}
		}

		DeelnemerZoekFilter deelnemerFilter = getDeelnemerZoekFilter(val);

		VerbintenisZoekFilter verbintenisFilter = new VerbintenisZoekFilter();
		verbintenisFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new ClassSecurityCheck(DeelnemerZoekenPage.class)));
		verbintenisFilter.setCustomPeildatumModel(new Model<Date>());
		verbintenisFilter.setDeelnemerZoekFilter(deelnemerFilter);
		deelnemerFilter.setExactSnelZoeken(true);

		boolean deelnemerAuthorized =
			new ClassSecurityCheck(DeelnemerZoekenPage.class).isActionAuthorized(Render.class);
		long deelnemerCount =
			deelnemerAuthorized ? DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class)
				.getDeelnemerCount(verbintenisFilter) : 0;

		if (deelnemerCount == 1)
		{
			// exact 1 deelnemer (maar natuurlijk evt meerdere verbintenissen
			Verbintenis verbintenis = getDeelnemer(verbintenisFilter);
			if (verbintenis != null)
			{
				setResponsePage(new DeelnemerkaartPage(verbintenis));
				return;
			}
		}
		deelnemerFilter.setExactSnelZoeken(false);
		if (deelnemerCount == 0 && deelnemerAuthorized)
		{
			deelnemerCount =
				DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).getDeelnemerCount(
					verbintenisFilter);
		}

		GroepZoekFilter groepFilter = getGroepZoekFilter(val);
		groepFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new ClassSecurityCheck(GroepZoekenPage.class)));

		MedewerkerZoekFilter medewerkerFilter = getMedewerkerZoekFilter(val);
		medewerkerFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new ClassSecurityCheck(MedewerkerZoekenPage.class)));

		int groepCount =
			new ClassSecurityCheck(GroepZoekenPage.class).isActionAuthorized(Render.class)
				? getCount(GroepDataAccessHelper.class, groepFilter) : 0;
		int medewerkerCount =
			new ClassSecurityCheck(MedewerkerZoekenPage.class).isActionAuthorized(Render.class)
				? getCount(MedewerkerDataAccessHelper.class, medewerkerFilter) : 0;

		verbintenisFilter.addOrderByProperty("beeindigd");
		verbintenisFilter.addOrderByProperty("deelnemer.deelnemernummer");
		verbintenisFilter.addOrderByProperty("persoon.roepnaam");
		verbintenisFilter.addOrderByProperty("persoon.achternaam");

		if (deelnemerCount > 1 && groepCount == 0 && medewerkerCount == 0)
		{
			// meer dan 1 deelnemer dus naar deelnemerzoekenpage
			setResponsePage(new DeelnemerZoekenPage(verbintenisFilter));
		}
		else if (deelnemerCount == 0 && groepCount > 1 && medewerkerCount == 0)
		{
			// meer dan 1 groep dus naar groepzoekenpage
			setResponsePage(new GroepZoekenPage(groepFilter));
		}
		else if (deelnemerCount == 0 && groepCount == 0 && medewerkerCount > 1)
		{
			// meer dan 1 medewerker dus naar medewerkerzoekenpage
			setResponsePage(new MedewerkerZoekenPage(medewerkerFilter));
		}
		else if (deelnemerCount == 0 && groepCount == 1 && medewerkerCount == 0)
		{
			// exact 1 groep
			Groep groep = getGroep(groepFilter);
			if (groep != null)
				setResponsePage(new GroepKaartPage(groep));
			else
				warn("Onbekende groep of onvoldoende rechten: " + val);
		}
		else if (deelnemerCount == 0 && groepCount == 0 && medewerkerCount == 1)
		{
			// exact 1 medewerker
			Medewerker medewerker = getMedewerker(medewerkerFilter);
			if (medewerker != null)
				setResponsePage(new MedewerkerkaartPage(medewerker));
			else
				warn("Onbekende medewerker of onvoldoende rechten: " + val);
		}
		else if (deelnemerCount == 0 && groepCount == 0 && medewerkerCount == 0)
		{
			error("Er zijn geen resultaten gevonden voor " + val);
		}
		else
		{
			setResponsePage(new SnelZoekenPage(verbintenisFilter, groepFilter, medewerkerFilter,
				deelnemerCount, groepCount, medewerkerCount));
		}
	}

	private DeelnemerZoekFilter getDeelnemerZoekFilter(String query)
	{
		DeelnemerZoekFilter zoekFilter = new DeelnemerZoekFilter();
		zoekFilter.setSnelZoekenString(query);
		return zoekFilter;

	}

	private GroepZoekFilter getGroepZoekFilter(String query)
	{
		GroepZoekFilter zoekFilter = new GroepZoekFilter();
		zoekFilter.setSnelZoekenString(query);
		return zoekFilter;
	}

	private MedewerkerZoekFilter getMedewerkerZoekFilter(String query)
	{
		MedewerkerZoekFilter zoekFilter = new MedewerkerZoekFilter();
		zoekFilter.setSnelZoekenString(query);
		return zoekFilter;
	}

	@SuppressWarnings("unchecked")
	private int getCount(Class< ? extends ZoekFilterDataAccessHelper> helperClass,
			AbstractZoekFilter filter)
	{
		return DataAccessRegistry.getHelper(helperClass).listCount(filter);
	}

	private Verbintenis getDeelnemer(VerbintenisZoekFilter filter)
	{
		List<Verbintenis> verbintenissen =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).list(filter, 0, 1);

		if (verbintenissen.size() > 0)
			return verbintenissen.get(0);

		return null;
	}

	private Groep getGroep(GroepZoekFilter filter)
	{
		List<Groep> groepen =
			DataAccessRegistry.getHelper(GroepDataAccessHelper.class).list(filter);

		if (groepen.size() > 0)
			return groepen.get(0);

		return null;
	}

	private Medewerker getMedewerker(MedewerkerZoekFilter filter)
	{
		List<Medewerker> medewerkers =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).list(filter);

		if (medewerkers.size() > 0)
			return medewerkers.get(0);

		return null;
	}

	@Override
	public void registerShortcuts(ShortcutRegister register)
	{
		register.registerShortcut(txtField, KeyActionEnum.HOOFDMENU_ZOEKEN);
	}
}
