package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.RadioColumn;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AfspraakTable.AfspraakDatumTijdModel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AfspraakTable.DatumTijdModel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class OpenstaandeAfspraakTable extends CustomDataPanelContentDescription<Afspraak>
{
	private static final long serialVersionUID = 1L;

	public OpenstaandeAfspraakTable()
	{
		super("Openstaande afspraken");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Afspraak>("Titel", "Titel", "titel", "titel"));
		addColumn(new CustomPropertyColumn<Afspraak>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Begin datum en tijd", "Begin",
			"beginDatumTijd", "beginDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new DatumTijdModel(super.createLabelModel(embeddedModel));
			}
		});
		addColumn(new CustomPropertyColumn<Afspraak>("Eind datum en tijd", "Eind", "eindDatumTijd",
			"eindDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new DatumTijdModel(super.createLabelModel(embeddedModel));
			}
		});
		addColumn(new CustomPropertyColumn<Afspraak>("Begin en eind tijd of datum", "Tijd",
			"beginDatumTijd", "beginDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new AfspraakDatumTijdModel(embeddedModel);
			}
		}.setDefaultVisible(false));
		addColumn(new RadioColumn<Afspraak, WaarnemingSoort>("aanwezig", "Aanwezig",
			new Model<WaarnemingSoort>(WaarnemingSoort.Aanwezig)));
		addColumn(new RadioColumn<Afspraak, WaarnemingSoort>("afwezig", "Afwezig",
			new Model<WaarnemingSoort>(WaarnemingSoort.Afwezig)));
		addColumn(new CustomPropertyColumn<Afspraak>("Overlappende melding",
			"Overlappende melding", "melding")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				Afspraak afspraak = embeddedModel.getObject();
				AbsentieMeldingDataAccessHelper absentieHelper =
					DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
				AbsentieMeldingZoekFilter absentieMeldingZoekFilter =
					new AbsentieMeldingZoekFilter();
				absentieMeldingZoekFilter.setDeelnemer(EduArteContext.get().getDeelnemer());
				absentieMeldingZoekFilter.setBeginDatumTijd(afspraak.getBeginDatumTijd());
				absentieMeldingZoekFilter.setEindDatumTijd(afspraak.getEindDatumTijd());
				List<AbsentieMelding> overlappendeMeldingen =
					absentieHelper.getOverlappendeMeldingen(absentieMeldingZoekFilter);

				List<String> overlappendeMeldingenOmschrijvingen =
					new ArrayList<String>(overlappendeMeldingen.size());
				for (AbsentieMelding overlappendeMelding : overlappendeMeldingen)
				{
					overlappendeMeldingenOmschrijvingen.add(overlappendeMelding.getAbsentieReden()
						.getOmschrijving());
				}
				String returnString = overlappendeMeldingenOmschrijvingen.toString();
				return new Model<String>(returnString.substring(1, returnString.length() - 1));
			}
		});
	}
}
