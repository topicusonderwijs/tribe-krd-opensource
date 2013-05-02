package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensInzien;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

@InPrincipal(DeelnemerExamensInzien.class)
class ExamenstatusOvergangBasePanel extends ModalWindowBasePanel<Void>
{
	private static final long serialVersionUID = 1L;

	private final ExamenstatusOvergangPanel statusovergangPanel;

	ExamenstatusOvergangBasePanel(String id, ExamenstatusOvergangModalWindow window,
			Examendeelname examendeelname, ToegestaneExamenstatusOvergang toegestaneOvergang,
			ModelManager modelManager)
	{
		super(id, window);
		statusovergangPanel =
			new ExamenstatusOvergangPanel("panel", examendeelname, toegestaneOvergang, modelManager);
		add(statusovergangPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton submitLink = new AjaxOpslaanButton(panel, statusovergangPanel.getForm())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
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
