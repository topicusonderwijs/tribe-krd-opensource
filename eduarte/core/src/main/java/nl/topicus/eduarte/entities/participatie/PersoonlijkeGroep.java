package nl.topicus.eduarte.entities.participatie;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.markup.html.form.CheckBox;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author ?
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonlijkeGroep extends Groep
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Omschrijving", order = 1, required = true, htmlClasses = {"unit_max"})
	@Column(nullable = false, length = 240)
	private String omschrijving;

	@AutoForm(include = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_PG_medewerker")
	private Medewerker medewerker;

	@AutoForm(include = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_PG_deelnemer")
	private Deelnemer deelnemer;

	/**
	 * Geeft aan of deze groep gedeeld wordt met andere medewerkers binnen dezelfde
	 * instelling.
	 */
	@AutoForm(label = "Publiek", order = 6, required = true, editorClass = CheckBox.class)
	@Column(nullable = false)
	private boolean gedeeld;

	public PersoonlijkeGroep()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	/**
	 * @return De entiteit (deelnemer, noiseGroep, persoonlijke groep) die aan deze
	 *         PersoonlijkeGroepDeelnemer is gekoppeld.
	 */
	@AutoForm(include = true, label = "Eigenaar", order = 2, readOnly = true)
	public IdObject getEigenaar()
	{
		if (getMedewerker() != null)
			return getMedewerker();
		return getDeelnemer();
	}

	/**
	 * @param entiteit
	 *            De entiteit (deelnemer, noiseGroep, persoonlijke groep) die gekoppeld
	 *            moet worden aan deze PersoonlijkeGroepDeelnemer.
	 */
	public void setEigenaar(IdObject entiteit)
	{
		if (entiteit instanceof Deelnemer)
			setDeelnemer((Deelnemer) entiteit);
		else if (entiteit instanceof Medewerker)
			setMedewerker((Medewerker) entiteit);
		else
			throw new IllegalArgumentException("Cannot set deelnemer of medewerker entiteit to "
				+ entiteit);
	}

	public boolean isGedeeld()
	{
		return gedeeld;
	}

	public void setGedeeld(boolean gedeeld)
	{
		this.gedeeld = gedeeld;
	}

	public String getGedeeldOmschrijving()
	{
		return isGedeeld() ? "Ja" : "Nee";
	}

	/**
	 * Zoekt de PersoonlijkeGroepDeelnemer voor de gegeven deelnemer.
	 * 
	 * @param pgdeelnemer
	 * @return De PersoonlijkeGroepDeelnemer of null.
	 */
	public PersoonlijkeGroepDeelnemer getDeelnemer(PersoonlijkeGroepDeelnemer pgdeelnemer)
	{
		for (PersoonlijkeGroepDeelnemer curDeelnemer : getDeelnemers())
		{
			if (curDeelnemer.equals(pgdeelnemer))
			{
				return curDeelnemer;
			}
		}
		return null;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	@Override
	public String toString()
	{
		return getCode() + " " + getOmschrijving();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersoonlijkeGroepDeelnemer> getDeelnamesUnordered()
	{
		return (List<PersoonlijkeGroepDeelnemer>) super.getDeelnamesUnordered();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersoonlijkeGroepDeelnemer> getDeelnemers()
	{
		return (List<PersoonlijkeGroepDeelnemer>) super.getDeelnemers();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersoonlijkeGroepDeelnemer> getDeelnemersOpDatum(Date datum)
	{
		return (List<PersoonlijkeGroepDeelnemer>) super.getDeelnemersOpDatum(datum);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersoonlijkeGroepDeelnemer> getDeelnemersOpPeildatum()
	{
		return (List<PersoonlijkeGroepDeelnemer>) super.getDeelnemersOpPeildatum();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersoonlijkeGroepDeelnemer> getDeelnemersOpPeildatumEnToekomst()
	{
		return (List<PersoonlijkeGroepDeelnemer>) super.getDeelnemersOpPeildatumEnToekomst();
	}

}
