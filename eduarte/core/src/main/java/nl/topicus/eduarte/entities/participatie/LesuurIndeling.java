/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een lesdagindeling verwijst naar een lesuurindeling. Een lesuurindeling bevat een lijst
 * met lesuren. Op deze manier wordt het mogelijk om de lesuren per weekdag te varieren.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class LesuurIndeling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	// @Column(length = 10, nullable = false)
	// @Index(name = "idx_Lesuur_code")
	// @AutoForm(label = "Code")
	// private String code;

	@Column(name = "lesuur", length = 2, nullable = false)
	@Index(name = "idx_Lesuur_Lesuur")
	@AutoForm(label = "Lesuur")
	private int lesuur;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesdagIndeling", nullable = false)
	@Index(name = "idx_LesdagInd_lesweekInd")
	private LesdagIndeling lesdagIndeling;

	@Column(name = "beginTijd", nullable = false)
	@Index(name = "idx_Lesuur_begin")
	@AutoForm(label = "Begin tijd")
	@Temporal(value = TemporalType.TIME)
	private Date beginTijd;

	@Column(name = "eindTijd", nullable = false)
	@Index(name = "idx_Lesuur_eind")
	@AutoForm(label = "Eind tijd")
	@Temporal(value = TemporalType.TIME)
	private Date eindTijd;

	/**
	 * Default constructor voor Hibernate.
	 */
	public LesuurIndeling()
	{
	}

	// /**
	// * @return Returns the code.
	// */
	// public String getCode()
	// {
	// return code;
	// }
	//
	// /**
	// * @param code
	// * The code to set.
	// */
	// public void setCode(String code)
	// {
	// this.code = code;
	// }

	public void setLesuur(int lesuur)
	{
		this.lesuur = lesuur;
	}

	public int getLesuur()
	{
		return lesuur;
	}

	public void setBeginTijd(Date beginTijd)
	{
		this.beginTijd = beginTijd;
	}

	public Date getBeginTijd()
	{
		return beginTijd;
	}

	public void setEindTijd(Date eindTijd)
	{
		this.eindTijd = eindTijd;
	}

	public Date getEindTijd()
	{
		return eindTijd;
	}

	public void setLesdagIndeling(LesdagIndeling lesdagIndeling)
	{
		this.lesdagIndeling = lesdagIndeling;
	}

	public LesdagIndeling getLesdagIndeling()
	{
		return lesdagIndeling;
	}

	public String getNaam()
	{
		return lesdagIndeling.getNaam();
	}

}
