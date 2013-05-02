package nl.topicus.eduarte.web.components.text;

import java.util.Date;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;

public class GeboortedatumField extends FormComponentPanel<Date>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer geboortedatumPanel;

	protected DatumField geboortedatumField;

	protected TextField<Date> geboorteMaandJaarField;

	protected EnumCombobox<ToepassingGeboortedatum> toepassingCombo;

	public GeboortedatumField(String id, IModel<Date> geboortedatumModel,
			IModel<ToepassingGeboortedatum> toepassingModel)
	{
		super(id, geboortedatumModel);

		geboortedatumPanel = new WebMarkupContainer("geboortedatumPanel");

		geboortedatumField = new DatumField("geboortedatum", new Model<Date>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return isGeboortedatumRequired();
			}

			@Override
			public boolean isEnabled()
			{
				return isGeboortedatumEnabled();
			}

			@Override
			public boolean isVisible()
			{
				return getToepassingGeboortedatum() != ToepassingGeboortedatum.Geboortejaar
					&& getToepassingGeboortedatum() != ToepassingGeboortedatum.GeboortemaandEnJaar;
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				geboorteMaandJaarField.setModelObject(geboortedatumField.getModelObject());
				GeboortedatumField.this.onUpdate(target);
			}
		};
		geboortedatumPanel.add(geboortedatumField);

		geboorteMaandJaarField = new TextField<Date>("geboortemaandjaar", new Model<Date>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("unchecked")
			public IConverter getConverter(Class type)
			{
				return new GeboorteMaandJaarConverter(getToepassingGeboortedatum());
			}

			@Override
			public boolean isRequired()
			{
				return isGeboortedatumRequired();
			}

			@Override
			public boolean isEnabled()
			{
				return isGeboortedatumEnabled();
			}

			@Override
			public boolean isVisible()
			{
				return getToepassingGeboortedatum() == ToepassingGeboortedatum.Geboortejaar
					|| getToepassingGeboortedatum() == ToepassingGeboortedatum.GeboortemaandEnJaar;
			}
		};
		geboorteMaandJaarField.setType(Date.class);

		geboorteMaandJaarField.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				geboortedatumField.setDefaultModelObject(geboorteMaandJaarField.getModelObject());
				GeboortedatumField.this.onUpdate(target);
			}
		});
		geboortedatumPanel.add(geboorteMaandJaarField);

		add(geboortedatumPanel);
		geboortedatumPanel.setOutputMarkupId(true);

		toepassingCombo =
			new EnumCombobox<ToepassingGeboortedatum>("toepassingGeboortedatum", toepassingModel,
				true, ToepassingGeboortedatum.values())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return isToepassingEnabled();
				}

				@Override
				protected void onUpdate(AjaxRequestTarget target,
						ToepassingGeboortedatum newSelection)
				{
					FeedbackComponent feedback = findParent(FeedbackComponent.class);
					if (feedback != null)
						feedback.refreshFeedback(target);
					geboortedatumField.clearInput();
					geboorteMaandJaarField.clearInput();
					target.addComponent(geboortedatumPanel);
					GeboortedatumField.this.onUpdate(target);
				}
			};
		add(toepassingCombo);
		toepassingCombo.setNullValid(true);
	}

	@Override
	protected void onBeforeRender()
	{
		Date modelDate = getModelObject();
		geboortedatumField.setModelObject(modelDate);
		geboorteMaandJaarField.setModelObject(modelDate);
		super.onBeforeRender();
	}

	@Override
	protected void convertInput()
	{
		if (geboortedatumField.isVisible())
		{
			setConvertedInput(geboortedatumField.getConvertedInput());
		}
		else if (geboorteMaandJaarField.isVisible())
		{
			setConvertedInput(geboorteMaandJaarField.getConvertedInput());
		}
	}

	protected boolean isGeboortedatumRequired()
	{
		return isRequired();
	}

	protected boolean isToepassingRequired()
	{
		return isRequired();
	}

	protected boolean isGeboortedatumEnabled()
	{
		return isEnabled();
	}

	protected boolean isToepassingEnabled()
	{
		return isEnabled();
	}

	protected ToepassingGeboortedatum getToepassingGeboortedatum()
	{
		return toepassingCombo.getModelObject();
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target)
	{
	}

	public DatumField getDatumField()
	{
		return geboortedatumField;
	}
}
