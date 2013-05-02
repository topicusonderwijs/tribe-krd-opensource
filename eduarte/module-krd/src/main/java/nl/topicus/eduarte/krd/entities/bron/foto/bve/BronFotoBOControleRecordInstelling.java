package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.util.StringUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoBOControleRecordInstelling extends BronFotoBORecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer totaalAantalInschrijvingen;

	@Column(nullable = true)
	private Integer totaalAantalBPVs;

	@Column(nullable = true)
	private Integer totaalAantalExamens;

	public BronFotoBOControleRecordInstelling()
	{
	}

	public BronFotoBOControleRecordInstelling(String[] velden)
	{
		super(velden);
		totaalAantalInschrijvingen =
			StringUtil.isEmpty(velden[9]) ? null : Integer.valueOf(velden[9]);
		totaalAantalBPVs = StringUtil.isEmpty(velden[10]) ? null : Integer.valueOf(velden[10]);
		totaalAantalExamens = StringUtil.isEmpty(velden[11]) ? null : Integer.valueOf(velden[11]);
	}

	public Integer getTotaalAantalInschrijvingen()
	{
		return totaalAantalInschrijvingen;
	}

	public void setTotaalAantalInschrijvingen(Integer totaalAantalInschrijvingen)
	{
		this.totaalAantalInschrijvingen = totaalAantalInschrijvingen;
	}

	public Integer getTotaalAantalBPVs()
	{
		return totaalAantalBPVs;
	}

	public void setTotaalAantalBPVs(Integer totaalAantalBPVs)
	{
		this.totaalAantalBPVs = totaalAantalBPVs;
	}

	public Integer getTotaalAantalExamens()
	{
		return totaalAantalExamens;
	}

	public void setTotaalAantalExamens(Integer totaalAantalExamens)
	{
		this.totaalAantalExamens = totaalAantalExamens;
	}

}
