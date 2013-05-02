package nl.topicus.eduarte.resultaten.web.components.resultaat;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory.ExtraInputFieldsBean;
import nl.topicus.eduarte.web.components.resultaat.DeelnemerToetsResolver;
import nl.topicus.eduarte.web.components.resultaat.EditorJavascriptRenderer;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class ResultaatEditColumn<T> extends ResultaatColumn<T>
{
	private static final long serialVersionUID = 1L;

	private ExtraInputFieldsBean extraInputFields;

	public ResultaatEditColumn(String id, String header, int pogingNummer,
			ResultatenEditModel resultatenModel, DeelnemerToetsResolver<T> deelnemerToetsResolver,
			EditorJavascriptRenderer<T> javascriptRenderer, ExtraInputFieldsBean extraInputFields)
	{
		super(id, header, pogingNummer, resultatenModel, deelnemerToetsResolver, javascriptRenderer);
		this.extraInputFields = extraInputFields;
	}

	@Override
	protected ResultaatEditCellPanel createCellComponent(String id, IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, WebMarkupContainer cell)
	{
		return new ResultaatEditCellPanel(id, toetsModel, deelnemerModel, this, cell);
	}

	public ResultatenEditModel getEditModel()
	{
		return (ResultatenEditModel) resultatenModel;
	}

	public ExtraInputFieldsBean getExtraInputFields()
	{
		return extraInputFields;
	}
}
