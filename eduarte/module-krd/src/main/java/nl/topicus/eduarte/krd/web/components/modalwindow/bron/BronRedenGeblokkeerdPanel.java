package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class BronRedenGeblokkeerdPanel extends ModalWindowBasePanel<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	private Form<IBronMelding> redenForm;

	public BronRedenGeblokkeerdPanel(String id, BronRedenGeblokkeerdModalWindow modalWindow,
			IModel<IBronMelding> meldingModel)
	{
		super(id, modalWindow);
		redenForm = new Form<IBronMelding>("redenForm", meldingModel);
		redenForm.add(new TextArea<String>("redenGeblokkeerd", new PropertyModel<String>(
			meldingModel, "redenGeblokkeerd")));
		add(redenForm);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton submitLink = new AjaxOpslaanButton(panel, redenForm, "Blokkeren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				IBronMelding melding = redenForm.getModelObject();
				melding.setGeblokkeerd(true);

				((InstellingEntiteit) melding).saveOrUpdate();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

				((BronRedenGeblokkeerdModalWindow) getModalWindow()).setSaved(true);
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > form)
			{
				refreshFeedback(target);
			}
		};
		submitLink.setMakeDefault(true);
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
	}
}
