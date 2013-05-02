package nl.topicus.eduarte.web.components.modalwindow.verbintenis;

import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

public class InschrijfverzoekVerificatieEditPanel<T> extends ModalWindowBasePanel<T>
{
	private static final long serialVersionUID = 1L;

	private Form<T> form;

	private List<WebMarkupContainer> refreshComponents;

	public InschrijfverzoekVerificatieEditPanel(String id, CobraModalWindow<T> modalWindow,
			AutoFieldSet<T> fieldset, List<WebMarkupContainer> refreshComponents)
	{
		super(id, modalWindow);
		this.refreshComponents = refreshComponents;
		form = new Form<T>("form");
		fieldset.setRenderMode(RenderMode.EDIT);
		form.add(fieldset);
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton submitLink = new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > verificatieForm)
			{
				for (Component component : refreshComponents)
				{
					if (component != null)
					{
						target.addComponent(component);
					}
				}
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > verificatieForm)
			{
				refreshFeedback(target);
			}

			@Override
			public String getLabel()
			{
				return "Uitvoeren";
			}

		};

		panel.addButton(submitLink);
		AjaxAnnulerenButton annuleren = new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		};
		panel.addButton(annuleren);
		super.fillBottomRow(panel);
	}

}