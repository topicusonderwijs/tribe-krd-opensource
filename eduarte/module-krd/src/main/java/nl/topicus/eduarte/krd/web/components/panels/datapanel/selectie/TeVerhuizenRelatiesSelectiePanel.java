package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RelatieTable;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public abstract class TeVerhuizenRelatiesSelectiePanel extends
		EduArteSelectiePanel<Serializable, Relatie, RelatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Relatie>> relaties;

	public TeVerhuizenRelatiesSelectiePanel(String id, Selection<Serializable, Relatie> selection,
			IModel<List<Relatie>> relaties)
	{
		super(id, null, selection);
		this.relaties = relaties;
	}

	protected abstract Persoon getPersoon();

	@Override
	protected CustomDataPanelContentDescription<Relatie> createContentDescription()
	{
		return new RelatieTable<Relatie>(getPersoon().isMeerderjarig())
			.setTitle("Mee te verhuizen relaties");
	}

	@Override
	protected IDataProvider<Relatie> createDataProvider(RelatieZoekFilter filter)
	{
		return new ListModelDataProvider<Relatie>(relaties);
	}

	@Override
	protected Panel createZoekFilterPanel(String id, RelatieZoekFilter filter,
			CustomDataPanel<Relatie> customDataPanel)
	{
		return new EmptyPanel(id);
	}

	@Override
	protected String getEntityName()
	{
		return "Mee te verhuizen relaties";
	}

	@Override
	public List<Serializable> getSelectedElements()
	{
		throw new UnsupportedOperationException();
	}
}
