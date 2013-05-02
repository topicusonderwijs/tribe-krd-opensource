package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.StringUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Record in een BO fotobestand.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoBORecord extends BronFotoRecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer bpvVolgnummer;

	@Column(nullable = true, length = 5)
	private String codeDeelkwalificatieBehaald;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "boInschrijvingRecord")
	@Index(name = "idx_BronFotoReco_boInschrijvi")
	private BronFotoBOInschrijvingRecord inschrijvingRecord;

	public BronFotoBORecord()
	{
	}

	public BronFotoBORecord(String[] velden)
	{
		super(velden);
		bpvVolgnummer = StringUtil.isEmpty(velden[6]) ? null : Integer.valueOf(velden[6]);
		codeDeelkwalificatieBehaald = velden[7];
	}

	public Integer getBpvVolgnummer()
	{
		return bpvVolgnummer;
	}

	public void setBpvVolgnummer(Integer bpvVolgnummer)
	{
		this.bpvVolgnummer = bpvVolgnummer;
	}

	public String getCodeDeelkwalificatieBehaald()
	{
		return codeDeelkwalificatieBehaald;
	}

	public void setCodeDeelkwalificatieBehaald(String codeDeelkwalificatieBehaald)
	{
		this.codeDeelkwalificatieBehaald = codeDeelkwalificatieBehaald;
	}

	public BronFotoBOInschrijvingRecord getInschrijvingRecord()
	{
		return inschrijvingRecord;
	}

	public void setInschrijvingRecord(BronFotoBOInschrijvingRecord inschrijvingRecord)
	{
		this.inschrijvingRecord = inschrijvingRecord;
	}
}
