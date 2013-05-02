package nl.topicus.cobra.web.components.datapanel.selection;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.TitleModel;

public class SelectieTitleModel extends TitleModel
{
	private static final long serialVersionUID = 1L;

	private Selection< ? , ? > selection;

	private int maxResults;

	public SelectieTitleModel(String base, CustomDataPanel< ? > dataPanel,
			Selection< ? , ? > selection, int maxResults)
	{
		super(base, dataPanel);
		this.selection = selection;
		this.maxResults = maxResults;
	}

	@Override
	public String getObject()
	{
		StringBuilder sb = new StringBuilder(super.getObject().toString());
		sb.append(" geselecteerd: ");
		sb.append(selection.size());
		if (maxResults < Integer.MAX_VALUE)
		{
			sb.append(" (maximaal ");
			sb.append(maxResults);
			sb.append(")");
		}
		return sb.toString();
	}
}
