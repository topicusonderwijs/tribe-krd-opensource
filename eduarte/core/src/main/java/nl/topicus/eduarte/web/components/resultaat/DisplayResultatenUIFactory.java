package nl.topicus.eduarte.web.components.resultaat;

import java.util.List;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class DisplayResultatenUIFactory<T> implements ResultatenUIFactory<T, ResultatenModel>
{
	private static final long serialVersionUID = 1L;

	@Override
	public ResultaatColumnCreator<T, ResultatenModel> createColumnCreator(
			EditorJavascriptRenderer<T> javascriptRenderer)
	{
		return new ResultaatColumnCreator<T, ResultatenModel>(javascriptRenderer);
	}

	@Override
	public ResultatenModel createResultatenModel(ResultaatZoekFilter resultaatFilter,
			IModel< ? extends List<Toets>> toetsenModel,
			IModel< ? extends List<Deelnemer>> deelnemersModel)
	{
		return new ResultatenModel(resultaatFilter, toetsenModel, deelnemersModel);
	}

	@Override
	public Component createInputfields(String id)
	{
		return new WebMarkupContainer(id).setVisible(false);
	}
}
