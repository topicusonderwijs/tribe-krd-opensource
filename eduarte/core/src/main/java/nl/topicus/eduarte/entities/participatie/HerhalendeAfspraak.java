package nl.topicus.eduarte.entities.participatie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakHerhalingDag;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakHerhalingType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een herhalende afspraak geeft aan dat een afspraak zichzelf om de X weken herhaalt.
 * Alle instanties van de herhalende afspraak verwijzen naar dit object. Wanneer een
 * instantie aangepast wordt, kan de gebruiker ervoor kiezen om alleen de instantie aan te
 * passen, of alle (nog niet uitgevoerde) instanties aan te passen. Lessen uit een
 * basisrooster zijn voorbeelden van herhalende afspraken.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class HerhalendeAfspraak extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date beginDatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date eindDatum;

	@Column(nullable = true)
	private Integer maxHerhalingen;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AfspraakHerhalingType type;

	@Column(nullable = false)
	private int dagen;

	@Column(nullable = false)
	private int skip;

	/**
	 * Het aantal tijdeenheden tussen elke afspraak.
	 */
	@Column(nullable = false)
	private int cyclus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "herhalendeAfspraak")
	@OrderBy("beginDatumTijd")
	private List<Afspraak> afspraken = new ArrayList<Afspraak>();

	public HerhalendeAfspraak()
	{
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public int getCyclus()
	{
		return cyclus;
	}

	public void setCyclus(int cyclus)
	{
		this.cyclus = cyclus;
	}

	public List<Afspraak> getAfspraken()
	{
		return afspraken;
	}

	public void setAfspraken(List<Afspraak> afspraken)
	{
		this.afspraken = afspraken;
	}

	public AfspraakHerhalingType getType()
	{
		return type;
	}

	public void setType(AfspraakHerhalingType type)
	{
		this.type = type;
	}

	public int getDagen()
	{
		return dagen;
	}

	public void setDagen(int dagen)
	{
		this.dagen = dagen;
	}

	public Set<AfspraakHerhalingDag> getDagenSet()
	{
		List<AfspraakHerhalingDag> ret = new LinkedList<AfspraakHerhalingDag>();
		for (AfspraakHerhalingDag curDag : AfspraakHerhalingDag.values())
			if ((getDagen() & (1 << curDag.ordinal())) > 0)
				ret.add(curDag);
		return EnumSet.copyOf(ret);
	}

	public void setDagenSet(Set<AfspraakHerhalingDag> dagenSet)
	{
		int calcDagen = 0;
		for (AfspraakHerhalingDag curDag : dagenSet)
		{
			calcDagen |= 1 << curDag.ordinal();
		}
		setDagen(calcDagen);
	}

	public void setDagInSet(AfspraakHerhalingDag dag, boolean select)
	{
		if (select)
			setDagen(getDagen() | 1 << dag.ordinal());
		else
			setDagen(getDagen() & ~(1 << dag.ordinal()));
	}

	public boolean getDagInSet(AfspraakHerhalingDag dag)
	{
		return (getDagen() & 1 << dag.ordinal()) > 0;
	}

	public AfspraakHerhalingDag getEnkeleDag()
	{
		for (AfspraakHerhalingDag curDag : AfspraakHerhalingDag.values())
			if ((getDagen() & (1 << curDag.ordinal())) > 0)
				return curDag;
		return null;
	}

	public void setEnkeleDag(AfspraakHerhalingDag dag)
	{
		setDagenSet(EnumSet.of(dag));
	}

	public AfspraakHerhalingDag getEnkeleDagZonderWeekdagen()
	{
		for (AfspraakHerhalingDag curDag : EnumSet.of(AfspraakHerhalingDag.DAG,
			AfspraakHerhalingDag.WERKDAG))
			if ((getDagen() & (1 << curDag.ordinal())) > 0)
				return curDag;
		return null;
	}

	public void setEnkeleDagZonderWeekdagen(AfspraakHerhalingDag dag)
	{
		setEnkeleDag(dag);
	}

	public int getSkip()
	{
		return skip;
	}

	public void setSkip(int skip)
	{
		this.skip = skip;
	}

	public void setMaxHerhalingen(Integer maxHerhalingen)
	{
		this.maxHerhalingen = maxHerhalingen;
	}

	public Integer getMaxHerhalingen()
	{
		return maxHerhalingen;
	}

	@Override
	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String startStr;
		String eindStr = "";
		if (afspraken.isEmpty())
		{
			if (getEindDatum() == null)
			{
				startStr = "Vanaf " + sdf.format(getBeginDatum());
				eindStr = " tot " + getMaxHerhalingen() + " herhalingen";
			}
			else
				startStr =
					"Van " + sdf.format(getBeginDatum()) + " t/m " + sdf.format(getEindDatum());
		}
		else
		{
			Afspraak beginAfspraak = getAfspraken().get(0);
			Afspraak eindAfspraak = getAfspraken().get(getAfspraken().size() - 1);
			startStr =
				"Van " + sdf.format(beginAfspraak.getBeginDatum()) + " t/m "
					+ sdf.format(eindAfspraak.getBeginDatum());
		}
		switch (getType())
		{
			case MAANDELIJKS:
				return startStr
					+ (getCyclus() == 1 ? " maandelijks" : " elke " + getCyclus() + " maanden")
					+ " op de " + getSkip() + "e " + getEnkeleDag() + " van de maand" + eindStr
					+ ".";
			case WEKELIJKS:
				String ret =
					startStr + " elke " + (getCyclus() == 1 ? "week" : getCyclus() + " weken")
						+ " op ";
				@SuppressWarnings("hiding")
				List<AfspraakHerhalingDag> dagen =
					new ArrayList<AfspraakHerhalingDag>(getDagenSet());
				dagen.removeAll(EnumSet.of(AfspraakHerhalingDag.WERKDAG, AfspraakHerhalingDag.DAG));
				for (int count = 0; count < dagen.size(); count++)
				{
					if (count > 0)
					{
						if (count == dagen.size() - 1)
							ret += " en ";
						else
							ret += ", ";
					}
					ret += dagen.get(count);
				}
				return ret + eindStr + ".";
			case DAGELIJKS:
				return startStr
					+ " elke "
					+ (getCyclus() == 1 ? getEnkeleDagZonderWeekdagen() + eindStr + "."
						: getCyclus() + " " + getEnkeleDagZonderWeekdagen() + "en" + eindStr + ".");
		}
		return "Ongeldige herhaling";
	}
}
