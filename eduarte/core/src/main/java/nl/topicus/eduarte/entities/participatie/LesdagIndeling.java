/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite, henzen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class LesdagIndeling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(name = "dag", length = 2, nullable = false)
	@Index(name = "idx_Lesdag_dag")
	@AutoForm(label = "Dag")
	private String dag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesweekIndeling", nullable = false)
	@Index(name = "idx_Lesdag_lesweek")
	@IgnoreInGebruik
	private LesweekIndeling lesweekIndeling;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lesdagIndeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(label = "lesuur Indeling")
	@OrderBy("lesuur")
	private List<LesuurIndeling> lesuurIndeling;

	public LesdagIndeling()
	{
	}

	public String getDag()
	{
		return dag;
	}

	public void setDag(String dag)
	{
		this.dag = dag;
	}

	public LesweekIndeling getLesweekIndeling()
	{
		return lesweekIndeling;
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = lesweekIndeling;
	}

	/**
	 * 
	 * Controleerd aan de hand van de dag of deze indeling voor deze datum zou kunnen
	 * gelden.
	 * 
	 * @param datum
	 * @return true als beide dagen gelijk zijn, anders false.
	 */
	public boolean valtOpDatum(Date datum)
	{
		return TimeUtil.getInstance().getDayOfWeek(datum) == getDagNummerVanafZondag();
	}

	/**
	 * @return Het dagnummer van deze indeling, ZO=1,MA=2,DI=3 etc.
	 */
	public int getDagNummerVanafZondag()
	{
		String dagcode = getDag();
		if (dagcode.equals("ZO"))
			return 1;
		if (dagcode.equals("MA"))
			return 2;
		if (dagcode.equals("DI"))
			return 3;
		if (dagcode.equals("WO"))
			return 4;
		if (dagcode.equals("DO"))
			return 5;
		if (dagcode.equals("VR"))
			return 6;
		if (dagcode.equals("ZA"))
			return 7;
		return -1;

	}

	/**
	 * @return Het dagnummer van deze indeling, MA=1,DI=2 etc.
	 */
	public int getDagNummer()
	{
		String dagcode = getDag();
		if (dagcode.equals("MA"))
			return 1;
		if (dagcode.equals("DI"))
			return 2;
		if (dagcode.equals("WO"))
			return 3;
		if (dagcode.equals("DO"))
			return 4;
		if (dagcode.equals("VR"))
			return 5;
		if (dagcode.equals("ZA"))
			return 6;
		if (dagcode.equals("ZO"))
			return 7;
		return -1;
	}

	public void setLesuurIndeling(List<LesuurIndeling> lesuurIndeling)
	{
		this.lesuurIndeling = lesuurIndeling;
	}

	public List<LesuurIndeling> getLesuurIndeling()
	{
		if (lesuurIndeling == null)
			lesuurIndeling = new ArrayList<LesuurIndeling>();

		return lesuurIndeling;
	}

	public int getAantalLesuren()
	{
		return getLesuurIndeling().size();
	}

	public String getBegintijdFormatted()
	{
		if (getLesuurIndeling().isEmpty())
			return null;
		return TimeUtil.getInstance().formatHourMinute(getLesuurIndeling().get(0).getBeginTijd(),
			":");
	}

	public String getEindtijdFormatted()
	{
		if (getLesuurIndeling().isEmpty())
			return null;
		return TimeUtil.getInstance().formatHourMinute(
			getLesuurIndeling().get(getLesuurIndeling().size() - 1).getEindTijd(), ":");
	}

	public String getNaam()
	{
		String dagcode = getDag();
		if (dagcode.equals("MA"))
			return "Maandag";
		if (dagcode.equals("DI"))
			return "Dinsdag";
		if (dagcode.equals("WO"))
			return "Woensdag";
		if (dagcode.equals("DO"))
			return "Donderdag";
		if (dagcode.equals("VR"))
			return "Vrijdag";
		if (dagcode.equals("ZA"))
			return "Zaterdag";
		if (dagcode.equals("ZO"))
			return "Zondag";
		return "";
	}
}
