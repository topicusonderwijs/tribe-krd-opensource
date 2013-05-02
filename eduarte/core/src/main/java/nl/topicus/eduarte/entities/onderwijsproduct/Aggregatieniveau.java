package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Aggregatieniveau van een onderwijsproduct.
 * 
 * @author loite
 * 
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"code", "organisatie"}),
	@UniqueConstraint(columnNames = {"niveau", "organisatie"})})
public class Aggregatieniveau extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column()
	private int niveau;

	public Aggregatieniveau()
	{
	}

	public int getNiveau()
	{
		return niveau;
	}

	public void setNiveau(int niveau)
	{
		this.niveau = niveau;
	}

	public static Aggregatieniveau getAggregatieniveau(int niveau)
	{
		return DataAccessRegistry.getHelper(
			CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).getAggregatieniveau(
			niveau);
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
