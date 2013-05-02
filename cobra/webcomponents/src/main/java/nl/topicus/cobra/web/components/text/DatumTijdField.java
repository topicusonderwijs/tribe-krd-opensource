package nl.topicus.cobra.web.components.text;

import java.util.Date;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DatumTijdField extends FormComponentPanel<Date>
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> requiredModel;

	// datum deel uit het model
	protected Date datum;

	// tijd deel uit het model.
	protected Time tijd;

	protected DatumField datumField;

	protected TijdField tijdField;

	public DatumTijdField(String id)
	{
		this(id, null);
	}

	public DatumTijdField(String id, IModel<Date> model)
	{
		super(id, model);
		setType(Date.class);

		datumField = new DatumField("datum", new PropertyModel<Date>(this, "datum"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return isDatumRequired();
			}

			@Override
			public boolean isEnabled()
			{
				return isDatumEnabled();
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				DatumTijdField.this.convertInput();
				DatumTijdField.this.updateModel();
				DatumTijdField.this.dateChanged(target);
				DatumTijdField.this.onUpdate(target);
			}
		};
		add(datumField);

		tijdField = new TijdField("tijd", new PropertyModel<Time>(this, "tijd"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return isTijdRequired();
			}

			@Override
			public boolean isEnabled()
			{
				return isTijdEnabled();
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target, Time newValue)
			{
				DatumTijdField.this.convertInput();
				DatumTijdField.this.updateModel();
				DatumTijdField.this.timeChanged(target);
				DatumTijdField.this.onUpdate(target);
			}
		};
		add(tijdField);
	}

	public void setRequiredModel(IModel<Boolean> requiredModel)
	{
		this.requiredModel = requiredModel;
	}

	@Override
	protected void onBeforeRender()
	{
		Date modelDate = getModelObject();
		if (modelDate != null)
		{
			datum = TimeUtil.getInstance().asDate(modelDate);
			tijd = new Time(TimeUtil.getInstance().asTime(modelDate).getTime());
		}
		super.onBeforeRender();
	}

	@Override
	protected void convertInput()
	{
		Date inputDate = datumField.getConvertedInput();
		if (inputDate == null)
			inputDate = datum;

		Time inputTime = tijdField.getConvertedInput();
		if (inputTime == null)
			inputTime = tijd;

		if (inputDate != null && inputTime != null)
			setConvertedInput(TimeUtil.getInstance().setTimeOnDate(inputDate, inputTime));
		else
			setConvertedInput(null);
	}

	@Override
	public boolean isRequired()
	{
		if (requiredModel != null)
			return requiredModel.getObject();
		return super.isRequired();
	}

	protected boolean isDatumRequired()
	{
		return isRequired();
	}

	protected boolean isTijdRequired()
	{
		return isRequired();
	}

	protected boolean isDatumEnabled()
	{
		return isEnabled();
	}

	protected boolean isTijdEnabled()
	{
		return isEnabled();
	}

	@SuppressWarnings("unused")
	protected void dateChanged(AjaxRequestTarget target)
	{
	}

	@SuppressWarnings("unused")
	protected void timeChanged(AjaxRequestTarget target)
	{
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target)
	{
	}
}
