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
public class BronFotoVEControleRecordInschrijving extends BronFotoVERecord
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer totaalAantalInschrijvingen;

	@Column(nullable = true)
	private Integer totaalAantalVakkenEducatie;

	@Column(nullable = true)
	private Integer totaalAantalVakkenVavo;

	@Column(nullable = true)
	private Integer totaalAantalVaardigheden;

	@Column(nullable = true)
	private Integer totaalAantalExamensVavo;

	@Column(nullable = true)
	private Integer totaalAantalDiplomasEducatie;

	public BronFotoVEControleRecordInschrijving()
	{
	}

	public BronFotoVEControleRecordInschrijving(String[] velden)
	{
		super(velden);
		totaalAantalInschrijvingen =
			StringUtil.isEmpty(velden[12]) ? null : Integer.valueOf(velden[12]);
		totaalAantalVakkenEducatie =
			StringUtil.isEmpty(velden[13]) ? null : Integer.valueOf(velden[13]);
		totaalAantalVakkenVavo =
			StringUtil.isEmpty(velden[14]) ? null : Integer.valueOf(velden[14]);
		totaalAantalVaardigheden =
			StringUtil.isEmpty(velden[15]) ? null : Integer.valueOf(velden[15]);
		totaalAantalExamensVavo =
			StringUtil.isEmpty(velden[16]) ? null : Integer.valueOf(velden[16]);
		totaalAantalDiplomasEducatie =
			StringUtil.isEmpty(velden[17]) ? null : Integer.valueOf(velden[17]);
	}

	public Integer getTotaalAantalInschrijvingen()
	{
		return totaalAantalInschrijvingen;
	}

	public void setTotaalAantalInschrijvingen(Integer totaalAantalInschrijvingen)
	{
		this.totaalAantalInschrijvingen = totaalAantalInschrijvingen;
	}

	public Integer getTotaalAantalVakkenEducatie()
	{
		return totaalAantalVakkenEducatie;
	}

	public void setTotaalAantalVakkenEducatie(Integer totaalAantalVakkenEducatie)
	{
		this.totaalAantalVakkenEducatie = totaalAantalVakkenEducatie;
	}

	public Integer getTotaalAantalVakkenVavo()
	{
		return totaalAantalVakkenVavo;
	}

	public void setTotaalAantalVakkenVavo(Integer totaalAantalVakkenVavo)
	{
		this.totaalAantalVakkenVavo = totaalAantalVakkenVavo;
	}

	public Integer getTotaalAantalVaardigheden()
	{
		return totaalAantalVaardigheden;
	}

	public void setTotaalAantalVaardigheden(Integer totaalAantalVaardigheden)
	{
		this.totaalAantalVaardigheden = totaalAantalVaardigheden;
	}

	public Integer getTotaalAantalExamensVavo()
	{
		return totaalAantalExamensVavo;
	}

	public void setTotaalAantalExamensVavo(Integer totaalAantalExamensVavo)
	{
		this.totaalAantalExamensVavo = totaalAantalExamensVavo;
	}

	public Integer getTotaalAantalDiplomasEducatie()
	{
		return totaalAantalDiplomasEducatie;
	}

	public void setTotaalAantalDiplomasEducatie(Integer totaalAantalDiplomasEducatie)
	{
		this.totaalAantalDiplomasEducatie = totaalAantalDiplomasEducatie;
	}

}
