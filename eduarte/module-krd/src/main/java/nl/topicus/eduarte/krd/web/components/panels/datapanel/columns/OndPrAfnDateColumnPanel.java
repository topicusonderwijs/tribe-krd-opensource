/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel om te tonen in een customdatapanel, die de begin en einddatum toont. Als nog niet
 * opgeslagen is als textfield, anders als label
 * 
 * @author vandekamp
 */
public class OndPrAfnDateColumnPanel extends TypedPanel<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public OndPrAfnDateColumnPanel(String id, IModel<OnderwijsproductAfnameContext> model)
	{
		super(id, model);
		OnderwijsproductAfnameContext context = model.getObject();
		OnderwijsproductAfname afname = context.getOnderwijsproductAfname();

		WebMarkupContainer fieldContainer = new WebMarkupContainer("fieldContainer");
		fieldContainer.setVisible(!afname.isSaved()
			&& context.getProductregel().getTypeProductregel() == TypeProductregel.Productregel);
		fieldContainer.setRenderBodyOnly(true);
		add(fieldContainer);
		fieldContainer.add(new RequiredDatumField("onderwijsproductAfname.begindatum"));
		fieldContainer.add(new DatumField("onderwijsproductAfname.einddatum"));

		WebMarkupContainer labelContainer = new WebMarkupContainer("labelContainer");
		labelContainer
			.setVisible(afname.isSaved()
				|| context.getProductregel().getTypeProductregel() == TypeProductregel.AfgeleideProductregel);
		labelContainer.setRenderBodyOnly(true);
		add(labelContainer);
		labelContainer.add(new Label("beginEindLabel", new Model<String>(afname
			.getGeldigVanTotBeschrijving())));

		setRenderBodyOnly(true);
	}
}
