/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link IBronTerugkoppeling} .
 * 
 * @author vandekamp
 */
public class BronTerugkoppelingZoekFilter extends AbstractBronZoekFilter<IBronTerugkoppeling>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_160")
	private BronOnderwijssoort bronOnderwijssoort;

	public BronTerugkoppelingZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatusModel)
	{
		super(bronSchooljaarStatusModel);
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}
}
