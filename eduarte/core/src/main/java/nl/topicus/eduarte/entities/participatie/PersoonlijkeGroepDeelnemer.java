package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Lid van een persoonlijke groep. Een lid kan een deelnemer, een noise-groep of een
 * andere persoonlijke groep zijn. Voor het ophalen van alle deelnemers (leerlingen) die
 * in een persoonlijke groep zitten, kan gebruik gemaakt worden van de entiteit
 * DeelnemerPersoonlijkeGroep. Deze verwijst naar een view die de geneste relaties ook
 * ophaalt.
 * 
 * @author loite
 */

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonlijkeGroepDeelnemer extends Groepsdeelname
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bevatGroep", nullable = true)
	@Index(name = "idx_PGD_bevatGroep")
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private Groep bevatGroep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract", nullable = true)
	@Index(name = "idx_PGD_contract")
	private Contract contract;

	public PersoonlijkeGroepDeelnemer()
	{
	}

	public Groep getBevatGroep()
	{
		return bevatGroep;
	}

	public void setBevatGroep(Groep bevatGroep)
	{
		this.bevatGroep = bevatGroep;
	}

	public IdObject getDeelnemerEntiteit()
	{
		if (getDeelnemer() != null)
			return getDeelnemer();
		else
			return getBevatGroep();
	}

	public void setDeelnemerEntiteit(IdObject entiteit)
	{
		if (entiteit instanceof Deelnemer)
			setDeelnemer((Deelnemer) entiteit);
		else if (entiteit instanceof Groep)
			setBevatGroep((Groep) entiteit);
		else
			throw new IllegalArgumentException("Cannot set deelnemer entiteit to " + entiteit);
	}

	@Override
	public String toString()
	{
		return getDeelnemerEntiteit() == null ? "" : getDeelnemerEntiteit().toString();
	}

	public Contract getContract()
	{
		return contract;
	}

	public void setContract(Contract contract)
	{
		this.contract = contract;
	}

}
