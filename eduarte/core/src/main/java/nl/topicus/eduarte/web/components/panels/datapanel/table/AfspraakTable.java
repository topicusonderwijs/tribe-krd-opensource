package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.text.SimpleDateFormat;
import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.Afspraak;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Definieert de default kolommen voor een lijst met afspraken.
 * 
 * @author loite
 */
public class AfspraakTable extends CustomDataPanelContentDescription<Afspraak>
{

	private static final long serialVersionUID = 1L;

	public static class DatumTijdModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Date> base;

		@SuppressWarnings("unchecked")
		public DatumTijdModel(IModel< ? > base)
		{
			this.base = (IModel<Date>) base;
		}

		@Override
		public String getObject()
		{
			Date date = base.getObject();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			return format.format(date);
		}

		@Override
		public void detach()
		{
			base.detach();
		}
	}

	/**
	 * @author loite
	 */
	public static class AfspraakLesuurModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Afspraak> afspraakModel;

		public AfspraakLesuurModel(IModel<Afspraak> afspraakModel)
		{
			this.afspraakModel = afspraakModel;
		}

		@Override
		public String getObject()
		{
			Afspraak afspraak = afspraakModel.getObject();
			if (afspraak.getBeginLesuur() == null || afspraak.getEindLesuur() == null)
				return "n.v.t.";
			return afspraak.getBeginLesuur() + " - " + afspraak.getEindLesuur();
		}

		@Override
		public void detach()
		{
			afspraakModel.detach();
		}
	}

	/**
	 * @author loite
	 */
	public static class AfspraakDatumTijdModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Afspraak> afspraakModel;

		public AfspraakDatumTijdModel(IModel<Afspraak> afspraakModel)
		{
			this.afspraakModel = afspraakModel;
		}

		@Override
		public String getObject()
		{
			Afspraak afspraak = afspraakModel.getObject();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			if (TimeUtil.getInstance().isZelfdeDatum(afspraak.getBeginDatumTijd(),
				afspraak.getEindDatumTijd()))
				return dateFormat.format(afspraak.getBeginDatum()) + " "
					+ timeFormat.format(afspraak.getBeginTijd()) + " - "
					+ timeFormat.format(afspraak.getEindTijd());
			return dateFormat.format(afspraak.getBeginDatum()) + " - "
				+ dateFormat.format(afspraak.getEindDatum());
		}

		@Override
		public void detach()
		{
			afspraakModel.detach();
		}
	}

	public AfspraakTable()
	{
		this("Afspraken");
	}

	public AfspraakTable(String titel)
	{
		super(titel);
		addColumns();
		addGroupProperties();
	}

	private void addColumns()
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
		}.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Eind datum en tijd", "Eind", "eindDatumTijd",
			"eindDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new DatumTijdModel(super.createLabelModel(embeddedModel));
			}
		}.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Begin- en eindtijd of datum", "Tijd",
			"beginDatumTijd", "beginDatumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new AfspraakDatumTijdModel(embeddedModel);
			}
		});
		addColumn(new CustomPropertyColumn<Afspraak>("Begin- en eindlesuur", "Lesuren",
			"beginLesuur", "beginLesuur")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<Afspraak> embeddedModel)
			{
				return new AfspraakLesuurModel(embeddedModel);
			}
		});

		addColumn(new CustomPropertyColumn<Afspraak>("Beginlesuur", "Beginlesuur", "beginLesuur",
			"beginLesuur").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Eindlesuur", "Eindlesuur", "eindLesuur",
			"eindLesuur").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Onderwijsproduct", "Onderwijsproduct",
			"onderwijsproduct", "onderwijsproduct.code"));
		addColumn(new CustomPropertyColumn<Afspraak>("Organisatie-eenheid", "Organisatie-eenheid",
			"organisatieEenheid", "organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<Afspraak>("Locatie", "Locatie", "afspraakLocatie",
			"afspraakLocatie"));
		addColumn(new CustomPropertyColumn<Afspraak>("Medewerker", "Medewerker", "docentNamen"));
		addColumn(new CustomPropertyColumn<Afspraak>("Medewerker (roostercode)", "Roostercode",
			"docentRoostercodes"));
		addColumn(new CustomPropertyColumn<Afspraak>("Groep", "Groep", "groepNamen")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Afspraaktype", "Type", "afspraakType",
			"afspraakType.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Afspraak>("Minuten IIVO", "IIVO", "minutenIIVO",
			"minutenIIVO").setDefaultVisible(false));
	}

	private void addGroupProperties()
	{
		addGroupProperty(new GroupProperty<Afspraak>("organisatieEenheid.naam",
			"Organisatie-eenheid", "organisatieEenheid.naam"));
		addGroupProperty(new GroupProperty<Afspraak>("onderwijsproduct.code", "Onderwijsproduct",
			"onderwijsproduct.code"));
		addGroupProperty(new GroupProperty<Afspraak>("groepeerDatumOmschrijving", "Datum/tijd",
			"beginDatumTijd"));
	}
}
