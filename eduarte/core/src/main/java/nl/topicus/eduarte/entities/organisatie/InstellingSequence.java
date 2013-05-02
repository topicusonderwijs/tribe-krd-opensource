package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Table;

/**
 * Bewaart gegevens voor de custom SequenceGenerators per instelling.
 * 
 * @author Joost Limburg
 */
@Entity()
@Table(appliesTo = "InstellingSequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"naam",
		"organisatie"})})
public class InstellingSequence extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	// Naam van de sequence.
	@Column(nullable = false)
	private String naam;

	// Maximale waarde voor de sequence.
	@Column(nullable = false)
	private Long maximum;

	// Startwaarde van de sequence.
	@Column(nullable = false)
	private Long startWaarde;

	public InstellingSequence(Instelling instelling)
	{
		super(instelling);
	}

	public InstellingSequence()
	{
		super();
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Long getMaximum()
	{
		return maximum;
	}

	public void setMaximum(Long maximum)
	{
		this.maximum = maximum;
	}

	public Long getStartWaarde()
	{
		return startWaarde;
	}

	public void setStartWaarde(Long startWaarde)
	{
		this.startWaarde = startWaarde;
	}
}
