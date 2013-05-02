package nl.topicus.eduarte.entities.security.authentication;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon.ExterneOrganisatieContactPersoonSearchEditor;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Account voor deelnemers.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue(value = "ExtOrgContPersAccount")
public class ExterneOrganisatieContactPersoonAccount extends Account
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatieContPers", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_ExtOrgAccount_contPers")
	@AutoForm(htmlClasses = "unit_max", editorClass = ExterneOrganisatieContactPersoonSearchEditor.class)
	private ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon;

	public ExterneOrganisatieContactPersoonAccount()
	{
	}

	public ExterneOrganisatieContactPersoonAccount(
			ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon)
	{
		setExterneOrganisatieContactPersoon(externeOrganisatieContactPersoon);
	}

	@Override
	public Persoon getEigenaar()
	{
		return null;
	}

	@Override
	public Medewerker getMedewerker()
	{
		return null;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return null;
	}

	@Override
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatie()
	{
		return null;
	}

	@Override
	public boolean isValid()
	{
		return isActief() && getExterneOrganisatieContactPersoon() != null;
	}

	@Override
	public RechtenSoort getRechtenSoort()
	{
		return RechtenSoort.EXTERNEORGANISATIE;
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

}
