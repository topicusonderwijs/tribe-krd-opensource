package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VooropleidingSignaalcode extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public enum Signaalcode implements KeyValue
	{
		BrinNrOnglk("401", "Brin-nummer school vooropleiding niet gelijk"),
		DatumDimplomaOnglk("402", "Maand of dag diploma behaald niet gelijk");

		private String key;

		private String value;

		@Override
		public String getKey()
		{
			return key;
		}

		@Override
		public String getValue()
		{
			return value;
		}

		private Signaalcode(String key, String value)
		{
			this.key = key;
			this.value = value;
		}
	}

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Signaalcode signaalcode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vooropleiding")
	@Index(name = "idx_VooroplSignCde_Vooropl")
	private Vooropleiding vooropleiding;

	public Signaalcode getSignaalcode()
	{
		return signaalcode;
	}

	public void setSignaalcode(Signaalcode signaalcode)
	{
		this.signaalcode = signaalcode;
	}

	public Vooropleiding getVooropleiding()
	{
		return vooropleiding;
	}

	public void setVooropleiding(Vooropleiding vooropleiding)
	{
		this.vooropleiding = vooropleiding;
	}
}
