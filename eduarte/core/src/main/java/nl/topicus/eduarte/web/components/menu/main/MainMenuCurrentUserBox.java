/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.home.SignalenOverzichtPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author loite
 */
public class MainMenuCurrentUserBox extends Panel
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(MainMenuCurrentUserBox.class);

	/**
	 * Maakt de user box aan.
	 */
	public MainMenuCurrentUserBox(String id, final SecurePage page)
	{
		super(id);

		add(ComponentFactory.getDataLabel("medewerkerNaam", page.getIngelogdeGebruikersNaam()));
		Organisatie organisatie = EduArteContext.get().getOrganisatie();
		add(ComponentFactory.getDataLabel("organisatieNaam", organisatie.getNaam()));

		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				Account myAccount = EduArteContext.get().getAccount();
				if (myAccount instanceof MedewerkerAccount)
					setResponsePage(SignalenOverzichtPage.class);
			}

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && !page.isEditable();
			}
		};
		link.add(new Label("messages", new MessagesModel()).setEscapeModelStrings(false)
			.setRenderBodyOnly(false));
		add(link);
		setRenderBodyOnly(true);
	}

	/**
	 * Geeft html opmaakt terug waarin het aantal openstaande en het aantal nieuwe
	 * signalen staat.
	 * 
	 * @author marrink
	 */
	private final class MessagesModel extends LoadableDetachableModel<String>
	{

		private static final long serialVersionUID = 1L;

		@Override
		public String load()
		{
			try
			{
				long nieuw = 0;
				long openstaand = 0;

				return "" + openstaand + " (" + nieuw + ") <img src=\""
					+ getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/message.png\" alt=\"openstaande (ongelezen) signalen\"/>";
			}
			catch (DataAccessException e)
			{
				log.error(e.getMessage(), e);
				return "&nbsp;";
			}
		}
	}
}
