package nl.topicus.eduarte.entities.personen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "PersoonExtOrgContactPersoon")
public class PersoonExterneOrganisatieContactPersoon extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "persoonExterneOrganisatie")
	@Index(name = "idx_PersExtOrgCP_perEO")
	private PersoonExterneOrganisatie persoonExterneOrganisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "extOrgContactPersoon")
	@Index(name = "idx_PersExtOrgCP_extOrgCP")
	private ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon;

	public PersoonExterneOrganisatieContactPersoon()
	{
	}

	public PersoonExterneOrganisatieContactPersoon(
			PersoonExterneOrganisatie persoonExterneOrganisatie)
	{
		this.persoonExterneOrganisatie = persoonExterneOrganisatie;
	}

	public PersoonExterneOrganisatie getPersoonExterneOrganisatie()
	{
		return persoonExterneOrganisatie;
	}

	public void setExterneOrganisatieContactPersoon(
			ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon)
	{
		this.externeOrganisatieContactPersoon = externeOrganisatieContactPersoon;
	}

	public ExterneOrganisatieContactPersoon getExterneOrganisatieContactPersoon()
	{
		return externeOrganisatieContactPersoon;
	}

	public void setPersoonExterneOrganisatie(PersoonExterneOrganisatie persoonExterneOrganisatie)
	{
		this.persoonExterneOrganisatie = persoonExterneOrganisatie;
	}

}
