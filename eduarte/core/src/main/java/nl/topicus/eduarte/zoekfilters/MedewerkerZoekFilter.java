/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.AccountType;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.apache.wicket.model.IModel;

public class MedewerkerZoekFilter extends PersoonZoekFilter<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private String gebruikersnaam;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean heeftAccount;

	private String afkorting;

	private IModel<List<Rol>> rollen;

	private IModel<Functie> functie;

	private String snelZoekenString;

	public MedewerkerZoekFilter()
	{
	}

	public final Boolean getHeeftAccount()
	{
		return heeftAccount;
	}

	public final void setHeeftAccount(Boolean heeftAccount)
	{
		this.heeftAccount = heeftAccount;
	}

	public final String getGebruikersnaam()
	{
		return gebruikersnaam;
	}

	public final void setGebruikersnaam(String gebruikersnaam)
	{
		this.gebruikersnaam = gebruikersnaam;
		if (StringUtil.isNotEmpty(gebruikersnaam))
			setHeeftAccount(true);
		else
			setHeeftAccount(null);
	}

	public final AccountType getType()
	{
		return AccountType.Medewerker;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public List<Rol> getRollen()
	{
		return getModelObject(rollen);
	}

	public void setRollen(List<Rol> rollen)
	{
		this.rollen = makeModelFor(rollen);
	}

	public void setRollenModel(IModel<List<Rol>> rollenModel)
	{
		this.rollen = rollenModel;
	}

	public Functie getFunctie()
	{
		return getModelObject(functie);
	}

	public void setFunctie(Functie functie)
	{
		this.functie = makeModelFor(functie);
	}

	public String getSnelZoekenString()
	{
		return snelZoekenString;
	}

	public void setSnelZoekenString(String snelZoekenString)
	{
		this.snelZoekenString = snelZoekenString;
	}

	@Override
	public nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter.SelectieMode getOrganisatieEenheidSelectie()
	{
		return SelectieMode.CHILDREN;
	}
}
