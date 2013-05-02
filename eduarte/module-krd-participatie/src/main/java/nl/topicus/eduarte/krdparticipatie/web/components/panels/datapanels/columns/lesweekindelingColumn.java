package nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns;

import java.util.List;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek.LesweekKoppelingWrapper;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class lesweekindelingColumn extends AbstractCustomColumn<LesweekKoppelingWrapper>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<LesweekIndeling>> choisesModel;

	public lesweekindelingColumn(String id, String header,
			IModel<List<LesweekIndeling>> choisesModel)
	{
		super(id, header);
		this.choisesModel = choisesModel;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<LesweekKoppelingWrapper> rowModel, int span)
	{
		LesweekKoppelingWrapper wrapper = rowModel.getObject();
		cell.add(new LesweekindelingDropDownPanel(componentId, new PropertyModel<LesweekIndeling>(
			wrapper, "lesweekindeling"), choisesModel));
	}

	@Override
	public void detach()
	{
		super.detach();
		choisesModel.detach();
	}
}
