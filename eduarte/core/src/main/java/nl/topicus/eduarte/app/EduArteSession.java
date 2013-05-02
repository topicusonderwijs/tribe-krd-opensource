/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.app.CobraSession;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.app.security.authorization.EduArteLogin;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.RequestLogger.ISessionLogInfo;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.security.WaspApplication;
import org.apache.wicket.security.WaspSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom sessie voor EduArte
 * 
 * @author marrink
 */
public class EduArteSession extends CobraSession implements ISessionLogInfo
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EduArteSession.class);

	private final Map<Class< ? extends AbstractSideBarDataStore>, AbstractSideBarDataStore> sideBarDataStores =
		new HashMap<Class< ? extends AbstractSideBarDataStore>, AbstractSideBarDataStore>();

	private transient EduArteSessionInfo sessionInfo;

	/**
	 * Het cohort dat door de gebruiker geselecteerd is voor het inrichten van de
	 * applicatie of voor het afhandelen van examens. Dit wordt per sessie onthouden zodat
	 * de gebruiker niet elke keer opnieuw het juiste cohort hoeft te selecteren (dit
	 * hoeft namelijk niet altijd het huidige cohort te zijn). Let op: Dit cohort dient
	 * alleen gebruikt te worden voor inrichtingspagina's en examenpagina's, en niet voor
	 * bijvoorbeeld het inschrijven van deelnemers.
	 */
	private IModel<Cohort> selectedCohortModel;

	/**
	 * De opleiding dat door de gebruiker geselecteerd is voor het inrichten van de
	 * applicatie of voor het afhandelen van examens. Dit wordt per sessie onthouden zodat
	 * de gebruiker niet elke keer opnieuw de juiste opleiding hoeft te selecteren. Let
	 * op: Deze opleiding dient alleen gebruikt te worden voor inrichtingspagina's en
	 * examenpagina's, en niet voor bijvoorbeeld het inschrijven van deelnemers.
	 */
	private IModel<Opleiding> selectedOpleidingModel;

	/**
	 * @param application
	 * @param request
	 */
	public EduArteSession(WaspApplication application, Request request)
	{
		super(application, request);
	}

	/**
	 * De EduArte sessie.
	 * 
	 * @return sessie of null als die niet beschikbaar is.
	 */
	public static final EduArteSession get()
	{
		return (EduArteSession) Session.get();
	}

	/**
	 * Log off gebruiker.
	 * 
	 * @return true, als het gelukt is om de gebruiker uit te loggen, ander false.
	 */
	@Override
	public boolean logoff()
	{
		return ((WaspSession) Session.get()).logoff(new EduArteLogin());
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @return De sidebar datastore geregistreerd onder de gegeven class
	 */
	@SuppressWarnings("unchecked")
	public final <T extends AbstractSideBarDataStore> T getSideBarDataStore(Class<T> clazz)
	{
		T datastore = (T) sideBarDataStores.get(clazz);
		if (datastore == null)
		{
			// Maak een nieuwe datastore aan
			try
			{
				datastore = clazz.newInstance();
			}
			catch (InstantiationException e)
			{
				String msg = "Kon datastore " + clazz.getName() + " niet instantieren";
				log.error(msg, e);
				throw new WicketRuntimeException(msg, e);
			}
			catch (IllegalAccessException e)
			{
				String msg = "Kon datastore " + clazz.getName() + " niet instantieren";
				log.error(msg, e);
				throw new WicketRuntimeException(msg, e);
			}
			sideBarDataStores.put(clazz, datastore);
		}
		return datastore;
	}

	/**
	 * @return De sidebar datastores van deze sessie
	 */
	public final Collection<AbstractSideBarDataStore> getSideBarDataStores()
	{
		return Collections.unmodifiableCollection(sideBarDataStores.values());
	}

	/**
	 * 
	 * Het cohort dat door de gebruiker geselecteerd is voor het inrichten van de
	 * applicatie. Dit wordt per sessie onthouden zodat de gebruiker niet elke keer
	 * opnieuw het juiste cohort hoeft te selecteren (dit hoeft namelijk niet altijd het
	 * huidige cohort te zijn). Let op: Dit cohort dient alleen gebruikt te worden voor
	 * inrichtingspagina's, niet voor bijvoorbeeld het inschrijven van deelnemers.
	 */
	public IModel<Cohort> getSelectedCohortModel()
	{
		if (selectedCohortModel == null)
			selectedCohortModel = ModelFactory.getModel(Cohort.getHuidigCohort());
		if (selectedCohortModel.getObject() == null)
			selectedCohortModel.setObject(Cohort.getHuidigCohort());
		return selectedCohortModel;
	}

	/**
	 * De opleiding dat door de gebruiker geselecteerd is voor het inrichten van de
	 * applicatie of voor het afhandelen van examens. Dit wordt per sessie onthouden zodat
	 * de gebruiker niet elke keer opnieuw de juiste opleiding hoeft te selecteren. Let
	 * op: Deze opleiding dient alleen gebruikt te worden voor inrichtingspagina's en
	 * examenpagina's, en niet voor bijvoorbeeld het inschrijven van deelnemers.
	 */
	public Opleiding getSelectedOpleiding()
	{
		if (selectedOpleidingModel == null)
			return null;

		return selectedOpleidingModel.getObject();
	}

	public void setSelectedOpleiding(Opleiding opleiding)
	{
		if (selectedOpleidingModel == null)
			selectedOpleidingModel = ModelFactory.getModel(opleiding);

		selectedOpleidingModel.setObject(opleiding);
	}

	@Override
	public EduArteSessionInfo getSessionInfo()
	{
		if (sessionInfo == null)
		{
			sessionInfo = new EduArteSessionInfo(this, (WebClientInfo) getClientInfo());
		}
		return sessionInfo;
	}
}
