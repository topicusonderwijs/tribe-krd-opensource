/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.text.autocomplete.RolCategorieAutoCompleteTextField;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link Rol}s. Rollen boven of op het ingestelde authorisatieniveau worden
 * weggefiltered.
 * 
 * @author marrink
 */
public class RolZoekFilter extends AbstractZoekFilter<Rol>
{
	private static final long serialVersionUID = 1L;

	private IModel<Account> account;

	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	@AutoForm(editorClass = RolCategorieAutoCompleteTextField.class, htmlClasses = "unit_160")
	private String categorie;

	private AuthorisatieNiveau authorisatieNiveau;

	private RechtenSoort rechtenSoort;

	public RolZoekFilter()
	{
	}

	public final AuthorisatieNiveau getAuthorisatieNiveau()
	{
		return authorisatieNiveau;
	}

	public final void setAuthorisatieNiveau(AuthorisatieNiveau authorisatieNiveau)
	{
		this.authorisatieNiveau = authorisatieNiveau;
	}

	public final String getNaam()
	{
		return naam;
	}

	public final void setNaam(String gebruikersnaam)
	{
		this.naam = gebruikersnaam;
	}

	public final RechtenSoort getRechtenSoort()
	{
		return rechtenSoort;
	}

	public final void setRechtenSoort(RechtenSoort rechtenSoort)
	{
		this.rechtenSoort = rechtenSoort;
	}

	public String getCategorie()
	{
		return categorie;
	}

	public void setCategorie(String categorie)
	{
		this.categorie = categorie;
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

}
