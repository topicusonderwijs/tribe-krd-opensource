package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.model.IModel;

public class DeelnemerZoekOpdrachtZoekFilter extends AbstractZoekFilter<DeelnemerZoekOpdracht>
{
	private static final long serialVersionUID = 1L;

	private String omschrijving;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean persoonlijk;

	private IModel<Account> account;

	private boolean publicerenRecht;

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setPersoonlijk(Boolean persoonlijk)
	{
		this.persoonlijk = persoonlijk;
	}

	public Boolean isPersoonlijk()
	{
		return persoonlijk;
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}

	public boolean isPublicerenRecht()
	{
		return publicerenRecht;
	}

	public void setPublicerenRecht(boolean publicerenRecht)
	{
		this.publicerenRecht = publicerenRecht;
	}
}
