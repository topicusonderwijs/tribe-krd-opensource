package nl.topicus.eduarte.krd.entities.bron;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;

@Entity
@Table(name = "BRON_AANLEVERPUNTEN")
public class BronAanleverpunt extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 2, nullable = false)
	private int nummer;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "aanleverpunt")
	private Set<BronAanleverpuntLocatie> locaties = new LinkedHashSet<BronAanleverpuntLocatie>();

	@OneToMany(mappedBy = "aanleverpunt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BronSchooljaarStatus> schooljaarStatussen = new ArrayList<BronSchooljaarStatus>();

	@Column(nullable = false)
	private int laatsteBatchNrBO;

	@Column(nullable = false)
	private int laatsteBatchNrED;

	@Column(nullable = false)
	private int laatsteBatchNrVAVO;

	@Column(nullable = false)
	private int laatsteBatchNrVO;

	public int getNummer()
	{
		return nummer;
	}

	public void setNummer(int nummer)
	{
		this.nummer = nummer;
	}

	public Set<BronAanleverpuntLocatie> getLocaties()
	{
		return locaties;
	}

	public void setLocaties(Set<BronAanleverpuntLocatie> locaties)
	{
		this.locaties = locaties;
	}

	public String getLocatiesOmschrijving()
	{
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (BronAanleverpuntLocatie locatie : locaties)
		{
			if (first)
			{
				builder.append(locatie.getLocatie().getNaam());
				first = false;
			}
			else
				builder.append(", ").append(locatie.getLocatie().getNaam());

		}
		return builder.toString();
	}

	public String getEmptyString()
	{
		return "";
	}

	/**
	 * Verhoogt het batchnummer modulo 1000.
	 */
	public int incBatchNrBO()
	{
		this.laatsteBatchNrBO = (this.laatsteBatchNrBO + 1) % 1000;
		return this.laatsteBatchNrBO;
	}

	/**
	 * Verlaag het batchnummer modulo 1000.
	 */
	public int decBatchNrBO()
	{
		this.laatsteBatchNrBO = (this.laatsteBatchNrBO - 1) % 1000;
		return this.laatsteBatchNrBO;
	}

	public void setLaatsteBatchNrBO(int laatsteBatchNrBO)
	{
		this.laatsteBatchNrBO = laatsteBatchNrBO;
	}

	public int getLaatsteBatchNrBO()
	{
		return laatsteBatchNrBO;
	}

	/**
	 * Verhoogt het batchnummer modulo 1000.
	 */
	public int incBatchNrED()
	{
		this.laatsteBatchNrED = (this.laatsteBatchNrED + 1) % 1000;
		return this.laatsteBatchNrED;
	}

	/**
	 * Verlaag het batchnummer modulo 1000.
	 */
	public int decBatchNrED()
	{
		this.laatsteBatchNrED = (this.laatsteBatchNrED - 1) % 1000;
		return this.laatsteBatchNrED;
	}

	public void setLaatsteBatchNrED(int laatsteBatchNrED)
	{
		this.laatsteBatchNrED = laatsteBatchNrED;
	}

	public int getLaatsteBatchNrED()
	{
		return laatsteBatchNrED;
	}

	/**
	 * Verhoogt het batchnummer modulo 1000.
	 */
	public int incBatchNrVAVO()
	{
		this.laatsteBatchNrVAVO = (this.laatsteBatchNrVAVO + 1) % 1000;
		return this.laatsteBatchNrVAVO;
	}

	/**
	 * Verlaag het batchnummer modulo 1000.
	 */
	public int decBatchNrVAVO()
	{
		this.laatsteBatchNrVAVO = (this.laatsteBatchNrVAVO - 1) % 1000;
		return this.laatsteBatchNrVAVO;
	}

	public void setLaatsteBatchNrVAVO(int laatsteBatchNrVAVO)
	{
		this.laatsteBatchNrVAVO = laatsteBatchNrVAVO;
	}

	public int getLaatsteBatchNrVAVO()
	{
		return laatsteBatchNrVAVO;
	}

	/**
	 * Verhoogt het batchnummer modulo 1000.
	 */
	public int incBatchNrVO()
	{
		this.laatsteBatchNrVO = (this.laatsteBatchNrVO + 1) % 1000;
		return this.laatsteBatchNrVO;
	}

	/**
	 * Verlaag het batchnummer modulo 1000.
	 */
	public int decBatchNrVO()
	{
		this.laatsteBatchNrVO = (this.laatsteBatchNrVO - 1) % 1000;
		return this.laatsteBatchNrVO;
	}

	public void setLaatsteBatchNrVO(int laatsteBatchNrVO)
	{
		this.laatsteBatchNrVO = laatsteBatchNrVO;
	}

	public int getLaatsteBatchNrVO()
	{
		return laatsteBatchNrVO;
	}

	public void setSchooljaarStatussen(List<BronSchooljaarStatus> schooljaarStatussen)
	{
		this.schooljaarStatussen = schooljaarStatussen;
	}

	public List<BronSchooljaarStatus> getSchooljaarStatussen()
	{
		return schooljaarStatussen;
	}

	public int getLaatseBatchNrSector(Sectordeel sector)
	{
		switch (sector)
		{
			case Beroepsonderwijs:
				return getLaatsteBatchNrBO();
			case Basiseducatie:
				return getLaatsteBatchNrED();
			case VAVO:
				return getLaatsteBatchNrVAVO();
			default:
				return 0;
		}
	}

	public void decLaatseBatchNrSector(Sectordeel sectordeel)
	{
		switch (sectordeel)
		{
			case Beroepsonderwijs:
				decBatchNrBO();
				break;
			case Basiseducatie:
				decBatchNrED();
				break;
			case VAVO:
				decBatchNrVAVO();
				break;
		}

	}
}
