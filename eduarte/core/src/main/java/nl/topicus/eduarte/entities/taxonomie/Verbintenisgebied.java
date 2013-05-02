package nl.topicus.eduarte.entities.taxonomie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@Exportable
public class Verbintenisgebied extends TaxonomieElement
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private List<VerbintenisgebiedOnderdeel> onderdelen;

	/**
	 * De landelijke productregels die aan dit verbintenisgebied zijn gekoppeld. Let op:
	 * Deze lijst bevat dus niet de productregels die aan dit verbintenisgebied via een
	 * opleiding zijn gekoppeld!
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenisgebied")
	@Where(clause = "opleiding is null")
	private List<Productregel> productregels;

	/**
	 * De landelijke criteria die aan dit verbintenisgebied zijn gekoppeld.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenisgebied")
	@Where(clause = "opleiding is null")
	private List<Criterium> criteria;

	public Verbintenisgebied()
	{
	}

	public Verbintenisgebied(EntiteitContext context)
	{
		super(context);
	}

	public List<VerbintenisgebiedOnderdeel> getOnderdelen()
	{
		if (onderdelen == null)
			onderdelen = new ArrayList<VerbintenisgebiedOnderdeel>();
		return onderdelen;
	}

	public void setOnderdelen(List<VerbintenisgebiedOnderdeel> onderdelen)
	{
		this.onderdelen = onderdelen;
	}

	/**
	 * @return Returns the productregels. Let op: Deze lijst bevat alleen landelijke
	 *         productregels. Je mag dus ook geen niet-landelijke productregels aan deze
	 *         lijst toevoegen!
	 */
	public List<Productregel> getProductregels()
	{
		if (productregels == null)
			productregels = new ArrayList<Productregel>();
		return productregels;
	}

	public void setProductregels(List<Productregel> productregels)
	{
		this.productregels = productregels;
	}

	/**
	 * 
	 * @param soort
	 * @param cohort
	 * @return Een lijst van alle productregels van de gegeven soort bij dit
	 *         verbintenisgebied.
	 */
	public List<Productregel> getProductregels(SoortProductregel soort, Cohort cohort)
	{
		List<Productregel> res = new ArrayList<Productregel>(getProductregels().size());
		for (Productregel regel : getProductregels())
		{
			if (regel.getSoortProductregel().equals(soort) && regel.getCohort().equals(cohort))
			{
				res.add(regel);
			}
		}
		return res;
	}

	/**
	 * 
	 * @param naam
	 * @return Een lijst van alle productregels waarvan de naam overeenkomt met parameter
	 *         'naam'
	 */
	public List<Productregel> getProductregels(String naam)
	{
		List<Productregel> res = new ArrayList<Productregel>();
		for (Productregel regel : getProductregels())
		{
			if (regel.getNaam().contains(naam))
			{
				res.add(regel);
			}
		}
		return res;
	}

	public List<Criterium> getCriteria()
	{
		if (criteria == null)
			criteria = new ArrayList<Criterium>();
		return criteria;
	}

	public void setCriteria(List<Criterium> criteria)
	{
		this.criteria = criteria;
	}

	/**
	 * 
	 * @return Het soort onderwijs (als vooropleiding) van dit verbintenisgebied.
	 */
	public SoortOnderwijs getSoortOnderwijs()
	{
		Taxonomie taxonomie = getTaxonomie();
		if (taxonomie.isMBO() || taxonomie.isCGO())
		{
			if (AbstractMBOVerbintenisgebied.class.isAssignableFrom(getClass()))
			{
				AbstractMBOVerbintenisgebied mbo = (AbstractMBOVerbintenisgebied) this;
				if (mbo.getNiveau() == MBONiveau.Niveau1 || mbo.getNiveau() == MBONiveau.Niveau2)
				{
					return SoortOnderwijs.MBO12;
				}
				else if (mbo.getNiveau() == MBONiveau.Niveau3
					|| mbo.getNiveau() == MBONiveau.Niveau4)
				{
					return SoortOnderwijs.MBO34;
				}
			}
		}
		if (taxonomie.isVO())
		{
			String code = getTaxonomiecode();
			if (code.startsWith("3.1"))
			{
				return SoortOnderwijs.Basisvorming;
			}
			if (code.startsWith("3.2.1"))
			{
				return SoortOnderwijs.VWO;
			}
			if (code.startsWith("3.2.2"))
			{
				return SoortOnderwijs.HAVO;
			}
			if (code.startsWith("3.2.3.1"))
			{
				return SoortOnderwijs.VMBOTL;
			}
			if (code.startsWith("3.2.3.2") || code.startsWith("3.2.4") || code.startsWith("3.2.5"))
			{
				return SoortOnderwijs.VMBO;
			}
		}
		return null;
	}

}
