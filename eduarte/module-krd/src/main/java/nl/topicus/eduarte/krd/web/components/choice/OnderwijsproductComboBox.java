/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.choice;

import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class OnderwijsproductComboBox extends AbstractAjaxDropDownChoice<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductComboBox(String id, IModel<Onderwijsproduct> model,
			IModel< ? extends List<Onderwijsproduct>> ondPrAfnContModel)
	{
		super(id, model, ondPrAfnContModel, new EntiteitToStringRenderer());
	}

	public OnderwijsproductComboBox(String id, IModel<Onderwijsproduct> model,
			IModel< ? extends List<Onderwijsproduct>> ondPrAfnContModel, String toonProperty)
	{
		super(id, model, ondPrAfnContModel, new EntiteitPropertyRenderer(toonProperty));
	}
}
