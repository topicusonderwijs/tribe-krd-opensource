package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite/schimmel
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class SoortOnderwijsproduct extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Leiden onderwijsproducten van dit type tot een summatief resultaat?
	 */
	@Column(nullable = false)
	private boolean summatief = true;

	/**
	 * Is dit onderwijsproduct een BPV of Stage (e.g. kunnen er stages/bpv's gedaan worden
	 * voor dit onderwijsproduct)
	 */
	@Column(nullable = false)
	private boolean stage = false;

	public SoortOnderwijsproduct()
	{
	}

	public boolean isSummatief()
	{
		return summatief;
	}

	public void setSummatief(boolean summatief)
	{
		this.summatief = summatief;
	}

	public boolean isStage()
	{
		return stage;
	}

	public void setStage(boolean stage)
	{
		this.stage = stage;
	}
}
