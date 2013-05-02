/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RolTable;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Uitgebreide informatie over een account en de eigenaar hiervan.
 * 
 * @author marrink
 */
public class AccountPanel extends TypedPanel<Account>
{
	private static final long serialVersionUID = 1L;

	public AccountPanel(String id, IModel<Account> accountModel)
	{
		super(id, accountModel);
		setRenderBodyOnly(true);

		Account account = accountModel.getObject();
		AutoFieldSet<Account> accountFieldSet =
			new AutoFieldSet<Account>("account", accountModel, "Account");
		accountFieldSet.setVisible(account != null);
		accountFieldSet.setPropertyNames("gebruikersnaam", "authorisatieNiveau", "rechtenSoort",
			"ipAdressen", "actiefOmschrijving", "organisatie");
		accountFieldSet.setSortAccordingToPropertyNames(true);
		add(accountFieldSet);

		AutoFieldSet<Persoon> personaliaFieldSet =
			new AutoFieldSet<Persoon>("personalia", new PropertyModel<Persoon>(getDefaultModel(),
				"eigenaar"), "Personalia");
		personaliaFieldSet.setPropertyNames("voornamen", "achternaam", "geslacht");
		accountFieldSet.setSortAccordingToPropertyNames(true);
		personaliaFieldSet.setVisible(account != null && account.getEigenaar() != null);
		add(personaliaFieldSet);

		RolZoekFilter filter = new RolZoekFilter();
		filter.setAccount(account);
		IDataProvider<Rol> dataProvider =
			GeneralFilteredSortableDataProvider.of(filter, RolDataAccessHelper.class);
		EduArteDataPanel<Rol> rollen =
			new EduArteDataPanel<Rol>("rollen", dataProvider, new RolTable());
		rollen.setButtonsVisible(false);
		rollen.setVisible(account != null);
		add(rollen);
	}

}
