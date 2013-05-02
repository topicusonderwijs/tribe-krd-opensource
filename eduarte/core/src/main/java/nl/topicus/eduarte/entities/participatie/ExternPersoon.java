package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.providers.EmailProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExternPersoon extends InstellingEntiteit implements EmailProvider
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(label = "e-mail")
	private String email;

	public ExternPersoon()
	{
	}

	public ExternPersoon(String email)
	{
		setEmail(email);
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getEmail()
	{
		return email;
	}

	@Override
	public String toString()
	{
		return email;
	}

	@Override
	public String getEmailAdres()
	{
		return getEmail();
	}

	@Override
	public String getNaam()
	{
		return null;
	}
}
