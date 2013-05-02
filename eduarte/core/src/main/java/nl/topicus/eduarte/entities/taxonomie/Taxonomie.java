package nl.topicus.eduarte.entities.taxonomie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.ExamenWorkflowTaxonomie;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een specifieke taxonomie
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Taxonomie extends TaxonomieElement
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxonomie")
	private List<TaxonomieElementType> taxonomieElementTypes =
		new ArrayList<TaxonomieElementType>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxonomie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
	private List<ExamenWorkflowTaxonomie> taxonomieExamenWorkflows;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Taxonomie()
	{
	}

	public Taxonomie(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return de TaxonomieEnum die overeenkomt met deze taxonomie.
	 */
	public TaxonomieEnum getTaxonomieEnum()
	{
		for (TaxonomieEnum te : TaxonomieEnum.values())
		{
			if (te.isTaxonomieEnum(this))
			{
				return te;
			}
		}
		return TaxonomieEnum.Anders;
	}

	/**
	 * @return true als dit de CGO-taxonomie is.
	 */
	public boolean isCGO()
	{
		return getTaxonomieEnum() == TaxonomieEnum.CGO;
	}

	/**
	 * @return true als dit de MBO-kwalificatietaxonomie is.
	 */
	public boolean isMBO()
	{
		return getTaxonomieEnum() == TaxonomieEnum.MBO;
	}

	/**
	 * @return true als dit een BO opleiding is (ongeacht of het een kwalificatietaxonomie
	 *         of competentiegerichtonderwijs taxonomie is)
	 */
	public boolean isBO()
	{
		return isCGO() || isMBO();
	}

	public boolean isHO()
	{
		return getTaxonomieEnum() == TaxonomieEnum.HO;
	}

	/**
	 * @return true als dit de VO-taxonomie is.
	 */
	public boolean isVO()
	{
		return getTaxonomieEnum() == TaxonomieEnum.VO;
	}

	/**
	 * @return true als dit de Educatie-taxonomie is.
	 */
	public boolean isEducatie()
	{
		return getTaxonomieEnum() == TaxonomieEnum.Educatie;
	}

	/**
	 * @return true als dit de Inburgering-taxonomie is.
	 */
	public boolean isInburgering()
	{
		return getTaxonomieEnum() == TaxonomieEnum.Inburgering;
	}

	/**
	 * @return Het taxonomie-elementtype dat onderaan in de hierarchie staat van deze
	 *         taxonomie.
	 */
	public TaxonomieElementType getOndersteVerbintenisgebied()
	{
		for (TaxonomieElementType type : getTaxonomieElementTypes())
		{
			if (type.getSoort() == SoortTaxonomieElement.Verbintenisgebied
				&& type.getVerbintenisgebiedChild() == null)
			{
				return type;
			}
		}
		// Indien zover gekomen zijn er geen verbintenisgebieden gedefinieerd, en zou de
		// taxonomie als onderste element moeten functioneren.
		return getTaxonomieElementType();
	}

	/**
	 * @return Het taxonomie-elementtype van het soort Deelgebied dat onderaan staat in
	 *         deze taxonomie, of null indien nog geen deelgebieden zijn gedefinieerd.
	 */
	public TaxonomieElementType getOndersteDeelgebied()
	{
		for (TaxonomieElementType type : getTaxonomieElementTypes())
		{
			if (type.getSoort() == SoortTaxonomieElement.Deelgebied
				&& type.getDeelgebiedChild() == null)
			{
				return type;
			}
		}
		return null;
	}

	/**
	 * @return Returns the taxonomieElementTypes.
	 */
	public List<TaxonomieElementType> getTaxonomieElementTypes()
	{
		return taxonomieElementTypes;
	}

	/**
	 * @param taxonomieElementTypes
	 *            The taxonomieElementTypes to set.
	 */
	public void setTaxonomieElementTypes(List<TaxonomieElementType> taxonomieElementTypes)
	{
		this.taxonomieElementTypes = taxonomieElementTypes;
	}

	/**
	 * @return Het hoogste volgnummer van de taxonomie-elementtypes van deze taxonomie.
	 */
	public int getHoogsteTaxonomieElementTypeVolgnummer()
	{
		int max = 0;
		for (TaxonomieElementType type : getTaxonomieElementTypes())
		{
			max = Math.max(max, type.getVolgnummer());
		}
		return max;
	}

	/**
	 * @param taxonomiecode
	 * @return De taxonomie met de gegeven taxonomiecode.
	 */
	public static Taxonomie getLandelijkeTaxonomie(String taxonomiecode)
	{
		return (Taxonomie) DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class)
			.getLandelijk(taxonomiecode);
	}

	public ExamenWorkflow getExamenWorkflow()
	{
		for (ExamenWorkflowTaxonomie workflow : getTaxonomieExamenWorkflows())
		{
			return workflow.getExamenWorkflow();
		}
		return null;
	}

	public List<ExamenWorkflowTaxonomie> getTaxonomieExamenWorkflows()
	{
		return taxonomieExamenWorkflows;
	}

	public void setTaxonomieExamenWorkflows(List<ExamenWorkflowTaxonomie> taxonomieExamenWorkflows)
	{
		this.taxonomieExamenWorkflows = taxonomieExamenWorkflows;
	}

}
