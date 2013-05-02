package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.ModelObjectKey;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class IntakegesprekTreeColumn<T extends VerbintenisProvider> extends AbstractCustomColumn<T>
		implements IStyledColumn<T>
{
	private static final long serialVersionUID = 1L;

	private Map<ModelObjectKey, WebMarkupContainer> gesprekRows =
		new HashMap<ModelObjectKey, WebMarkupContainer>();

	private CollapsableRowFactoryDecorator<T> rowFactory;

	public IntakegesprekTreeColumn(CollapsableRowFactoryDecorator<T> rowFactory)
	{
		super("Intake", "Intake");
		this.rowFactory = rowFactory;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		StringBuilder label = new StringBuilder();

		row.setOutputMarkupId(true);
		gesprekRows.put(new ModelObjectKey(rowModel), row);

		VerbintenisProvider obj = rowModel.getObject();

		if (obj instanceof Intakegesprek)
		{
			WebMarkupContainer parentRow =
				gesprekRows.get(new ModelObjectKey(new Model<Verbintenis>(obj.getVerbintenis())));
			if (parentRow != null)
			{
				rowFactory.makeChild(parentRow, row);
			}
		}

		if (obj instanceof Verbintenis)
		{
			Verbintenis verbintenis = (Verbintenis) obj;

			if (!(verbintenis.getIntakegesprekken().isEmpty()))
			{
				rowFactory.makeParent(row, rowModel);
			}

			label.append(verbintenis.getOrganisatieEenheid().getNaam());
			if (verbintenis.getOpleiding() != null)
			{
				label.append(" (").append(verbintenis.getOpleiding()).append(")");
			}

			cell.add(new SimpleAttributeModifier("colspan", "7"));
		}

		cell.add(new Label(componentId, label.toString()));
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
		for (ModelObjectKey curKey : gesprekRows.keySet())
			curKey.detach();
	}

}
