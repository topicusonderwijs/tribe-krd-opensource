package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.ModelObjectKey;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PlaatsingTreeColumn<T extends IdObject> extends AbstractCustomColumn<T> implements
		IStyledColumn<T>
{
	private static final long serialVersionUID = 1L;

	private Map<ModelObjectKey, WebMarkupContainer> plaatsingRows =
		new HashMap<ModelObjectKey, WebMarkupContainer>();

	private CollapsableRowFactoryDecorator<T> rowFactory;

	// Gewenste colspan voor een Verbintenis row
	private String colspan;

	public PlaatsingTreeColumn(CollapsableRowFactoryDecorator<T> rowFactory, String colspan)
	{
		super("Opleiding", "Opleiding");
		this.rowFactory = rowFactory;
		this.colspan = colspan;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		String label = "";

		row.setOutputMarkupId(true);
		plaatsingRows.put(new ModelObjectKey(rowModel), row);

		Object obj = rowModel.getObject();

		if (obj instanceof Plaatsing)
		{
			WebMarkupContainer parentRow =
				plaatsingRows.get(new ModelObjectKey(new Model<Verbintenis>(((Plaatsing) obj)
					.getVerbintenis())));
			if (parentRow != null)
			{
				rowFactory.makeChild(parentRow, row);
			}
		}

		if (obj instanceof Verbintenis)
		{
			Verbintenis verbintenis = (Verbintenis) obj;

			if (!(verbintenis.getPlaatsingen().isEmpty()))
			{
				rowFactory.makeParent(row, rowModel);
			}

			if (verbintenis.getOpleiding() != null)
			{
				label = verbintenis.getOpleiding().getNaam();
			}
			else
				label = verbintenis.getOrganisatieEenheid().getNaam();

			cell.add(new SimpleAttributeModifier("colspan", colspan));
		}

		cell.add(new Label(componentId, label));
	}

	@Override
	public String getCssClass()
	{
		return "unit_40";
	}

	@Override
	public void detach()
	{
		super.detach();
		for (ModelObjectKey curKey : plaatsingRows.keySet())
			curKey.detach();
	}

}
