package nl.topicus.eduarte.entities.participatie.olc;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.participatie.helpers.OlcWaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class OlcLocatie extends CodeNaamActiefInstellingEntiteit implements
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_OlcLocatie_organisatieE")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max", order = 10)
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_OlcLocatie_Locatie")
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private Locatie locatie;

	@ManyToOne(optional = true)
	@JoinColumn(name = "afspraaktype", nullable = false)
	@Index(name = "idx_OlcLocatie_afspraakType")
	@AutoForm(label = "Afspraak type", order = 20, htmlClasses = {"unit_max"})
	private AfspraakType afspraakType;

	public OlcLocatie()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Override
	public String toString()
	{
		return getCode() + " - " + getNaam();
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}

	public boolean isInUse(OlcLocatie olcLocatie)
	{
		return DataAccessRegistry.getHelper(OlcWaarnemingDataAccessHelper.class)
			.hasOlcWaarnemingen(olcLocatie);
	}
}
