package nl.topicus.eduarte.web.components.panels;

import java.util.Map;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.TitleModel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

public class DeelnemerDataPanel extends EduArteDataPanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerDataPanel(String id, CustomDataPanelId panelId,
			GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> provider,
			CustomDataPanelContentDescription<Verbintenis> contentDescription)
	{
		super(id, panelId, new DeelnemerDataProvider(provider), contentDescription);
	}

	public DeelnemerDataPanel(String id,
			GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> provider,
			CustomDataPanelContentDescription<Verbintenis> contentDescription)
	{
		super(id, new DeelnemerDataProvider(provider), contentDescription);
	}

	/**
	 * Factory methode voor de titel. Standaard wordt een model gebruikt dat het volgende
	 * format gebruikt: ${title} - ${x} tot ${y} van ${z}. Waarbij x en y staan voor de
	 * range en z vor het totale aantal items.
	 * 
	 * @param title
	 * @return model om titel te renderen
	 */
	@Override
	protected IModel<String> createTitleModel(String title)
	{
		return new TitleModel(title, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				String superTitle = super.getObject();
				StringBuilder buffer = new StringBuilder(superTitle.length() + 20);
				buffer.append(superTitle)
					.replace(buffer.length() - 1, buffer.length(), " verbintenissen, ")
					.append(getAantalDeelnemers()).append(" ")
					.append(EduArteApp.get().getDeelnemerTermMeervoud()).append(")");

				return buffer.toString();
			}
		};
	}

	protected int getAantalDeelnemers()
	{
		DeelnemerDataProvider provider = (DeelnemerDataProvider) getDataProvider();
		return provider.getDeelnemerCount();
	}

	@Override
	public Map<String, Object> getAfdrukParameters()
	{
		Map<String, Object> ret = super.getAfdrukParameters();
		StringBuilder builder = new StringBuilder();
		builder.append(getContentDescription().getTitle());
		builder.append(" (");
		builder.append(getRowCount());
		builder.append(" verbintenissen, ");
		builder.append(getAantalDeelnemers());
		builder.append(" ");
		builder.append(EduArteApp.get().getDeelnemerTermMeervoud()).append(")");
		ret.put("titel", builder.toString());
		return ret;

	}

	/**
	 * Geef de deelnemer van de verbintenis terug ipv de verbintenis. Dit zorgt ervoor dat
	 * een deelnemer niet herhaald wordt op de volgende regel als het om dezelfde
	 * deelnemer gaat.
	 * 
	 * @see nl.topicus.cobra.web.components.datapanel.CustomDataPanel#getRowValue(Object
	 *      rowValue)
	 */
	@Override
	protected Object getRowValue(Verbintenis rowValue)
	{
		if (rowValue != null)
		{
			return rowValue.getDeelnemer();
		}
		return null;
	}
}
