package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.web.components.quicksearch.brin.BrinSearchEditor;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "Vervolgonderwijs")
public class Vervolgonderwijs extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum SoortVervolgonderwijs
	{
		Intern,
		BRIN,
		Overig,
		Onbekend;
	}

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SoortVervolgonderwijs soortVervolgonderwijs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code", nullable = true)
	@Index(name = "idx_Vervolgond_brincode")
	@AutoForm(editorClass = BrinSearchEditor.class, label = "BRIN vervolgschool")
	private Brin brincode;

	@Column(length = 100, nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "Naam vervolgschool")
	private String naam;

	@Column(length = 100, nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "Plaats vervolgschool")
	private String plaats;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Vervolgonderwijs()
	{
		this.soortVervolgonderwijs = SoortVervolgonderwijs.BRIN;
	}

	public void setSoortVervolgonderwijs(SoortVervolgonderwijs soortVervolgonderwijs)
	{
		this.soortVervolgonderwijs = soortVervolgonderwijs;
	}

	public SoortVervolgonderwijs getSoortVervolgonderwijs()
	{
		return soortVervolgonderwijs;
	}

	public Brin getBrincode()
	{
		return brincode;
	}

	public void setBrincode(Brin brincode)
	{
		this.brincode = brincode;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

}
