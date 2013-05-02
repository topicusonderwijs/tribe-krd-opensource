package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.*;

import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Niveau;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVEVaardigheidNT2Record extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private NT2Niveau startniveau;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private NT2Niveau eindniveau;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "vakRecord")
	@Index(name = "idx_BronFotoRecord_vakRecord")
	private BronFotoVEVakBasiseducatieRecord vakRecord;

	public BronFotoVEVaardigheidNT2Record()
	{
	}

	public BronFotoVEVaardigheidNT2Record(String[] velden,
			BronFotoVEVakBasiseducatieRecord vakRecord)
	{
		super(velden);
		setVakRecord(vakRecord);
		startniveau = NT2Niveau.A1BasicUserBreakthrough.parse(velden[12]);
		eindniveau = NT2Niveau.A1BasicUserBreakthrough.parse(velden[13]);
	}

	public NT2Niveau getStartniveau()
	{
		return startniveau;
	}

	public void setStartniveau(NT2Niveau startniveau)
	{
		this.startniveau = startniveau;
	}

	public NT2Niveau getEindniveau()
	{
		return eindniveau;
	}

	public void setEindniveau(NT2Niveau eindniveau)
	{
		this.eindniveau = eindniveau;
	}

	public BronFotoVEVakBasiseducatieRecord getVakRecord()
	{
		return vakRecord;
	}

	public void setVakRecord(BronFotoVEVakBasiseducatieRecord vakRecord)
	{
		this.vakRecord = vakRecord;
	}

}
