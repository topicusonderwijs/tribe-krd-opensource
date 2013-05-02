package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Index;

@Entity
public class Beoordeling extends CompetentieNiveauVerzameling
{
	private static final long serialVersionUID = 1L;

	/**
	 * De medewerker die dit Competentieniveau invult
	 */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_beoordeling_medewerker")
	private Medewerker medewerker;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "opgenomenIn", nullable = true)
	@Index(name = "idx_beoordeling_opgenomenIn")
	private Beoordeling opgenomenIn;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private BeoordelingsType type;

	public Beoordeling()
	{
	}

	public BeoordelingsType getType()
	{
		return type;
	}

	public void setType(BeoordelingsType type)
	{
		this.type = type;
	}

	public Beoordeling getOpgenomenIn()
	{
		return opgenomenIn;
	}

	public void setOpgenomenIn(Beoordeling opgenomenIn)
	{
		this.opgenomenIn = opgenomenIn;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	@Override
	public String getTypeNaam()
	{
		// no NPE when type == null
		return new StringBuilder().append(type).toString();
	}
}
