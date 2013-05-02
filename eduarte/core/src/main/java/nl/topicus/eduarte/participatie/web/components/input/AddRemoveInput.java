package nl.topicus.eduarte.participatie.web.components.input;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.participatie.web.components.modalwindow.AddRemoveModalWindow;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Een inputveld waarmee 'complexe' objecten toegevoegd en verwijderd kunnen worden. Er
 * wordt een checkbox getoond, die bij aanvinken een modal window opent, waar het object
 * bewerkt kan worden. Bij het uitvinken wordt de property weer op null gezet (normaal
 * gesproken). Het toevoegen, verwijderen en updaten van de property verloopt via de
 * abstract methods in {@link AddRemoveModalWindow}
 * 
 * @author papegaaij
 */
public class AddRemoveInput<T> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	private boolean editable = true;

	public AddRemoveInput(String id, final IModel<T> model, final AddRemoveModalWindow editWindow)
	{
		super(id, model);
		add(new AjaxCheckBox("checkbox", new IModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return model.getObject() != null;
			}

			@Override
			public void setObject(Boolean object)
			{
			}

			@Override
			public void detach()
			{
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (model.getObject() == null)
				{
					editWindow.addProperty(target);
					editWindow.setDeleteOnClose(true);
					editWindow.show(target);
				}
				else
				{
					editWindow.removeProperty(target);
					editWindow.refreshProperty(target);
				}
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && AddRemoveInput.this.isEnabled();
			}

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && isEditable();
			}
		});

		AjaxLink<Void> editLink = new AjaxLink<Void>("editLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				editWindow.show(target);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && model.getObject() != null;
			}

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && AddRemoveInput.this.isEnabled();
			}
		};
		editLink.setOutputMarkupPlaceholderTag(true);
		add(editLink);
		editLink.add(new Label("linkString", model));
		editLink.add(new AttributeModifier("title", true, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return isEditable() ? "Bewerken" : "Bekijken";
			}
		}));
		setOutputMarkupId(true);
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
}
