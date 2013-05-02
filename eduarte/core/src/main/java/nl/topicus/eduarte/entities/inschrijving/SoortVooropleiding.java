package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@IsViewWhenOnNoise
public class SoortVooropleiding extends CodeNaamActiefInstellingEntiteit implements
		Comparable<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	public static enum SoortOnderwijs
	{
		Geen("01", "Geen of niet voltooid basisond. (ook funct. Analfabeet)", false),
		Basisonderwijs("02", "Voltooid basisonderwijs (geen analfabeet)", false),
		Basisvorming("03", "Basisvorming (alg. leerj. AVO/VBO/VMBO)", false),
		VMBO("04", "VMBO excl. Theoretische leerweg (VBO)", false),
		VMBOTL("05", "Theoretische leerweg VMBO (MAVO)", false),
		HAVO("06", "HAVO", true),
		VWO("07", "VWO", true),
		MBO12("08", "MBO niveau 1-2", false),
		MBO34("09", "MBO niveau 3-4", true),
		PropHBO("10", "Propedeuse HBO", true),
		HBO("11", "HBO", true),
		Colloquium("12", "Colloquium doctum", true),
		BeschikkingWO("13", "Beschikking toelating WO", true),
		PropWO("14", "Propedeuse WO", true),
		Bachelor("15", "Bachelor WO", true),
		Master("16", "Master WO", true);

		private final String code;

		private final String naam;

		private final boolean startkwalificatie;

		private SoortOnderwijs(String code, String naam, boolean startkwalificatie)
		{
			this.code = code;
			this.naam = naam;
			this.startkwalificatie = startkwalificatie;
		}

		public String getCode()
		{
			return code;
		}

		public String getNaam()
		{
			return naam;
		}

		public boolean isBasisOfVoortgezetOnderwijs()
		{
			return (SoortOnderwijs.Geen.ordinal() <= this.ordinal())
				&& this.ordinal() <= SoortOnderwijs.VWO.ordinal();
		}

		public boolean isStartkwalificatie()
		{
			return startkwalificatie;
		}

		@Override
		public String toString()
		{
			return getCode() + " " + getNaam();
		}
	}

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SoortOnderwijs soortOnderwijsMetDiploma;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SoortOnderwijs soortOnderwijsZonderDiploma;

	public SoortVooropleiding()
	{
	}

	public void setSoortOnderwijsMetDiploma(SoortOnderwijs soortOnderwijsMetDiploma)
	{
		this.soortOnderwijsMetDiploma = soortOnderwijsMetDiploma;
	}

	public SoortOnderwijs getSoortOnderwijsMetDiploma()
	{
		return soortOnderwijsMetDiploma;
	}

	public void setSoortOnderwijsZonderDiploma(SoortOnderwijs soortOnderwijsZonderDiploma)
	{
		this.soortOnderwijsZonderDiploma = soortOnderwijsZonderDiploma;
	}

	public SoortOnderwijs getSoortOnderwijsZonderDiploma()
	{
		return soortOnderwijsZonderDiploma;
	}

	@Override
	public int compareTo(SoortVooropleiding o)
	{
		return soortOnderwijsMetDiploma.compareTo(o.soortOnderwijsMetDiploma);
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
