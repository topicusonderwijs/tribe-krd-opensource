package nl.topicus.eduarte.entities.landelijk;

import static nl.topicus.cobra.util.TimeUtil.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;

/**
 * Een <tt>Schooljaar</tt> is de periode tussen twee zomervakanties: van 1 augustus van
 * jaar X tot en met 31 juli van jaar X+1. Het schooljaar 2008/2009 loopt dus van
 * 1-aug-2008 tot en met 31-jul-2009. Deze klasse is immutable gemaakt aangezien het geen
 * pas heeft om van een schooljaar de properties te kunnen aanpassen.
 * 
 * <h2>Gebruik</h2>
 * 
 * Als je een veld van type <tt>Schooljaar</tt> wilt gebruiken in een persistent entity,
 * dan moet je de volgende annotatie opnemen:
 * 
 * <pre>
 * &#064;Type(type = &quot;nl.topicus.eduarte.hibernate.usertypes.SchooljaarUserType&quot;)
 * private Schooljaar schooljaar;
 * </pre>
 */
@Exportable
public final class Schooljaar implements IBeginEinddatumEntiteit, Comparable<Schooljaar>,
		Serializable
{
	private static final long serialVersionUID = 1L;

	private static final Pattern officieel = Pattern.compile("(\\d{4})[\\-/](\\d{4})");

	private static final Pattern kort = Pattern.compile("(\\d{2})[\\-/](\\d{2})");

	private static final int START_MAAND = Calendar.AUGUST;

	private static final int START_DAG = 1;

	private static final int EIND_MAAND = Calendar.JULY;

	private static final int EIND_DAG = 31;

	private final int startJaar;

	private final int eindJaar;

	private final Date begindatum;

	private final Date einddatum;

	private final String omschrijving;

	private final String afkorting;

	private static final ConcurrentHashMap<Integer, Schooljaar> schooljaren =
		new ConcurrentHashMap<Integer, Schooljaar>();

	private Schooljaar(int jaar)
	{
		startJaar = jaar;
		eindJaar = startJaar + 1;
		begindatum = TimeUtil.getInstance().asDate(startJaar, START_MAAND, START_DAG);
		einddatum = TimeUtil.getInstance().asDate(eindJaar, EIND_MAAND, EIND_DAG);
		omschrijving = startJaar + "/" + eindJaar;
		afkorting = String.format("%02d/%02d", startJaar % 100, eindJaar % 100);
	}

	private Schooljaar(Date datum)
	{
		this(bepaalStartJaarVanSchooljaarActiefOpDatum(datum));
	}

	/**
	 * Het start jaar van het schooljaar dat actief is op de peildatum: 30-4-2009 levert
	 * 2008 op (30 april 2009 valt in schooljaar 2008-2009), en 11-11-2009 levert 2009 op
	 * (11-11-2009 valt in schooljaar 2009-2010).
	 * 
	 * @return het start jaar van het schooljaar dat actief is op de peildatum.
	 */
	private static int bepaalStartJaarVanSchooljaarActiefOpDatum(Date datum)
	{
		TimeUtil kalender = TimeUtil.getInstance();
		int jaar = kalender.getYear(datum);
		Date startVanSchooljaarBeginnendInJaar = kalender.asDate(jaar, Calendar.AUGUST, 1);
		if (datum.before(startVanSchooljaarBeginnendInJaar))
			jaar = jaar - 1;
		return jaar;
	}

	/**
	 * @return het jaartal waarin dit schooljaar start (2008 van 2008/2009)
	 */
	public int getStartJaar()
	{
		return startJaar;
	}

	/**
	 * @return het jaartal waarin dit schooljaar eindigt (2009 van 2008/2009)
	 */
	public int getEindJaar()
	{
		return eindJaar;
	}

	/**
	 * @return de eerste dag dat dit schooljaar actief is (1 augustus van het jaar)
	 */
	@Override
	public Date getBegindatum()
	{
		return new Date(begindatum.getTime());
	}

	/**
	 * @return de laatste dag dat dit schooljaar actief is (31 juli van het jaar)
	 */
	@Override
	public Date getEinddatum()
	{
		return new Date(einddatum.getTime());
	}

	/**
	 * @return <tt>true</tt> als de peildatum tussen de <tt>begindatum</tt> en
	 *         <tt>einddatum</tt> van dit schooljaar ligt.
	 * @see nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit#isActief(java.util.Date)
	 */
	@Override
	public boolean isActief(Date peildatum)
	{
		return TimeUtil.getInstance().dateBetween(begindatum, einddatum, peildatum);
	}

	/**
	 * <b>NIET GEBRUIKEN! DEZE KLASSE IS IMMUTABLE</b>
	 */
	@Override
	public void setBegindatum(Date begindatum)
	{
		throw new UnsupportedOperationException("Schooljaar kan niet aangepast worden");
	}

	/**
	 * <b>NIET GEBRUIKEN! DEZE KLASSE IS IMMUTABLE</b>
	 */
	@Override
	public void setEinddatum(Date einddatum)
	{
		throw new UnsupportedOperationException("Schooljaar kan niet aangepast worden");
	}

	/**
	 * @return een lange omschrijving van het schooljaar <tt>"2008/2009"</tt>
	 */
	@Exportable
	public String getOmschrijving()
	{
		return omschrijving;
	}

	/**
	 * @return een verkorte omschrijving van het schooljaar <tt>"08/09"</tt>
	 */
	@Exportable
	public String getAfkorting()
	{
		return afkorting;
	}

	@Override
	public String toString()
	{
		return omschrijving;
	}

	/**
	 * @return het schooljaar volgend op dit schooljaar.
	 */
	public Schooljaar getVolgendSchooljaar()
	{
		return Schooljaar.valueOf(startJaar + 1);
	}

	/**
	 * @return het schooljaar dat voor dit schooljaar ligt
	 */
	public Schooljaar getVorigSchooljaar()
	{
		return Schooljaar.valueOf(startJaar - 1);
	}

	/**
	 * @return <code>true</code> als de systeemdatum na de einddatum van het schooljaar
	 *         ligt
	 */
	public boolean isAfgelopen()
	{
		Date current = TimeUtil.getInstance().currentDate();
		return current.after(getEinddatum());
	}

	/**
	 * @return het schooljaar dat actief is op de systeemdatum
	 */
	public static Schooljaar huidigSchooljaar()
	{
		return valueOf(new Date());
	}

	/**
	 * @return het schooljaar dat actief is op de gezette peildatum <b>of</b> vandaag (als
	 *         er geen peildatum gezet is)
	 */
	public static Schooljaar schooljaarOpPeildatum()
	{
		return valueOf(EduArteContext.get().getPeildatumOfVandaag());
	}

	/**
	 * @return het schooljaar dat actief is op de datum
	 */
	public static Schooljaar valueOf(Date datum)
	{
		int startJaar = bepaalStartJaarVanSchooljaarActiefOpDatum(datum);
		return valueOf(startJaar);
	}

	/**
	 * @return het schooljaar dat start in het <tt>startJaar</tt>
	 */
	public static Schooljaar valueOf(int startJaar)
	{
		Schooljaar value = new Schooljaar(startJaar);
		Schooljaar schooljaar = schooljaren.putIfAbsent(startJaar, value);
		if (schooljaar == null)
			schooljaar = value;
		return schooljaar;
	}

	/**
	 * Parst de <tt>value</tt> en geeft een <tt>Schooljaar</tt> terug als de
	 * <tt>value</tt> een correct geformatteerd schooljaar bevat. Schooljaren kunnen lang
	 * of kort worden weergegeven: 2008/2009 of 08/09. Het scheidingsteken mag varieren
	 * tussen het <tt>-</tt> en <tt>/</tt> karakter.
	 * 
	 * @param value
	 *            de waarde die geparst moet worden in het formaat xxxx/yyyy
	 * @return het schooljaar
	 */
	public static Schooljaar parse(String value)
	{
		Pattern[] patterns = new Pattern[] {officieel, kort};
		for (Pattern pattern : patterns)
		{
			Matcher matcher = pattern.matcher(value);
			if (!matcher.matches())
			{
				continue;
			}

			String startJaar = matcher.group(1);

			if (startJaar == null)
				return null;

			TimeUtil kalender = TimeUtil.getInstance();
			Date date = kalender.parseDateString(String.format("01-08-%s", startJaar));
			return Schooljaar.valueOf(date);
		}
		return null;
	}

	@Override
	public int compareTo(Schooljaar other)
	{
		return this.startJaar - other.startJaar;
	}

	@Override
	public int hashCode()
	{
		return startJaar;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Schooljaar other = (Schooljaar) obj;

		return startJaar == other.startJaar;
	}

	@Override
	public Date getEinddatumNotNull()
	{
		return einddatum;
	}

	@Override
	public boolean isActief()
	{
		return false;
	}

	/**
	 * Geeft 1 oktober van het beginjaar van het schooljaar terug. Dit is de peildatum
	 * voor BRON voor VO en de eerste peildatum voor BO.
	 */
	public Date getEenOktober()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(begindatum);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Geeft 1 januari van het eindjaar van het schooljaar terug. Dit is een peildatum
	 * voor BPVs.
	 */
	public Date getEenJanuari()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(einddatum);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Geeft 1 februari van het eindjaar van het schooljaar terug. Dit is de tweede
	 * peildatum van een schooljaar voor BRON voor BO.
	 */
	public Date getEenFebruari()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(einddatum);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Geeft alle peildata voor de BVE sector die relevant zijn voor dit schooljaar.
	 */
	public List<Date> getBoPeildata()
	{
		return getBoPeildataOpOfNa(begindatum);
	}

	/**
	 * Geeft alle peildata de BVE sector die relevant zijn in dit schooljaar en op of na
	 * de datum liggen.
	 */
	public List<Date> getBoPeildataOpOfNa(Date datum)
	{
		ArrayList<Date> result = new ArrayList<Date>();
		if (TimeUtil.opOfVoor(datum, getEenOktober()))
			result.add(getEenOktober());
		if (TimeUtil.opOfVoor(datum, getEenFebruari()))
			result.add(getEenFebruari());
		return result;
	}

	public boolean voor(Schooljaar anderSchooljaar)
	{
		return anderSchooljaar != null && this.startJaar < anderSchooljaar.startJaar;
	}

	public boolean gelijkOfVoor(Schooljaar anderSchooljaar)
	{
		return anderSchooljaar != null && this.startJaar <= anderSchooljaar.startJaar;
	}

	public boolean na(Schooljaar anderSchooljaar)
	{
		return anderSchooljaar != null && this.startJaar > anderSchooljaar.startJaar;
	}

	public boolean gelijkOfNa(Schooljaar anderSchooljaar)
	{
		return anderSchooljaar != null && this.startJaar >= anderSchooljaar.startJaar;
	}

	public static List<Date> allePeildataTussen(Date begindatum, Date einddatum)
	{
		ArrayList<Date> result = new ArrayList<Date>();

		Schooljaar beginSchooljaar = valueOf(begindatum);
		Schooljaar eindSchooljaar = valueOf(einddatum);

		while (beginSchooljaar.gelijkOfVoor(eindSchooljaar))
		{
			List<Date> boPeildata = beginSchooljaar.getBoPeildata();
			for (Date teldatum : boPeildata)
			{
				if (tussen(teldatum, begindatum, einddatum))
					result.add(teldatum);
			}
			beginSchooljaar = beginSchooljaar.getVolgendSchooljaar();
		}
		return result;
	}

	public static List<Date> allePeildataVoorSchooljarenTussen(Date begindatum, Date einddatum)
	{
		ArrayList<Date> result = new ArrayList<Date>();

		Schooljaar beginSchooljaar = valueOf(begindatum);
		Schooljaar eindSchooljaar = valueOf(einddatum);

		while (beginSchooljaar.gelijkOfVoor(eindSchooljaar))
		{
			result.addAll(beginSchooljaar.getBoPeildataOpOfNa(begindatum));
			beginSchooljaar = beginSchooljaar.getVolgendSchooljaar();
		}
		return result;
	}

	public static List<Date> allePeildataTotEnMetHuidigSchooljaarVanaf(Date datum)
	{
		ArrayList<Date> result = new ArrayList<Date>();

		Schooljaar schooljaar = valueOf(datum);
		Schooljaar huidigSchooljaar = huidigSchooljaar();

		while (schooljaar.gelijkOfVoor(huidigSchooljaar))
		{
			result.addAll(schooljaar.getBoPeildataOpOfNa(datum));
			schooljaar = schooljaar.getVolgendSchooljaar();
		}
		return result;
	}
}
