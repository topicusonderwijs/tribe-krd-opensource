package nl.topicus.eduarte.web.components.text;

import java.util.Date;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.TijdField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class SettableDatumTijdField extends FormComponentPanel<Date>
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> requiredModel;

	protected Date datum;

	protected Time tijd;

	protected DatumField datumField;

	protected TijdField tijdField;

	public SettableDatumTijdField(String id)
	{
		this(id, null);
	}

	public SettableDatumTijdField(String id, IModel<Date> model)
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
				SettableDatumTijdField.this.convertInput();
				SettableDatumTijdField.this.updateModel();
				SettableDatumTijdField.this.dateChanged(target);
				SettableDatumTijdField.this.onUpdate(target);
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
				SettableDatumTijdField.this.convertInput();
				SettableDatumTijdField.this.updateModel();
				SettableDatumTijdField.this.timeChanged(target);
				SettableDatumTijdField.this.onUpdate(target);
			}
		};
		add(tijdField);
		Date modelDate = getModelObject();
		if (modelDate != null)
			tijd = new Time(TimeUtil.getInstance().asTime(modelDate).getTime());
	}

	public void setRequiredModel(IModel<Boolean> requiredModel)
	{
		this.requiredModel = requiredModel;
	}

	public void setDate(Date date)
	{
		datumField.clearInput();
		if (tijd != null)
			date = TimeUtil.getInstance().setTimeOnDate(date, tijd);
		setModelObject(date);

	}

	@Override
	protected void onBeforeRender()
	{
		Date modelDate = getModelObject();
		if (modelDate != null)
		{
			datum = TimeUtil.getInstance().asDate(modelDate);
			if (tijdField.getConvertedInput() != null)
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
