package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.*;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefLandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class Fase extends CodeNaamActiefLandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_fase_hoofdfase")
	private Hoofdfase hoofdfase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "volgendeFase", nullable = true)
	@Index(name = "idx_fase_volgende")
	private Fase volgendeFase;

	public Hoofdfase getHoofdfase()
	{
		return hoofdfase;
	}

	public void setHoofdfase(Hoofdfase hoofdfase)
	{
		this.hoofdfase = hoofdfase;
	}

	public Fase getVolgendeFase()
	{
		return volgendeFase;
	}

	public void setVolgendeFase(Fase volgendeFase)
	{
		this.volgendeFase = volgendeFase;
	}
}
