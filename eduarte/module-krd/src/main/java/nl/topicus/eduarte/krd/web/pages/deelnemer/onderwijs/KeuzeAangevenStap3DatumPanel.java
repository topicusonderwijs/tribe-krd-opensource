package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;

import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class KeuzeAangevenStap3DatumPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public KeuzeAangevenStap3DatumPanel(String id)
	{
		super(id);
		DatumKeuze datumKeuze =
			new DatumKeuze(SoortDatum.StartVerbintenis, TimeUtil.getInstance().currentDate());
		setDefaultModel(new Model<DatumKeuze>(datumKeuze));
		final DatumField datumField =
			new DatumField("datum", new PropertyModel<Date>(getDefaultModel(), "datum"));
		datumField.setVisible(false);
		RadioChoice<SoortDatum> radioChoice =
			new RadioChoice<SoortDatum>("soortDatum", new PropertyModel<SoortDatum>(
				getDefaultModel(), "soortDatum"), Arrays.asList(SoortDatum.values()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean wantOnSelectionChangedNotifications()
				{
					return true;
				}

				@Override
				protected void onSelectionChanged(Object newSelection)
				{
					if (newSelection.equals(SoortDatum.StartVerbintenis))
					{
						datumField.setVisible(false);
						datumField.setRequired(false);
					}
					else
					{
						datumField.setVisible(true);
						datumField.setRequired(true);
					}
				}
			};
		add(radioChoice);
		add(datumField);

	}

	public enum SoortDatum
	{
		StartVerbintenis
		{
			@Override
			public String toString()
			{
				return "Start verbintenis";
			}
		},

		Zelfgedefineerd
		{
			@Override
			public String toString()
			{
				return "Zelf gedefineerd";
			}

		};
	}

	public class DatumKeuze implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private SoortDatum soortDatum;

		private Date datum;

		public DatumKeuze(SoortDatum soortDatum, Date datum)
		{
			this.soortDatum = soortDatum;
			this.datum = datum;
		}

		public SoortDatum getSoortDatum()
		{
			return soortDatum;
		}

		public void setSoortDatum(SoortDatum soortDatum)
		{
			this.soortDatum = soortDatum;
		}

		public Date getDatum()
		{
			return datum;
		}

		public void setDatum(Date datum)
		{
			this.datum = datum;
		}
	}

	public Date getDatum()
	{
		DatumKeuze keuze = (DatumKeuze) KeuzeAangevenStap3DatumPanel.this.getDefaultModelObject();
		if (keuze.getSoortDatum().equals(SoortDatum.Zelfgedefineerd))
			return keuze.getDatum();
		return null;
	}
}