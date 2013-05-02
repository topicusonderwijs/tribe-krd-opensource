package nl.topicus.eduarte.entities.taxonomie;

import javax.persistence.Entity;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@Exportable
public class Deelgebied extends TaxonomieElement
{
	private static final long serialVersionUID = 1L;

	public Deelgebied()
	{
	}

	public Deelgebied(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * 
	 * @return De taxonomiecode van dit deelgebied zonder de code van het
	 *         verbintenisgebied waarbij het deelgebied hoort (dus zonder alles voor de
	 *         underscore).
	 */
	public String getTaxonomiecodeZonderVerbintenisgebied()
	{
		String res = getTaxonomiecode();
		int index = res.indexOf("_");
		if (index > -1)
		{
			res = res.substring(index + 1);
		}
		return res;
	}

	/**
	 * @return Het verbintenisgebied-gedeelte van de taxonomiecode van dit deelgebied.
	 */
	public String getTaxonomiecodeZonderDeelgebied()
	{
		String res = getTaxonomiecode();
		int index = res.indexOf("_");
		if (index > -1)
		{
			res = res.substring(0, index);
		}
		return res;
	}

	/**
	 * Het gedeelte van de taxonomiecode van het verbintenisgebied wordt overgeslagen.
	 * 
	 * @see nl.topicus.eduarte.entities.taxonomie.TaxonomieElement#getTaxonomiecodeNaam()
	 */
	@Override
	@Exportable
	public String getTaxonomiecodeNaam()
	{
		return getTaxonomiecodeZonderVerbintenisgebied() + " " + getNaam();
	}

}
