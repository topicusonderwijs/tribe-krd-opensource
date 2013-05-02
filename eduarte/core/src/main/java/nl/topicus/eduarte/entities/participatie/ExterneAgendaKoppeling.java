package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.enums.ExterneAgendaConnection;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.KoppelingEditAfspraakTypeCombobox;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ExterneAgendaKoppeling extends InstellingEntiteit implements
		OrganisatieEenheidProvider, IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String naam;

	private boolean actief;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@Index(name = "idx_ExterneAK_afspraakType")
	@AutoForm(order = -1, editorClass = KoppelingEditAfspraakTypeCombobox.class)
	private AfspraakType afspraakType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@Index(name = "idx_EAKoppeling_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max", order = -3)
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	@Index(name = "idx_EAKoppeling_locatie")
	@AutoForm(order = -2)
	private Locatie locatie;

	@AutoForm(label = "Automatische koppeling")
	private boolean automatisch;

	@AutoForm(label = "Geldigheids duur (min.)", htmlClasses = "unit_40")
	private int geldigheidsduur;

	public ExterneAgendaKoppeling()
	{
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setGeldigheidsduur(int geldigheidsduur)
	{
		this.geldigheidsduur = geldigheidsduur;
	}

	public int getGeldigheidsduur()
	{
		return geldigheidsduur;
	}

	abstract public ExterneAgendaConnection connect(ExterneAgenda agenda)
			throws ExterneAgendaException;

	abstract public String getBeschrijving();

	public void selectDefaults()
	{
		geldigheidsduur = 60;
		actief = true;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public void setAutomatisch(boolean automatisch)
	{
		this.automatisch = automatisch;
	}

	public boolean isAutomatisch()
	{
		return automatisch;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}
}
