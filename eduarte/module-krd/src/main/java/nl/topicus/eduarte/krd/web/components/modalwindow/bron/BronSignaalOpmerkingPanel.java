package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * Panel voor het toevoegen van een opmerking aan een signaal
 * 
 * @author vandekamop
 */
public class BronSignaalOpmerkingPanel extends ModalWindowBasePanel<IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	private Form<IBronSignaal> myForm;

	public BronSignaalOpmerkingPanel(String id, BronSignaalOpmerkingModalWindow modalWindow,
			IModel<IBronSignaal> signaalModel)
	{
		super(id, modalWindow);
		myForm = new Form<IBronSignaal>("form", signaalModel);
		myForm.add(new TextArea<String>("opmerking"));
		add(myForm);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton submitLink = new AjaxOpslaanButton(panel, myForm)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				IBronSignaal signaal = myForm.getModelObject();
				if (signaal instanceof BronVoSignaal)
				{
					BronVoSignaal sign = (BronVoSignaal) signaal;
					sign.saveOrUpdate();
					sign.commit();
				}
				if (signaal instanceof BronBveTerugkoppelRecord)
				{
					BronBveTerugkoppelRecord sign = (BronBveTerugkoppelRecord) signaal;
					sign.saveOrUpdate();
					sign.commit();
				}
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
