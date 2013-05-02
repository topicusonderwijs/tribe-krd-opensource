package nl.topicus.eduarte.entities.opleiding;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OpleidingProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een opleiding kan op verschillende organisatie-eenheden aangeboden worden. Een
 * deelnemer wordt ingeschreven op een opleiding. Hierbij wordt ook een
 * organisatie-eenheid en locatie geselecteerd waarop de opleiding wordt aangeboden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "OpleidingAanbod", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"organisatieEenheid", "locatie", "opleiding", "team"})})
@IsViewWhenOnNoise
public class OpleidingAanbod extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelEntiteit<OpleidingAanbod>, ITeamEntiteit,
		OpleidingProvider, IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = false)
	@Index(name = "idx_OplAanbod_opleiding")
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_OplAanbod_orgEhd")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = false)
	@Index(name = "idx_OplAanbod_locatie")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team", nullable = true)
	@Index(name = "idx_OplAanbod_locatie")
	private Team team;

	public OpleidingAanbod()
	{
	}

	public OpleidingAanbod(Opleiding opleiding)
	{
		setOpleiding(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
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
	public Opleiding getEntiteit()
	{
		return getOpleiding();
	}

	public Team getTeam()
	{
		return team;
	}

	public void setTeam(Team team)
	{
		this.team = team;
	}

	@Override
	public String toString()
	{
		return getOpleiding().getNaam();
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return true;
	}

	@Override
	public boolean isActief()
	{
		if (getOrganisatieEenheid() != null && !getOrganisatieEenheid().isActief())
			return false;
		if (getLocatie() != null && !getLocatie().isActief())
			return false;
		return true;
	}
}
