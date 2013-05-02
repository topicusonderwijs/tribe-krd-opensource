package nl.topicus.eduarte.entities.security.authentication;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.DeelnemerSearchEditor;

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
public class DeelnemerAccount extends Account
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_DlnAccount_deelnemer")
	@AutoForm(htmlClasses = "unit_max", editorClass = DeelnemerSearchEditor.class)
	private Deelnemer deelnemer;

	public DeelnemerAccount()
	{
	}

	public DeelnemerAccount(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	public Persoon getEigenaar()
	{
		return getDeelnemer().getPersoon();
	}

	@Override
	public Medewerker getMedewerker()
	{
		return null;
	}

	@Override
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatie()
	{
		return getDeelnemer();
	}

	@Override
	public boolean isValid()
	{
		return isActief() && getDeelnemer() != null
			&& getDeelnemer().isActief(TimeUtil.getInstance().currentDateTime());
	}

	@Override
	public RechtenSoort getRechtenSoort()
	{
		return RechtenSoort.DEELNEMER;
	}
}
