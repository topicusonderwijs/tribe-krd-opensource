package nl.topicus.eduarte.entities.bijlage;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Entiteit welke een enkel bijlage bevat.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "BijlageEntiteit", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public abstract class BijlageEntiteit extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "bijlage")
	@Index(name = "idx_BijlageEntiteit_bijlage")
	private Bijlage bijlage;

	public BijlageEntiteit()
	{
	}

	public Bijlage getBijlage()
	{
		return bijlage;
	}

	public void setBijlage(Bijlage bijlage)
	{
		this.bijlage = bijlage;
	}

	public abstract IBijlageKoppelEntiteit< ? extends BijlageEntiteit> getEntiteit();
}
