package nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.bekostiging;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class BekostigingsperiodeEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<Bekostigingsperiode>
{
	private static final long serialVersionUID = 1L;

	public BekostigingsperiodeEditPanel(String id, IModel<List<Bekostigingsperiode>> model,
			ModelManager manager)
	{
		super(id, model, manager, new BekostigingsTable());
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<Bekostigingsperiode> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<Bekostigingsperiode> modalWindow)
	{
		return new BekostigingsperiodeModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Bekostigingsperiode toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Bekostigingsperiode";
	}

	private static class BekostigingsTable extends
			CustomDataPanelContentDescription<Bekostigingsperiode>
	{
		private static final long serialVersionUID = 1L;

		public BekostigingsTable()
		{
			super("Bekostigingsperiodes");
			addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Van", "Van", "begindatum"));
			addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Tot", "Tot", "einddatum"));
			addColumn(new BooleanPropertyColumn<Bekostigingsperiode>("Bekostigd", "Bekostigd",
				"begindatum", "bekostigd"));
		}
	}

	@Override
	protected boolean isDeletable()
	{
		return false;
	}

}
