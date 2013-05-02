package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.HerhalendeAfspraak;
import nl.topicus.eduarte.entities.participatie.InloopCollege;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class InloopCollegeTable extends CustomDataPanelContentDescription<InloopCollege>
{
	private static final long serialVersionUID = 1L;

	public InloopCollegeTable()
	{
		super("Inschrijvingen");
		addColumns();
	}

	private void addColumns()
	{
		addColumn(new CustomPropertyColumn<InloopCollege>("Omschrijving", "Omschrijving",
			"omschrijving", "Omschrijving"));
		addColumn(new CustomPropertyColumn<InloopCollege>("Onderwijsproduct", "Onderwijsproduct",
			"eersteAfspraak.onderwijsproduct"));
		addColumn(new CustomPropertyColumn<InloopCollege>("Sluiting inschrijving",
			"Sluiting inschrijving", "inschrijfEindDatum", "inschrijfEindDatum")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<InloopCollege>("Datum eerste afspraak",
			"Datum eerste afspraak", "eersteAfspraak.beginDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<InloopCollege> embeddedModel)
			{
				return new DatumTijdHerhalingModel(embeddedModel);
			}
		});
		addColumn(new CustomPropertyColumn<InloopCollege>("Locatie", "Locatie",
			"eersteAfspraak.locatie"));
		addColumn(new CustomPropertyColumn<InloopCollege>("Opmerking", "Opmerking", "opmerking"));
	}

	private static class DatumTijdHerhalingModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<InloopCollege> inloopcollegeModel;

		public DatumTijdHerhalingModel(IModel<InloopCollege> inloopcollegeModel)
		{
			this.inloopcollegeModel = inloopcollegeModel;
		}

		@Override
		public String getObject()
		{
			InloopCollege inloopcollege = inloopcollegeModel.getObject();
			Afspraak afspraak = inloopcollege.getEersteAfspraak();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			String returnString = "";
			if (TimeUtil.getInstance().isZelfdeDatum(afspraak.getBeginDatumTijd(),
				afspraak.getEindDatumTijd()))
				returnString =
					dateFormat.format(afspraak.getBeginDatum()) + " "
						+ timeFormat.format(afspraak.getBeginTijd()) + " - "
						+ timeFormat.format(afspraak.getEindTijd());
			else
				returnString =
					dateFormat.format(afspraak.getBeginDatum()) + " - "
						+ dateFormat.format(afspraak.getEindDatum());

			if (inloopcollege.isHeleHerhaling() && afspraak.getHerhalendeAfspraak() != null)
			{
				HerhalendeAfspraak herhalendeAfspraak = afspraak.getHerhalendeAfspraak();
				returnString = returnString + ", " + herhalendeAfspraak.getType().toString();
				List<Afspraak> geordendeAfspraken = herhalendeAfspraak.getAfspraken();
				Collections.sort(geordendeAfspraken, new Comparator<Afspraak>()
				{

					@Override
					public int compare(Afspraak a1, Afspraak a2)
					{
						if (a1.getBeginDatumTijd() == null || a2.getBeginDatumTijd() == null)
						{
							return 0;
						}
						return a1.getBeginDatumTijd().compareTo(a2.getBeginDatumTijd());
					}

				});
				Date einddatum =
					geordendeAfspraken.get(geordendeAfspraken.size() - 1).getEindDatum();
				SimpleDateFormat daysAndMonthFormat = new SimpleDateFormat("dd-MM");
				returnString = returnString + " tot " + daysAndMonthFormat.format(einddatum);
			}

			return returnString;
		}

		@Override
		public void detach()
		{
			inloopcollegeModel.detach();
		}
	}
}
