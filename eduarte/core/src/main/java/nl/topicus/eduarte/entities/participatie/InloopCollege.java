package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class InloopCollege extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_80", label = "Maximum aantal inschrijvingen")
	private Integer maxDeelnemers;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	@AutoForm(editorClass = DatumField.class, htmlClasses = "unit_80", label = "Inschrijving openen")
	private Date inschrijfBeginDatum;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	@AutoForm(editorClass = DatumField.class, htmlClasses = "unit_80", label = "Inschrijving sluiten")
	private Date inschrijfEindDatum;

	@Column(nullable = false)
	@AutoForm(label = "Inschrijving geldt voor alle afspraken")
	private boolean heleHerhaling;

	@Column(nullable = true)
	@Lob
	@AutoForm(htmlClasses = "unit_max")
	private String opmerking;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inloopCollege")
	@AutoForm(include = false)
	private List<Afspraak> afspraken = new ArrayList<Afspraak>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inloopCollege")
	@AutoForm(include = false)
	private List<InloopCollegeOpleiding> opleidingen = new ArrayList<InloopCollegeOpleiding>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inloopCollege")
	@AutoForm(include = false)
	private List<InloopCollegeGroep> groepen = new ArrayList<InloopCollegeGroep>();

	public InloopCollege()
	{
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public Integer getMaxDeelnemers()
	{
		return maxDeelnemers;
	}

	public void setMaxDeelnemers(Integer maxDeelnemers)
	{
		this.maxDeelnemers = maxDeelnemers;
	}

	public Date getInschrijfBeginDatum()
	{
		return inschrijfBeginDatum;
	}

	public void setInschrijfBeginDatum(Date inschrijfBeginDatum)
	{
		this.inschrijfBeginDatum = inschrijfBeginDatum;
	}

	public Date getInschrijfEindDatum()
	{
		return inschrijfEindDatum;
	}

	public void setInschrijfEindDatum(Date inschrijfEindDatum)
	{
		this.inschrijfEindDatum = inschrijfEindDatum;
	}

	public boolean isHeleHerhaling()
	{
		return heleHerhaling;
	}

	public void setHeleHerhaling(boolean heleHerhaling)
	{
		this.heleHerhaling = heleHerhaling;
	}

	public String getOpmerking()
	{
		return opmerking;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	public List<Afspraak> getAfspraken()
	{
		return afspraken;
	}

	public Afspraak getEersteAfspraak()
	{
		if (getAfspraken().isEmpty())
			return null;
		List<Afspraak> geordendeAfspraken = getAfspraken();
		Collections.sort(geordendeAfspraken, new Comparator<Afspraak>()
		{

			@Override
			public int compare(Afspraak a1, Afspraak a2)
			{
				if (a1.getBeginDatumTijd() == null || a2.getBeginDatumTijd() == null)
				{
					return 0;
				}
				return a1.getBeginDatumTijd().compareTo(a2.getBeginDatumTijd());
			}

		});
		return geordendeAfspraken.get(0);
	}

	public void setAfspraken(List<Afspraak> afspraken)
	{
		this.afspraken = afspraken;
	}

	public List<InloopCollegeOpleiding> getOpleidingen()
	{
		return opleidingen;
	}

	public void setOpleidingen(List<InloopCollegeOpleiding> opleidingen)
	{
		this.opleidingen = opleidingen;
	}

	public List<InloopCollegeGroep> getGroepen()
	{
		return groepen;
	}

	public void setGroepen(List<InloopCollegeGroep> groepen)
	{
		this.groepen = groepen;
	}

	@Override
	public String toString()
	{
		String ret = "Inloop college";
		if (!StringUtil.isEmpty(getOmschrijving()))
			ret += " (" + getOmschrijving() + ")";
		return ret;
	}

	public InloopCollege copy()
	{
		InloopCollege copy = new InloopCollege();
		copy.setInschrijfBeginDatum(getInschrijfBeginDatum());
		copy.setInschrijfEindDatum(getInschrijfEindDatum());
		copy.setMaxDeelnemers(getMaxDeelnemers());
		copy.setOmschrijving(getOmschrijving());
		copy.setOpmerking(getOpmerking());
		copy.setHeleHerhaling(isHeleHerhaling());
		for (InloopCollegeGroep curGroep : getGroepen())
		{
			InloopCollegeGroep groepCopy = new InloopCollegeGroep();
			groepCopy.setInloopCollege(copy);
			groepCopy.setGroep(curGroep.getGroep());
			copy.getGroepen().add(groepCopy);
		}
		for (InloopCollegeOpleiding curOpleiding : getOpleidingen())
		{
			InloopCollegeOpleiding opleidingCopy = new InloopCollegeOpleiding();
			opleidingCopy.setInloopCollege(copy);
			opleidingCopy.setOpleiding(curOpleiding.getOpleiding());
			copy.getOpleidingen().add(opleidingCopy);
		}
		return copy;
	}
}
