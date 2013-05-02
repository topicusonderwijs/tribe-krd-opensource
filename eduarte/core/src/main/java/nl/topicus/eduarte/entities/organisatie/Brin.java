package nl.topicus.eduarte.entities.organisatie;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Brin extends ExterneOrganisatie
{
	public static final String BRIN_FORMAT = "([0-9]{2}[a-zA-Z0-9]{2})([0-9]{1,2})?";

	private static final Pattern BRIN_REGEXP = Pattern.compile(BRIN_FORMAT);

	public static enum Onderwijssector
	{
		AK("Administratiekantoor", false),
		AMB("Ambulante Begeleiding"),
		AOC("Agrarisch Opleidings Centrum"),
		AOCV("AOC met vo onderwijs"),
		BAS("Basisschool"),
		BBAS("Buitenlandse basisschool"),
		BGRE("Bestuur REC", false),
		BGCD("Bestuur Centrale Dienst", false),
		BGCA("Bestuur t.b.v. Derden (CASO)", false),
		BGTC("Bestuur Technocentrum", false),
		BINS("Bestuur Van Instelling", false),
		BSM("BSM t.b.v. Inspectie", false),
		BVOS("Buitenlandse VO-school"),
		CASO("t.b.v. Derden (CASO)", false),
		CDPO("Centrale Dienst PO", false),
		DOV("Doveninstituut"),
		EDU("Educatief Centrum"),
		EDUV("Educatief Centrum + VO"),
		ERK("Erkende Instelling"),
		EXA("Examen Instelling BVE"),
		FAC("Faculteit", false),
		GJI("Gesloten Jeugdzorg Inrichting"),
		HAS("Hogere Agrarische instelling"),
		HBOS("Hogeschool"),
		ILOC("Inspectie Locatie"),
		ISWV("Inspectie Samenwerkingsverband", false),
		IBR("Instelling Bepaalde Richting"),
		IEB("Instelling Met Breedtegebrek"),
		JJI("Justitiele Jeugd Inrichting"),
		LOB("Kenniscentrum BVE", false),
		LAOV("Lagere Overheid", false),
		LOA("Landelijk Ondersteuning Act.", false),
		LOC("Locatie"),
		NAUT("Nautisch Onderwijs"),
		PROS("PRO-school"),
		REC("Regionaal Expertise Centrum", false),
		REFR("Inst. t.b.v. FRE's", false), // dit zijn de REC's
		RGN("Regionaal Netwerk", false),
		RGNE("Regionaal Netwerk Experimenten", false),
		RGNS("Startkwalificatie RGN", false),
		ROC("Regionaal Opleidings Centrum"),
		ROCV("Verticale School"),
		RVC("Regionale Verwijzings Commissie", false),
		RVT("Raad van Toezicht", false),
		SGM("Scholengemeenschap"),
		SWOP("Samenwerkingsovereenkomst PO", false),
		SWPO("Samenwerkingsverband PO", false),
		SBD("Schoolbegeleidingsdienst", false),
		SBAS("Speciaal Basisonderwijs"),
		SPEC("Speciale School"),
		SWVO("Samenwerkingsverband VO", false),
		TEC("Technocentrum", false),
		TVST("Tijdelijke Vestiging"),
		TOE("Toezichthouder", false),
		UNIV("Universiteit"),
		VAK("Vakschool"),
		VAKV("Vakschool met VO Vestigingen"),
		VOS("VO-school"),
		VST("Vestiging"),
		VSTS("Vestiging Spreidingsnoodzaak"),
		VSTZ("Vestiging Zorg");

		private final String omschrijving;

		private final boolean school;

		private Onderwijssector(String omschrijving)
		{
			this(omschrijving, true);
		}

		private Onderwijssector(String omschrijving, boolean school)
		{
			this.omschrijving = omschrijving;
			this.school = school;
		}

		public String getOmschrijving()
		{
			return omschrijving;
		}

		@Override
		public String toString()
		{
			return getOmschrijving();
		}

		public boolean isBasisonderwijs()
		{
			return equals(BAS) || equals(SBAS) || equals(BBAS);
		}

		public boolean isSchool()
		{
			return school;
		}
	}

	private static final long serialVersionUID = 1L;

	// nulllable vanwege opslaan in zelfde tabel als superclass
	@Column(nullable = true, length = 6)
	private String code;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private Onderwijssector onderwijssector;

	public Brin()
	{
	}

	public Brin(String brincode)
	{
		setCode(brincode);
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String brincode)
	{
		// KOL: Werkt niet met de brinimporter, er komt een brin binnen (RNE001) die niet
		// overeenkomt met de pattern.
		// if (!BRIN_REGEXP.matcher(brincode).matches())
		// {
		// Asserts.fail(brincode + " is geen geldige BRIN code");
		// }
		this.code = brincode;
	}

	/**
	 * Test of de meegegeven brincode voldoet aan 2 cijfers en 2 letters.
	 * 
	 * @param brincode
	 * @return true als de brincode voldoet aan de eisen, en anders false.
	 */
	public static boolean testBrincode(String brincode)
	{
		return BRIN_REGEXP.matcher(brincode).matches();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(50);
		builder.append(getCode()).append(" - ").append(getNaam());

		ExterneOrganisatieAdres adres = getFysiekAdres();
		if (adres != null)
		{
			builder.append(", ").append(adres.getAdres().getVolledigAdresOp1Regel());
		}
		return builder.toString();
	}

	public void setOnderwijssector(Onderwijssector onderwijssector)
	{
		this.onderwijssector = onderwijssector;
	}

	public Onderwijssector getOnderwijssector()
	{
		return onderwijssector;
	}

	/**
	 * Eerst testen of de string voldoet aan ([2 cijfers] [2 cijfers of letters] [2
	 * cijfers]) Als dat zo is worden de laatste 2 cijfers als integer terugggeven
	 * 
	 * @return vestigingsvolgnummer
	 */
	@Exportable
	public Integer getVestigingsVolgnummer()
	{
		String brincode = getCode();
		if (brincode != null)
		{
			Matcher matcher = BRIN_REGEXP.matcher(brincode);
			if (!matcher.matches())
			{
				Asserts.fail(brincode + " is geen geldige BRIN code");
			}
			String volgnummer = matcher.group(2);
			if (volgnummer != null)
			{
				return Integer.parseInt(volgnummer);
			}
		}
		return null;
	}

	public static Brin get(String code)
	{
		return DataAccessRegistry.getHelper(BrinDataAccessHelper.class).get(code);
	}
}