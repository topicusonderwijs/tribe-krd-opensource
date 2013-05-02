package nl.topicus.eduarte.krd.entities.bron;

import javax.persistence.*;

import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

/**
 * Bepaalt de status van de BRON communicatie tussen instelling en IB-groep voor een
 * schooljaar.
 */
@Entity
@javax.persistence.Table(name = "BRON_SCHOOLJAARSTATUSSEN", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"organisatie", "aanleverpunt", "schooljaar"})})
public class BronSchooljaarStatus extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aanleverpunt")
	@Index(name = "idx_SCHOOLJ_STATUS_aanleverp")
	private BronAanleverpunt aanleverpunt;

	@Type(type = "nl.topicus.eduarte.hibernate.usertypes.SchooljaarUserType")
	@Column(nullable = false, length = 9)
	private Schooljaar schooljaar;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronStatus statusBO = BronStatus.GegevensWordenIngevoerd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronStatus statusED = BronStatus.GegevensWordenIngevoerd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronStatus statusVAVO = BronStatus.GegevensWordenIngevoerd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronStatus statusVO = BronStatus.GegevensWordenIngevoerd;

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = schooljaar;
	}

	public Schooljaar getSchooljaar()
	{
		return schooljaar;
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return aanleverpunt;
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = aanleverpunt;
	}

	public BronStatus getStatusBO()
	{
		return statusBO;
	}

	public void setStatusBO(BronStatus statusBO)
	{
		this.statusBO = statusBO;
	}

	public BronStatus getStatusED()
	{
		return statusED;
	}

	public void setStatusED(BronStatus statusED)
	{
		this.statusED = statusED;
	}

	public BronStatus getStatusVAVO()
	{
		return statusVAVO;
	}

	public void setStatusVAVO(BronStatus statusVAVO)
	{
		this.statusVAVO = statusVAVO;
	}

	public BronStatus getStatusVO()
	{
		return statusVO;
	}

	public void setStatusVO(BronStatus statusVO)
	{
		this.statusVO = statusVO;
	}

	public BronStatus getStatus(BronOnderwijssoort bronOnderwijssoort)
	{
		if (bronOnderwijssoort == null)
			return null;
		switch (bronOnderwijssoort)
		{
			case BEROEPSONDERWIJS:
				return getStatusBO();
			case EDUCATIE:
				return getStatusED();
			case VAVO:
				return getStatusVAVO();
			case VOORTGEZETONDERWIJS:
				return getStatusVO();
			default:
				return null;
		}
	}
}
