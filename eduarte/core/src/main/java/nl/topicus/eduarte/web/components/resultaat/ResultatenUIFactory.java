package nl.topicus.eduarte.web.components.resultaat;

import java.io.Serializable;
import java.util.List;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public interface ResultatenUIFactory<T, M extends ResultatenModel> extends Serializable
{
	public ResultaatColumnCreator<T, M> createColumnCreator(
			EditorJavascriptRenderer<T> javascriptRenderer);

	public M createResultatenModel(ResultaatZoekFilter resultaatFilter,
			IModel< ? extends List<Toets>> toetsenModel,
			IModel< ? extends List<Deelnemer>> deelnemersModel);

	public Component createInputfields(String id);
}
