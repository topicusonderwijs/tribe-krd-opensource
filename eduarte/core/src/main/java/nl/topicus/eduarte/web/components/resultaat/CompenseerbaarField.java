package nl.topicus.eduarte.web.components.resultaat;

import java.math.BigDecimal;

import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.web.components.choice.SchaalwaardeCombobox;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class CompenseerbaarField extends FormComponentPanel<BigDecimal>
{
	private static final long serialVersionUID = 1L;

	private IModel<Toets> toetsModel;

	public CompenseerbaarField(String id, IModel<BigDecimal> model, IModel<Toets> toetsModel)
	{
		super(id, model);

		this.toetsModel = toetsModel;

		TextField<BigDecimal> text = new TextField<BigDecimal>("text", model, BigDecimal.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Toets toets = getToets();
				return super.isVisible()
					&& (toets == null || toets.getSchaal() == null || toets.getSchaal()
						.getSchaaltype() == Schaaltype.Cijfer);
			}
		};
		text.add(new AjaxFormComponentSaveBehaviour());
		add(text);

		SchaalwaardeCombobox choice = new SchaalwaardeCombobox("choice", new IModel<Schaalwaarde>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Schaalwaarde getObject()
			{
				BigDecimal value = CompenseerbaarField.this.getModelObject();
				if (value == null)
					return null;
				Schaal schaal = getToets().getSchaal();
				return schaal.findWaarde(value);
			}

			@Override
			public void setObject(Schaalwaarde object)
			{
				CompenseerbaarField.this.setModelObject(object == null ? null : object
					.getNominaleWaarde());
			}

			@Override
			public void detach()
			{
			}
		}, new PropertyModel<Schaal>(toetsModel, "schaal"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Toets toets = getToets();
				return super.isVisible()
					&& (toets != null && toets.getSchaal() != null && toets.getSchaal()
						.getSchaaltype() == Schaaltype.Tekstueel);
			}
		};
		choice.setNullValid(true);
		choice.setAddAjax(true);
		add(choice);
	}

	@Override
	public void updateModel()
	{
		// do nothing
	}

	public Toets getToets()
	{
		return toetsModel.getObject();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		toetsModel.detach();
	}
}
