package nl.topicus.eduarte.resultaten.web.components.resultaat;

import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory.ExtraInputFieldsBean;
import nl.topicus.eduarte.web.components.resultaat.DeelnemerToetsResolver;
import nl.topicus.eduarte.web.components.resultaat.EditorJavascriptRenderer;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumn;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumnCreator;

public class ResultaatEditColumnCreator<T> extends ResultaatColumnCreator<T, ResultatenEditModel>
{
	private static final long serialVersionUID = 1L;

	private ExtraInputFieldsBean extraInputFields;

	public ResultaatEditColumnCreator(EditorJavascriptRenderer<T> javascriptRenderer,
			ExtraInputFieldsBean extraInputFields)
	{
		super(javascriptRenderer);
		this.extraInputFields = extraInputFields;
	}

	@Override
	protected ResultaatColumn<T> createResultaatColumn(String id, String header,
			int herkansingsNummer, ResultatenEditModel resultatenModel,
			DeelnemerToetsResolver<T> deelnemerToetsResolver)
	{
		return new ResultaatEditColumn<T>(id, header, herkansingsNummer, resultatenModel,
			deelnemerToetsResolver, javascriptRenderer, extraInputFields);
	}
}
