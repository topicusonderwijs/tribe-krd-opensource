package nl.topicus.eduarte.entities.examen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen examenworkflow en taxonomie. Hiermee wordt aangegeven welke workflow
 * geldt voor welke taxonomie.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(name = "ExamenWorkflowTax")
public class ExamenWorkflowTaxonomie extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "examenWorkflow")
	@Index(name = "idx_exWorkflowTax_wf")
	private ExamenWorkflow examenWorkflow;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "taxonomie")
	@Index(name = "idx_exWorkflowTax_tax")
	private Taxonomie taxonomie;

	/**
	 * Default constructor voor Hibernate.
	 */
	protected ExamenWorkflowTaxonomie()
	{
	}

	public ExamenWorkflowTaxonomie(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param examenWorkflow
	 * @param taxonomie
	 */
	public ExamenWorkflowTaxonomie(ExamenWorkflow examenWorkflow, Taxonomie taxonomie)
	{
		super(examenWorkflow.isLandelijk());
		setExamenWorkflow(examenWorkflow);
		setTaxonomie(taxonomie);
	}

	/**
	 * @return Returns the examenWorkflow.
	 */
	public ExamenWorkflow getExamenWorkflow()
	{
		return examenWorkflow;
	}

	/**
	 * @param examenWorkflow
	 *            The examenWorkflow to set.
	 */
	public void setExamenWorkflow(ExamenWorkflow examenWorkflow)
	{
		this.examenWorkflow = examenWorkflow;
	}

	/**
	 * @return Returns the taxonomie.
	 */
	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}

	/**
	 * @param taxonomie
	 *            The taxonomie to set.
	 */
	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

}
