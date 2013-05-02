package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoRecord;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class BronFotoRecordZoekFilter extends AbstractZoekFilter<BronFotoRecord>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends BronFotoRecord> clazz;

	private IModel<BronFotobestand> fotobestand;

	public BronFotoRecordZoekFilter(Class< ? extends BronFotoRecord> clazz,
			BronFotobestand fotobestand)
	{
		this.clazz = clazz;
		setFotobestand(fotobestand);
	}

	public Class< ? extends BronFotoRecord> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class< ? extends BronFotoRecord> clazz)
	{
		this.clazz = clazz;
	}

	public BronFotobestand getFotobestand()
	{
		return getModelObject(fotobestand);
	}

	public void setFotobestand(BronFotobestand fotobestand)
	{
		this.fotobestand = makeModelFor(fotobestand);
	}
}
