package nl.topicus.eduarte.entities.productregel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Productregels kunnen gegroepeerd worden per soort, bijvoorbeeld Gemeenschappelijk,
 * Profiel, Keuze.
 * 
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"naam", "taxonomie", "organisatie"}),
	@UniqueConstraint(columnNames = {"volgnummer", "taxonomie", "organisatie"})})
public class SoortProductregel extends LandelijkOfInstellingEntiteit implements
		Comparable<SoortProductregel>, IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 30)
	@AutoForm(htmlClasses = {"unit_max"})
	private String naam;

	/**
	 * Volgorde van productregels (Gemeenschappelijk komt voor Profiel).
	 */
	@Column(nullable = false)
	@AutoForm(htmlClasses = {"unit_40"})
	private int volgnummer;

	@Column(nullable = false)
	private boolean actief;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "taxonomie")
	@AutoForm(editorClass = TaxonomieCombobox.class)
	@Index(name = "idx_SoortProduct_taxonomie")
	private Taxonomie taxonomie;

	@Column(nullable = false, length = 30)
	@AutoForm(htmlClasses = {"unit_max"})
	private String diplomanaam;

	protected SoortProductregel()
	{
	}

	public SoortProductregel(EntiteitContext context)
	{
		super(context);
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getDiplomanaam()
	{
		return diplomanaam;
	}

	public void setDiplomanaam(String diplomanaam)
	{
		this.diplomanaam = diplomanaam;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	@Override
	public int compareTo(SoortProductregel o)
	{
		return getVolgnummer() - o.getVolgnummer();
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

}
