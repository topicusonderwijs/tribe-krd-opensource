package nl.topicus.eduarte.web.components.modalwindow.adres;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class AdressenEditPanel<T extends AdresEntiteit<T>, A extends Adresseerbaar<T>> extends
		TypedPanel<A>
{
	private static final long serialVersionUID = 1L;

	public AdressenEditPanel(String id, IModel<A> model, ModelManager manager)
	{
		this(id, model, manager, true);
	}

	public AdressenEditPanel(String id, IModel<A> model, ModelManager manager, boolean verplicht)
	{
		super(id, model);

		if (!verplicht || getEntiteit().isSaved())
		{
			ListAdressenEditPanel<T, A> listPanel =
				new ListAdressenEditPanel<T, A>("innerPanel", model, manager)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSaveCurrent(AjaxRequestTarget target, T object)
					{
						AdressenEditPanel.this.onSaveCurrent(target, object);
					}

					@Override
					protected void onSaveNew(AjaxRequestTarget target, T object)
					{
						AdressenEditPanel.this.onSaveNew(target, object);
					}
				};
			listPanel.setAdressenVerplicht(verplicht);
			add(listPanel);
		}
		else
			add(new InlineAdressenEditPanel<T, A>("innerPanel", model));
	}

	@SuppressWarnings("unused")
	protected void onSaveCurrent(AjaxRequestTarget target, T object)
	{
	}

	@SuppressWarnings("unused")
	protected void onSaveNew(AjaxRequestTarget target, T object)
	{
	}

	public A getEntiteit()
	{
		return getModelObject();
	}
}
