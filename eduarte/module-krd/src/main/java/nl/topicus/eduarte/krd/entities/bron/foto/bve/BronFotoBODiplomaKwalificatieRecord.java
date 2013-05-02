package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.util.TimeUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoBODiplomaKwalificatieRecord extends BronFotoBORecord
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumKwalificatieBehaald;

	@Column(nullable = true)
	private Boolean indicatieBekostigingDiploma;

	public BronFotoBODiplomaKwalificatieRecord()
	{
	}

	public BronFotoBODiplomaKwalificatieRecord(String velden[],
			BronFotoBOInschrijvingRecord inschrijvingRecord)
	{
		super(velden);
		setInschrijvingRecord(inschrijvingRecord);
		datumKwalificatieBehaald = TimeUtil.getInstance().isoStringAsDate(velden[9]);
		indicatieBekostigingDiploma = parseBronBoolean(velden[10]);
	}

	public Date getDatumKwalificatieBehaald()
	{
		return datumKwalificatieBehaald;
	}

	public void setDatumKwalificatieBehaald(Date datumKwalificatieBehaald)
	{
		this.datumKwalificatieBehaald = datumKwalificatieBehaald;
	}

	public Boolean getIndicatieBekostigingDiploma()
	{
		return indicatieBekostigingDiploma;
	}

	public void setIndicatieBekostigingDiploma(Boolean indicatieBekostigingDiploma)
	{
		this.indicatieBekostigingDiploma = indicatieBekostigingDiploma;
	}

	public String getIndicatieBekostigingDiplomaOmschrijving()
	{
		return getIndicatieBekostigingDiploma().booleanValue() ? "Ja" : "Nee";
	}

	/**
	 * @return true als dit diploma voor een deelkwalificatie is.
	 */
	public boolean isDeelkwalificatieDiploma()
	{
		return getCodeDeelkwalificatieBehaald() != null
			&& getCodeDeelkwalificatieBehaald().startsWith("5");
	}
}
