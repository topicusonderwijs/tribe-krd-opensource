package nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ModelObjectKey;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek.LesweekKoppelingWrapper;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LesweekindelingTreeColumn extends CustomPropertyColumn<LesweekKoppelingWrapper>
		implements IStyledColumn<LesweekKoppelingWrapper>
{
	private static final long serialVersionUID = 1L;

	private Map<ModelObjectKey, WebMarkupContainer> rows =
		new HashMap<ModelObjectKey, WebMarkupContainer>();

	private CollapsableRowFactoryDecorator<LesweekKoppelingWrapper> rowFactory;

	private IModel<OrganisatieEenheid> lastOrgEhdModel;

	public LesweekindelingTreeColumn(
			CollapsableRowFactoryDecorator<LesweekKoppelingWrapper> rowFactory)
	{
		super("organisatieLocatie", "Organisatie/Locatie", "");
		this.rowFactory = rowFactory;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<LesweekKoppelingWrapper> rowModel, int span)
	{
		row.setOutputMarkupId(true);
		rows.put(new ModelObjectKey(rowModel), row);

		LesweekKoppelingWrapper wrapper = rowModel.getObject();

		if (wrapper.getLocatieModel() == null)
		{
			OrganisatieEenheid org = wrapper.getOrgEenheidModel().getObject();
			lastOrgEhdModel = ModelFactory.getModel(org);
			if (org.getParent() != null)
			{
				WebMarkupContainer parentRow =
					rows.get(new ModelObjectKey(new Model<LesweekKoppelingWrapper>(
						new LesweekKoppelingWrapper(ModelFactory.getModel(org.getParent())))));
				if (parentRow != null)
				{
					rowFactory.makeChild(parentRow, row);
				}
			}
			if (!org.getActieveDirectChildren(TimeUtil.getInstance().currentDate()).isEmpty())
			{
				rowFactory.makeParent(row, rowModel);
			}
		}
		else if (wrapper.getLocatieModel() != null && wrapper.getLocatieModel().getObject() != null)
		{
			WebMarkupContainer parentRow =
				rows.get(new ModelObjectKey(new Model<LesweekKoppelingWrapper>(
					new LesweekKoppelingWrapper(lastOrgEhdModel))));
			if (parentRow != null)
			{
				rowFactory.makeChild(parentRow, row);
			}
		}
		super.populateItem(cell, componentId, row, rowModel, span);
	}

	@Override
	public String getCssClass()
	{
		return "";
	}

	@Override
	public void detach()
	{
		super.detach();
		rowFactory.detach();
		lastOrgEhdModel = null;
		for (ModelObjectKey curKey : rows.keySet())
			curKey.detach();
	}
}