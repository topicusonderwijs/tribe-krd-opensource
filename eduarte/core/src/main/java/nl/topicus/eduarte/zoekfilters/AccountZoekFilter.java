/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.AccountType;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.choice.RolCombobox;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link Account}s. Accounts boven het ingestelde authorisatieniveau worden
 * weggefiltered.
 * 
 * @author marrink
 */
public class AccountZoekFilter extends AbstractZoekFilter<Account>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_180")
	private String gebruikersnaam;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	private AuthorisatieNiveau authorisatieNiveau;

	@AutoForm(required = true)
	private AccountType type = AccountType.Medewerker;

	@AutoForm(editorClass = RolCombobox.class)
	private IModel<Rol> rol;

	public AccountZoekFilter()
	{
		actief = true;
	}

	public final Boolean getActief()
	{
		return actief;
	}

	public final void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public final AuthorisatieNiveau getAuthorisatieNiveau()
	{
		return authorisatieNiveau;
	}

	public final void setAuthorisatieNiveau(AuthorisatieNiveau authorisatieNiveau)
	{
		this.authorisatieNiveau = authorisatieNiveau;
	}

	public final String getGebruikersnaam()
	{
		return gebruikersnaam;
	}

	public final void setGebruikersnaam(String gebruikersnaam)
	{
		this.gebruikersnaam = gebruikersnaam;
	}

	public final AccountType getType()
	{
		return type;
	}

	public final void setType(AccountType type)
	{
		if (type == null)
			this.type = AccountType.All;
		else
			this.type = type;
	}

	public void setRol(Rol rol)
	{
		this.rol = makeModelFor(rol);
	}

	public Rol getRol()
	{
		return getModelObject(rol);
	}
}
