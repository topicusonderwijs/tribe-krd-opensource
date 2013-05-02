package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * 
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "Tekenbevoegdheid", indexes = {@Index(name = "idx_Tekenbevoegdheid", columnNames = {"organisatie"})})
@Exportable
public class Tekenbevoegdheid extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "organisatieEenheid")
	@Index(name = "idx_Tekenb_orgEenheid")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_Tekenb_medewerker")
	@AutoForm(htmlClasses = "unit_max")
	private Medewerker medewerker;

	@Column(nullable = false)
	private boolean onderwijsovereenkomst;

	@Column(nullable = false)
	private boolean bpvovereenkomst;

	public Tekenbevoegdheid()
	{
	}

	/**
	 * @return Returns the organisatieEenheid.
	 */
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	/**
	 * @param organisatieEenheid
	 *            The organisatieEenheid to set.
	 */
	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	/**
	 * @return Returns the medewerker.
	 */
	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	/**
	 * @param medewerker
	 *            The medewerker to set.
	 */
	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	/**
	 * @return Returns the onderwijsovereenkomst.
	 */
	public boolean isOnderwijsovereenkomst()
	{
		return onderwijsovereenkomst;
	}

	/**
	 * @param onderwijsovereenkomst
	 *            The onderwijsovereenkomst to set.
	 */
	public void setOnderwijsovereenkomst(boolean onderwijsovereenkomst)
	{
		this.onderwijsovereenkomst = onderwijsovereenkomst;
	}

	/**
	 * @return Returns the bpvovereenkomst.
	 */
	public boolean isBpvovereenkomst()
	{
		return bpvovereenkomst;
	}

	/**
	 * @param bpvovereenkomst
	 *            The bpvovereenkomst to set.
	 */
	public void setBpvovereenkomst(boolean bpvovereenkomst)
	{
		this.bpvovereenkomst = bpvovereenkomst;
	}

}
