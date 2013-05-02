package nl.topicus.eduarte.web.components.resultaat;

import java.math.BigDecimal;
import java.util.Date;

import nl.topicus.cobra.converters.BigDecimalConverter;
import nl.topicus.cobra.web.components.labels.NewlinePreservingLabel;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

public class ResultaatCellPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	protected IModel<Toets> toetsModel;

	protected ResultaatColumn< ? > column;

	protected IModel<Deelnemer> deelnemerModel;

	protected HiddenField<Object> editWaarde;

	public ResultaatCellPanel(String id, IModel<Toets> toetsModel,
			IModel<Deelnemer> deelnemerModel, ResultaatColumn< ? > column, WebMarkupContainer cell)
	{
		super(id);

		this.toetsModel = toetsModel;
		this.deelnemerModel = deelnemerModel;
		this.column = column;

		setRenderBodyOnly(true);
		IModel<Object> resultaatModel = createResultaatModel();

		createField(resultaatModel, cell);
		createDatumBehaaldField();
		createNotitieField();
		createWaardeLabel();
		createTraceLabel();
	}

	public Resultaat getResultaat()
	{
		return column.getResultaat(toetsModel, deelnemerModel);
	}

	private IModel<Object> createResultaatModel()
	{
		return new IModel<Object>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return column.getDisplayWaarde(getResultaat());
			}

			@Override
			public void setObject(Object object)
			{
				saveResultaat(object);
			}

			@Override
			public void detach()
			{
			}
		};
	}

	@SuppressWarnings("unused")
	protected void createField(IModel<Object> resultaatModel, WebMarkupContainer cell)
	{
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
		};
		editWaarde.setEnabled(false);
		add(editWaarde);
	}

	protected void createDatumBehaaldField()
	{
		HiddenField<Date> datumBehaald =
			new HiddenField<Date>("datumBehaald", new PropertyModel<Date>(this,
				"resultaat.datumBehaald"));
		datumBehaald.setEnabled(false);
		add(datumBehaald);
	}

	protected void createNotitieField()
	{
		HiddenField<String> notitie =
			new HiddenField<String>("notitie", new PropertyModel<String>(this, "resultaat.notitie"));
		notitie.setEnabled(false);
		add(notitie);
	}

	protected void createTraceLabel()
	{
		add(new NewlinePreservingLabel("trace", new PropertyModel<String>(this,
			"resultaat.berekening"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getLineSeparator()
			{
				return "<br class=\"below_5\"/>";
			}
		});
	}

	protected void createWaardeLabel()
	{
		add(new Label("displayWaarde", new AbstractReadOnlyModel<Object>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return editWaarde.getModelObject();
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public IConverter getConverter(Class type)
			{
				return editWaarde.getConverter(type);
			}
		});
	}

	protected Toets getToets()
	{
		return toetsModel.getObject();
	}

	protected Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		toetsModel.detach();
		deelnemerModel.detach();
	}

	@SuppressWarnings("unused")
	protected void saveResultaat(Object object)
	{
		throw new UnsupportedOperationException("Cannot set the resultaat in view mode");
	}

	public boolean hasFeedback()
	{
		return editWaarde.hasFeedbackMessage();
	}

	public String getFeedback()
	{
		return editWaarde.getFeedbackMessage().getMessage().toString();
	}

	public String getUserMessage()
	{
		return null;
	}
}
