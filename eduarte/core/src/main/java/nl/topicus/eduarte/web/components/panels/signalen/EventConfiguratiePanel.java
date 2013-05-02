package nl.topicus.eduarte.web.components.panels.signalen;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

public class EventConfiguratiePanel extends
		ModalWindowBasePanel<AbstractEventAbonnementConfiguration< ? >>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	public EventConfiguratiePanel(String id, EventConfiguratieModalWindow modalWindow)
	{
		super(id, modalWindow);
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<AbstractEventAbonnementConfiguration< ? >> fieldSet =
			new AutoFieldSet<AbstractEventAbonnementConfiguration< ? >>("fields", modalWindow
				.getModel());
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton opslaanButton = new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > aform)
			{
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > aform)
			{
				refreshFeedback(target);
			}
		};
		opslaanButton.setMakeDefault(true);
		panel.addButton(opslaanButton);

		AjaxAnnulerenButton annuleren = new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				((EventConfiguratieModalWindow) getModalWindow()).revertChanges();
				getModalWindow().close(target);
			}
		};
		panel.addButton(annuleren);
	}
}
