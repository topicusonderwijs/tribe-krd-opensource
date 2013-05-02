package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;

public abstract class AbstractResultatenTable<T, M extends ResultatenModel> extends
		CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	private M resultatenModel;

	private ResultaatColumnCreator<T, M> columnCreator;

	public AbstractResultatenTable(M resultatenModel, ResultaatColumnCreator<T, M> columnCreator)
	{
		super("Resultaten");
		this.resultatenModel = resultatenModel;
		this.columnCreator = columnCreator;
	}

	public M getResultatenModel()
	{
		return resultatenModel;
	}

	protected void createColumn(String id, String header, int pogingNr,
			DeelnemerToetsResolver<T> deelnemerToetsResolver)
	{
		addColumn(columnCreator.createResultaatColumn(id, header, pogingNr, resultatenModel,
			deelnemerToetsResolver));
	}

	public abstract int calculateWidth(CustomDataPanel<T> datapanel);

	@Override
	public void detach()
	{
		super.detach();
		resultatenModel.detach();
	}
}
