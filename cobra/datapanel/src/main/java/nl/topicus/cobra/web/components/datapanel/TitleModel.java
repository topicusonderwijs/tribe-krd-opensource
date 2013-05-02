package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.model.IModel;

/**
 * Toont de titel in het volgende format: ${title} - ${x} tot ${y} van ${z}. Waarbij x en
 * y staan voor de range en z vor het totale aantal items.
 * 
 * @author marrink
 */
public class TitleModel implements IModel<String>
{
	private static final long serialVersionUID = 1L;

	private String base;

	private CustomDataPanel< ? > dataPanel;

	public TitleModel(String base, CustomDataPanel< ? > dataPanel)
	{
		super();
		this.base = base;
		this.dataPanel = dataPanel;
	}

	@Override
	public String getObject()
	{
		int van = 0;
		int tot = 0;
		int totaal = 0;

		van = dataPanel.getViewOffset();
		tot = van + dataPanel.getViewSize();
		if (tot > 0)
			van++; // van begint bij 1
		totaal = dataPanel.getRowCount();

		StringBuilder buffer = new StringBuilder(30);
		buffer.append(base).append(" - ").append(van).append(" t/m ").append(tot).append(" (van ")
			.append(totaal).append(")");
		return buffer.toString();
	}

	@Override
	public void setObject(String object)
	{
		base = object;
	}

	@Override
	public void detach()
	{
	}
}