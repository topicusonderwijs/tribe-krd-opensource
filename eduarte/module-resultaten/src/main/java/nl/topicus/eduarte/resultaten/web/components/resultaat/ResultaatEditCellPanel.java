package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.math.BigDecimal;
import java.util.Date;

import nl.topicus.cobra.converters.BigDecimalConverter;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.web.components.resultaat.ResultaatCellPanel;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

public class ResultaatEditCellPanel extends ResultaatCellPanel
{
	private static final long serialVersionUID = 1L;

	public ResultaatEditCellPanel(String id, IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, ResultaatEditColumn< ? > column,
			WebMarkupContainer cell)
	{
		super(id, toetsModel, deelnemerModel, column, cell);
	}

	@Override
	protected void createField(IModel<Object> resultaatModel, WebMarkupContainer cell)
	{
		cell.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return editWaarde.hasErrorMessage() ? "panelError" : null;
			}
		}, " "));

		cell.add(new AttributeAppender("title", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				if (editWaarde.hasFeedbackMessage())
				{
					return "Invoerfout - "
						+ editWaarde.getFeedbackMessage().getMessage().toString();
				}
				return null;
			}
		}, " "));

		editWaarde = new HiddenField<Object>("editWaarde", resultaatModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public IConverter getConverter(Class< ? > type)
			{
				if (type.equals(BigDecimal.class))
				{
					return new BigDecimalConverter(getToets().getSchaal().getAantalDecimalen(),
						true);
				}
				return super.getConverter(type);
			}

			@Override
			public IModelComparator getModelComparator()
			{
				return new IModelComparator()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean compare(Component component, Object b)
					{
						final Object a = component.getDefaultModelObject();
						if (a == null && b == null)
							return true;

						if (a == null || b == null)
							return false;

						if (a instanceof BigDecimal && b instanceof BigDecimal)
							return ((BigDecimal) a).compareTo((BigDecimal) b) == 0;
						return a.equals(b);
					}
				};
			}
		};

		editWaarde.setConvertEmptyInputStringToNull(true);
		boolean aanpasbaar = column.isAanpasbaar(getToets(), getDeelnemer());
		editWaarde.setEnabled(aanpasbaar);
		if (aanpasbaar)
		{
			if (column.isCijferColumn(getToets()))
			{
				Class< ? > invoerType = getToets().getInvoerType();
				if (!invoerType.equals(String.class))
					editWaarde.setType(invoerType);
				editWaarde.add(new ToetsCijferValidator(toetsModel, editWaarde));
			}
			else
			{
				editWaarde.setType(Integer.class);
				editWaarde.add(new ToetsScoreValidator(toetsModel, editWaarde, column.getPoging()));
			}
		}
		else
		{
			String message = null;
			if (getToets() != null && !getToets().getResultaatstructuur().isBeschikbaar())
			{
				message =
					"De resultaatstructuur van '"
						+ getToets().getResultaatstructuur().getOnderwijsproduct().getCode()
						+ "' is niet beschikbaar. Resultaten behorende bij deze structuur "
						+ "kunnen niet bewerkt worden.";
				addMessage(message);
			}
		}
		add(editWaarde);
	}

	private void addMessage(String message)
	{
		for (Object curMessageObj : Session.get().getFeedbackMessages().messages(null))
		{
			FeedbackMessage curMessage = (FeedbackMessage) curMessageObj;
			if (curMessage.getMessage().equals(message))
				return;
		}
		Session.get().getFeedbackMessages().info(this, message);
	}

	@Override
	protected void createDatumBehaaldField()
	{
		HiddenField<Date> datumBehaald =
			new HiddenField<Date>("datumBehaald", new PropertyModel<Date>(this,
				"resultaat.datumBehaald")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void setObject(Date object)
				{
					if (getResultaat() != null && object != null)
						super.setObject(object);
				}
			});
		add(datumBehaald);
	}

	@Override
	protected void createNotitieField()
	{
		HiddenField<String> notitie =
			new HiddenField<String>("notitie", new PropertyModel<String>(this, "resultaat.notitie")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void setObject(String object)
				{
					if (getResultaat() != null)
						super.setObject(object);
				}
			});
		add(notitie);
	}

	@Override
	protected void createWaardeLabel()
	{
		add(new Label("displayWaarde", new AbstractReadOnlyModel<Object>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				boolean useInput = editWaarde.hasRawInput();
				return useInput ? editWaarde.getInput() : editWaarde.getModelObject();
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public IConverter getConverter(Class< ? > type)
			{
				return editWaarde.getConverter(type);
			}
		});
	}

	@Override
	protected void saveResultaat(Object object)
	{
		ResultatenEditModel editModel = getColumn().getEditModel();
		Resultaat oudResultaat = getResultaat();
		Resultaat nieuwResultaat = new Resultaat();
		Resultaat cijferResultaat =
			editModel.getResultaat(toetsModel, deelnemerModel, ResultatenModel.RESULTAAT_NR);
		nieuwResultaat.setDeelnemer(getDeelnemer());
		nieuwResultaat.setIngevoerdDoor(EduArteContext.get().getMedewerker());
		nieuwResultaat.setToets(getToets());

		switch (column.getPoging())
		{
			case ResultatenModel.ALTERNATIEF_NR:
				updateAlternatiefResultaat(object, nieuwResultaat);
				break;
			case ResultatenModel.RESULTAAT_NR:
				updateGeldendResultaat(object, oudResultaat, nieuwResultaat);
				break;
			default:
				updatePoging(object, nieuwResultaat);
				break;
		}

		boolean cijferOverschrevenInZelfdeRequest =
			isCijferOverschrevenInZelfdeRequest(cijferResultaat, oudResultaat, nieuwResultaat);

		if (!cijferOverschrevenInZelfdeRequest || oudResultaat != cijferResultaat)
		{
			if (oudResultaat != null)
			{
				nieuwResultaat.setOverschrijft(oudResultaat);
				oudResultaat.setActueel(false);
				oudResultaat.setGeldend(false);
				oudResultaat.setInSamengesteld(false);
				oudResultaat.update();
			}
		}
		nieuwResultaat.setActueel(!cijferOverschrevenInZelfdeRequest);
		if (cijferOverschrevenInZelfdeRequest && nieuwResultaat.getScore() != null)
			nieuwResultaat.setCijferOfWaardeUitScore(nieuwResultaat.getScore());
		nieuwResultaat.setDatumBehaald(getColumn().getExtraInputFields().getDatumBehaald());

		if (cijferOverschrevenInZelfdeRequest)
		{
			cijferResultaat.setScore(nieuwResultaat.getScore());
			// voorkom dat een nullresultaat een ander nullresultaat overschrijft
			if (!cijferResultaat.isNullResultaat() || !nieuwResultaat.isNullResultaat())
			{
				nieuwResultaat.save();
				cijferResultaat.setOverschrijft(nieuwResultaat);
			}
			cijferResultaat.update();
		}
		else
		{
			nieuwResultaat.save();
			editModel.updateResultaat(nieuwResultaat, column.getPoging());
			editModel.addRecalcuation(toetsModel.getObject(), deelnemerModel.getObject());
		}
	}

	private ResultaatEditColumn< ? > getColumn()
	{
		return (ResultaatEditColumn< ? >) column;
	}

	private boolean isCijferOverschrevenInZelfdeRequest(Resultaat cijferResultaat,
			Resultaat oudResultaat, Resultaat nieuwResultaat)
	{
		// nieuwe resultaten zijn nog niet geldend (wordt pas bij doorrekening gedaan)
		if (cijferResultaat == null || cijferResultaat.isGeldend())
			return false;

		// kijk of het cijferresultaat wel het oude resultaat overschrijft
		if (!isCijferEqual(cijferResultaat.getOverschrijft(), oudResultaat))
			return false;

		if (cijferResultaat.getHerkansingsnummer() != nieuwResultaat.getHerkansingsnummer())
			return false;

		// alternatieve resultaten hebben niet te maken met de andere resultaten
		return !nieuwResultaat.getSoort().equals(Resultaatsoort.Alternatief);
	}

	@SuppressWarnings("null")
	private boolean isCijferEqual(Resultaat r1, Resultaat r2)
	{
		if (JavaUtil.equalsOrBothNull(r1, r2))
			return true;
		if (r1 == null && r2.isNullResultaat())
			return true;
		if (r2 == null && r1.isNullResultaat())
			return true;
		return false;
	}

	private void updateAlternatiefResultaat(Object waarde, Resultaat nieuwResultaat)
	{
		nieuwResultaat.setSoort(Resultaatsoort.Alternatief);
		nieuwResultaat.setCijferOfWaarde(waarde);
		nieuwResultaat.appendBerekening("Alternatief resultaat ingevoerd als '" + waarde + "'");
	}

	private void updateGeldendResultaat(Object waarde, Resultaat oudResultaat,
			Resultaat nieuwResultaat)
	{
		if (waarde == null)
		{
			nieuwResultaat.setSoort(Resultaatsoort.Ingevoerd);
			nieuwResultaat.appendBerekening("Resultaat leeg gemaakt");
		}
		else
		{
			nieuwResultaat.setSoort(Resultaatsoort.Overschreven);
			nieuwResultaat.setCijferOfWaarde(waarde);
			nieuwResultaat.appendBerekening("Resultaat overschreven als '" + waarde + "'");
		}
		if (oudResultaat != null)
		{
			nieuwResultaat.setHerkansingsnummer(oudResultaat.getHerkansingsnummer());
			nieuwResultaat.setScore(oudResultaat.getScore());
			nieuwResultaat.appendBerekening("Herkansingsnummer ("
				+ oudResultaat.getHerkansingsnummer() + ") en score ("
				+ (oudResultaat.getScore() == null ? "-" : oudResultaat.getScore())
				+ ") overgenomen uit het vorige resultaat");
		}
	}

	private void updatePoging(Object object, Resultaat nieuwResultaat)
	{
		nieuwResultaat.setSoort(Resultaatsoort.Ingevoerd);
		nieuwResultaat.setHerkansingsnummer(column.getPoging() - 1);
		if (getToets().hasScoreschaal())
			nieuwResultaat.setScore((Integer) object);
		else
			nieuwResultaat.setCijferOfWaarde(object);
		nieuwResultaat.appendBerekening("Resultaat ingevoerd als '" + object + "' voor poging "
			+ column.getPoging());
	}

	@Override
	public String getUserMessage()
	{
		return column.getNietAanpasbaarMessage(getToets(), getDeelnemer());
	}
}
