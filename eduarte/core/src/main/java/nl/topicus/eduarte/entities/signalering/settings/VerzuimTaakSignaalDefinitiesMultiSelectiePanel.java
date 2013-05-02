package nl.topicus.eduarte.entities.signalering.settings;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.KoppelTabelModelSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.eduarte.dao.helpers.VerzuimTaakSignaalDefinitieDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteSelectiePanel;
import nl.topicus.eduarte.zoekfilters.VerzuimTaakSignaalDefinitieZoekFilter;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public class VerzuimTaakSignaalDefinitiesMultiSelectiePanel
		extends
		EduArteSelectiePanel<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel, VerzuimTaakSignaalDefinitie, VerzuimTaakSignaalDefinitieZoekFilter>
{
	private static class VerzuimTaakSignaalDefSelection
			extends
			KoppelTabelModelSelection<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel, VerzuimTaakSignaalDefinitie>
	{
		private static final long serialVersionUID = 1L;

		private IModel<VerzuimTaakEventAbonnementConfiguration> baseModel;

		private VerzuimTaakSignaalDefSelection(
				IModel<VerzuimTaakEventAbonnementConfiguration> baseModel,
				IModel< ? extends Collection<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel>> model)
		{
			super(model);
			this.baseModel = baseModel;
		}

		@Override
		protected VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel newR(
				VerzuimTaakSignaalDefinitie object)
		{
			VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel koppel =
				new VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel();
			koppel.setSignaalDefinitie(object);
			koppel.setAbonnementConfiguration(baseModel.getObject());
			return koppel;
		}

		@Override
		protected VerzuimTaakSignaalDefinitie convertRtoS(
				VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel object)
		{
			return object.getSignaalDefinitie();
		}

		@Override
		public void detach()
		{
			super.detach();
			baseModel.detach();
		}
	}

	private static final long serialVersionUID = 1L;

	public VerzuimTaakSignaalDefinitiesMultiSelectiePanel(String id,
			AutoFieldSet<VerzuimTaakEventAbonnementConfiguration> fieldset,
			IModel<List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel>> listModel)
	{
		super(id, new VerzuimTaakSignaalDefinitieZoekFilter(), new VerzuimTaakSignaalDefSelection(
			fieldset.getModel(), listModel));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CustomDataPanelContentDescription<VerzuimTaakSignaalDefinitie> createContentDescription()
	{
		return new VerzuimTaakSignaalDefinitieTable("VerzuimTaak Signaal Definitie");
	}

	@Override
	protected Panel createZoekFilterPanel(String id, VerzuimTaakSignaalDefinitieZoekFilter filter,
			CustomDataPanel<VerzuimTaakSignaalDefinitie> customDataPanel)
	{
		return new EmptyPanel(id);
	}

	@Override
	protected String getEntityName()
	{
		return "trajecttemplates";
	}

	@Override
	protected IDataProvider<VerzuimTaakSignaalDefinitie> createDataProvider(
			VerzuimTaakSignaalDefinitieZoekFilter filter)
	{
		return GeneralFilteredSortableDataProvider.of(filter,
			VerzuimTaakSignaalDefinitieDataAccessHelper.class);
	}

	@Override
	public List<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel> getSelectedElements()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getItemsPerPage()
	{
		return 5;
	}
}
