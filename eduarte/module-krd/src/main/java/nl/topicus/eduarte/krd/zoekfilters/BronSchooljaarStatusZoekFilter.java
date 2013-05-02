/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link BronSchooljaarStatus} .
 * 
 * @author vandekamp
 */
public class BronSchooljaarStatusZoekFilter extends AbstractZoekFilter<BronSchooljaarStatus>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Schooljaar>> schooljaren;

	private IModel<BronAanleverpunt> aanleverpunt;

	private IModel<BronSchooljaarStatus> bronSchooljaarStatus;

	public BronSchooljaarStatusZoekFilter()
	{
	}

	public void setSchooljaren(List<Schooljaar> schooljaren)
	{
		this.schooljaren = makeModelFor(schooljaren);
	}

	public List<Schooljaar> getSchooljaren()
	{
		return getModelObject(schooljaren);
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = makeModelFor(aanleverpunt);
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return getModelObject(aanleverpunt);
	}

	public void setBronSchooljaarStatus(BronSchooljaarStatus bronSchooljaarStatus)
	{
		this.bronSchooljaarStatus = makeModelFor(bronSchooljaarStatus);
	}

	public BronSchooljaarStatus getBronSchooljaarStatus()
	{
		return getModelObject(bronSchooljaarStatus);
	}
}
