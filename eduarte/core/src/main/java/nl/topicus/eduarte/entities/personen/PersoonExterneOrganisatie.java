package nl.topicus.eduarte.entities.personen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("O")
@Exportable
public class PersoonExterneOrganisatie extends AbstractRelatie
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instelling", nullable = true)
	@Index(name = "idx_AbstractRelatie_instelling")
	@AutoFormEmbedded
	private ExterneOrganisatie relatie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoonExterneOrganisatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<PersoonExterneOrganisatieContactPersoon> persoonExterneOrganisatieContactPersonen =
		new ArrayList<PersoonExterneOrganisatieContactPersoon>();

	public PersoonExterneOrganisatie()
	{
	}

	public void setRelatie(ExterneOrganisatie externeOrganisatie)
	{
		this.relatie = externeOrganisatie;
	}

	@Override
	@Exportable
	public ExterneOrganisatie getRelatie()
	{
		return relatie;
	}

	public List<PersoonExterneOrganisatieContactPersoon> getPersoonExterneOrganisatieContactPersonen()
	{
		return persoonExterneOrganisatieContactPersonen;
	}

	public void setPersoonExterneOrganisatieContactPersonen(
			List<PersoonExterneOrganisatieContactPersoon> persoonExterneOrganisatieContactPersonen)
	{
		this.persoonExterneOrganisatieContactPersonen = persoonExterneOrganisatieContactPersonen;
	}

	public ExterneOrganisatieAdres getEersteAdresOpPeildatum()
	{
		return getRelatie().getFysiekAdres();
	}

	public Geslacht getGeslacht()
	{
		return null;
	}

	public String getNaam()
	{
		return getRelatie().getNaam();
	}

	public void setNaam(String naam)
	{
		getRelatie().setNaam(naam);
	}

	public String getSoort()
	{
		if (relatie.getSoortExterneOrganisatie() != null)
			return relatie.getSoortExterneOrganisatie().toString();
		else
			return null;
	}

	public void verwijderGekoppeldeContactPersoon(ExterneOrganisatieContactPersoon contactPersoon)
	{
		List<PersoonExterneOrganisatieContactPersoon> toDelete =
			new ArrayList<PersoonExterneOrganisatieContactPersoon>();

		for (PersoonExterneOrganisatieContactPersoon extOrgContactPersoon : getPersoonExterneOrganisatieContactPersonen())
		{
			if (contactPersoon.equals(extOrgContactPersoon.getExterneOrganisatieContactPersoon()))
				toDelete.add(extOrgContactPersoon);
		}

		for (PersoonExterneOrganisatieContactPersoon del : toDelete)
		{
			getPersoonExterneOrganisatieContactPersonen().remove(del);
		}
	}

	public boolean isGekoppeldMetContactPersoon(ExterneOrganisatieContactPersoon contactPersoon)
	{
		for (PersoonExterneOrganisatieContactPersoon extOrgContactPersoon : getPersoonExterneOrganisatieContactPersonen())
		{
			if (extOrgContactPersoon.getExterneOrganisatieContactPersoon() != null
				&& extOrgContactPersoon.getExterneOrganisatieContactPersoon()
					.equals(contactPersoon))
				return true;
		}
		return false;
	}
}
