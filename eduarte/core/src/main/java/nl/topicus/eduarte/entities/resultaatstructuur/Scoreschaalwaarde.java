package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.web.components.choice.SchaalwaardeCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een waarde in de scoretable bij een toets.
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Scoreschaalwaarde extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "toets")
	@Index(name = "idx_Scoreschaalwaarde_toets")
	@AutoForm(include = false)
	private Toets toets;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "waarde")
	@Index(name = "idx_Scoreschaalwaarde_waarde")
	@AutoForm(editorClass = SchaalwaardeCombobox.class)
	private Schaalwaarde waarde;

	@Column(nullable = false)
	private int vanafScore;

	@Column(nullable = false)
	private int totScore;

	@Column(nullable = true, length = 1000)
	private String advies;

	public Scoreschaalwaarde()
	{
	}

	public Scoreschaalwaarde(Toets toets)
	{
		setToets(toets);
	}

	public Toets getToets()
	{
		return toets;
	}

	public void setToets(Toets toets)
	{
		this.toets = toets;
	}

	public Schaalwaarde getWaarde()
	{
		return waarde;
	}

	public void setWaarde(Schaalwaarde waarde)
	{
		this.waarde = waarde;
	}

	public int getVanafScore()
	{
		return vanafScore;
	}

	public void setVanafScore(int vanafScore)
	{
		this.vanafScore = vanafScore;
	}

	public int getTotScore()
	{
		return totScore;
	}

	public void setTotScore(int totScore)
	{
		this.totScore = totScore;
	}

	public String getAdvies()
	{
		return advies;
	}

	public void setAdvies(String advies)
	{
		this.advies = advies;
	}

	public boolean isScoreBinnenWaarde(int value)
	{
		return getVanafScore() <= value && getTotScore() > value;
	}
}
