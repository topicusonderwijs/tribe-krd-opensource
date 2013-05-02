package nl.topicus.eduarte.web.components.resultaat;

import java.io.Serializable;

public class ResultaatColumnCreator<T, M extends ResultatenModel> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected EditorJavascriptRenderer<T> javascriptRenderer;

	public ResultaatColumnCreator(EditorJavascriptRenderer<T> javascriptRenderer)
	{
		this.javascriptRenderer = javascriptRenderer;
	}

	protected ResultaatColumn<T> createResultaatColumn(String id, String header,
			int herkansingsNummer, M resultatenModel,
			DeelnemerToetsResolver<T> deelnemerToetsResolver)
	{
		return new ResultaatColumn<T>(id, header, herkansingsNummer, resultatenModel,
			deelnemerToetsResolver, javascriptRenderer);
	}
}
