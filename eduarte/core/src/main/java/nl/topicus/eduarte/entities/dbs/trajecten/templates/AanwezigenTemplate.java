package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AanwezigenTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AanwezigeType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = true)
	@ForeignKey(name = "FK_AanweTempl_persoon")
	@Index(name = "idx_AanweTempl_persoon")
	private Persoon persoon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handelingTemplate", nullable = true)
	@ForeignKey(name = "FK_AanweTempl_handTempl")
	@Index(name = "idx_AanweTempl_handTempl")
	private GeplandeBegeleidingsHandelingTemplate handeling;

	public AanwezigenTemplate()
	{
	}

	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	public GeplandeBegeleidingsHandelingTemplate getHandeling()
	{
		return handeling;
	}

	public void setHandeling(GeplandeBegeleidingsHandelingTemplate handeling)
	{
		this.handeling = handeling;
	}

	public AanwezigeType getType()
	{
		return type;
	}

	public void setType(AanwezigeType type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		if (AanwezigeType.GeselecteerdePersoon.equals(getType()))
			return getPersoon().getVolledigeNaam();

		return getType().toString();
	}
}
