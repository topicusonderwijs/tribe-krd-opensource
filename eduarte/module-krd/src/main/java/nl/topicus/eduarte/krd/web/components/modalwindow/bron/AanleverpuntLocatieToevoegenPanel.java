package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpuntLocatie;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.web.components.choice.LocatieCombobox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * Panel voor het selecteren van onderwijssoort(Beroepsonderwijs, Educatie, VAVO, VO)
 * 
 * @author vandekamop
 */
public class AanleverpuntLocatieToevoegenPanel extends ModalWindowBasePanel<Void>
{
	private static final long serialVersionUID = 1L;

	private Form<BronAanleverpuntLocatie> myForm;

	public AanleverpuntLocatieToevoegenPanel(String id,
			AanleverpuntLocatieToevoegenModalWindow modalWindow)
	{
		super(id, modalWindow);
		myForm = new Form<BronAanleverpuntLocatie>("form", getAanleverpuntLocatieModel());
		myForm.add(new BronAanleverpuntComboBox("aanleverpunt").setRequired(true));
		myForm.add(new LocatieCombobox("locatie").setRequired(true));
		add(myForm);
		createComponents();
	}

	private IModel<BronAanleverpuntLocatie> getAanleverpuntLocatieModel()
	{
		return ModelFactory.getCompoundChangeRecordingModel(new BronAanleverpuntLocatie(),
			new DefaultModelManager(BronAanleverpuntLocatie.class, BronAanleverpunt.class));
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
				BronAanleverpuntLocatie aanleverpuntLocatie = myForm.getModelObject();
				if (aanleverpuntLocatie.exist())
				{
					error("Aanleverpuntlocatie bestaat al");
					refreshFeedback(target);
					return;
				}
				aanleverpuntLocatie.save();
				aanleverpuntLocatie.commit();
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > form)
			{
				refreshFeedback(target);
			}

		};
		panel.addButton(submitLink);
		AjaxOpslaanButton opslaanEnToevoegen =
			new AjaxOpslaanButton(panel, myForm, "Opslaan en nieuwe toevoegen", CobraKeyAction.GEEN)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form< ? > form)
				{
					refreshFeedback(target);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
				{
					BronAanleverpuntLocatie aanleverpuntLocatie =
						(BronAanleverpuntLocatie) form.getModelObject();
					if (aanleverpuntLocatie.exist())
					{
						error("Aanleverpuntlocatie bestaat al");
						refreshFeedback(target);
						return;
					}
					aanleverpuntLocatie.save();
					aanleverpuntLocatie.commit();
					myForm.setModel(getAanleverpuntLocatieModel());
					info("Aanleverpuntlocatie opgeslagen");
					refreshFeedback(target);
				}
			};
		opslaanEnToevoegen.setMakeDefault(true);
		panel.addButton(opslaanEnToevoegen);
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
