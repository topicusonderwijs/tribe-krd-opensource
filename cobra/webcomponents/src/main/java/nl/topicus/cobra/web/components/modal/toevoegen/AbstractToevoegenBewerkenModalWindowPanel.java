package nl.topicus.cobra.web.components.modal.toevoegen;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenPanel.OpslaanButtonType;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

/**
 * Panel voor het toevoegen & bewerken van een Object in een modal window.
 * 
 * @author hoeve
 */
public abstract class AbstractToevoegenBewerkenModalWindowPanel<T extends IdObject> extends
		ModalWindowBasePanel<T>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer formContainer;

	private Form<T> form;

	private AbstractToevoegenBewerkenPanel<T> editPanel;

	public AbstractToevoegenBewerkenModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow,
			AbstractToevoegenBewerkenPanel<T> editPanel)
	{
		super(id, modalWindow);
		setDefaultModel(ModelFactory.getCompoundModelForModel(modalWindow.getModel()));
		this.editPanel = editPanel;

		form = new Form<T>("form");
		add(form);

		formContainer = new WebMarkupContainer("formContainer");
		formContainer.setOutputMarkupId(true);
		form.add(formContainer);
	}

	public WebMarkupContainer getFormContainer()
	{
		return formContainer;
	}

	public Form<T> getForm()
	{
		return form;
	}

	public AbstractToevoegenBewerkenPanel<T> getEditPanel()
	{
		return editPanel;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (getEditPanel().getOpslaanButtonType() != null)
		{

			if (getEditPanel().getOpslaanButtonType().contains(OpslaanButtonType.Opslaan))
			{
				// CobraKeyAction is geen, anders wordt er 'Ctrl+Return' als title
				// getoond, en
				// dat werkt niet in een modal window
				AjaxOpslaanButton submitLink =
					new AjaxOpslaanButton(panel, getForm(), "Opslaan", CobraKeyAction.GEEN)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
						{
							getModalWindow().setDeleteOnClose(false);
							getModalWindow().close(target);
						}

						@Override
						protected void onError(AjaxRequestTarget target, Form< ? > _form)
						{
							refreshFeedback(target);
						}

					};
				submitLink.setMakeDefault(getEditPanel().getOpslaanButtonType().contains(
					OpslaanButtonType.Opslaan));
				panel.addButton(submitLink);
			}
			if (getEditPanel().getOpslaanButtonType()
				.contains(OpslaanButtonType.OpslaanEnToevoegen))
			{
				AjaxOpslaanButton submitNextLink =
					new AjaxOpslaanButton(panel, getForm(), "Opslaan en nieuwe toevoegen",
						CobraKeyAction.GEEN)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
						{
							List<T> modelObjectList = getEditPanel().getModelObject();
							T modelObject = getEditPanel().createNewT();

							if (!modelObjectList.contains(modelObject))
								modelObjectList.add(modelObject);

							getModalWindow().setDefaultModelObject(modelObject);
							getModalWindow().setAddEntity(true);

							target.addComponent(getFormContainer());
						}

						@Override
						protected void onError(AjaxRequestTarget target, Form< ? > _form)
						{
							refreshFeedback(target);
						}

					};
				submitNextLink.setMakeDefault(getEditPanel().getOpslaanButtonType().equals(
					OpslaanButtonType.OpslaanEnToevoegen));
				panel.addButton(submitNextLink);
			}

			if (getEditPanel().getOpslaanButtonType().contains(OpslaanButtonType.OpslaanEnKopie))
			{
				AjaxOpslaanButton submitNextLink =
					new AjaxOpslaanButton(panel, getForm(), "Opslaan en kopie toevoegen",
						CobraKeyAction.GEEN)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
						{
							List<T> modelObjectList = getEditPanel().getModelObject();
							T modelObject = getEditPanel().createNewT();

							fillCopiedT(modelObject);

							if (!modelObjectList.contains(modelObject))
								modelObjectList.add(modelObject);

							getModalWindow().setDefaultModelObject(modelObject);
							getModalWindow().setAddEntity(true);

							target.addComponent(getFormContainer());
						}

						@Override
						protected void onError(AjaxRequestTarget target, Form< ? > _form)
						{
							refreshFeedback(target);
						}
					};
				submitNextLink.setMakeDefault(getEditPanel().getOpslaanButtonType().equals(
					OpslaanButtonType.OpslaanEnKopie));
				panel.addButton(submitNextLink);

			}
		}
		AjaxAnnulerenButton annuleren = new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().setDeleteOnClose(true);
				getModalWindow().close(target);
			}
		};
		annuleren.setAction(CobraKeyAction.GEEN);
		panel.addButton(annuleren);

	}

	/**
	 * functie welke subclasses kunnen overschrijven om een kopie van T in te vullen.
	 * Bedoelt voor de opslaan-en-kopie button, {@link OpslaanButtonType#OpslaanEnKopie}.
	 */
	@SuppressWarnings("unused")
	public void fillCopiedT(T modelObject)
	{

	}

	@Override
	public AbstractToevoegenBewerkenModalWindow<T> getModalWindow()
	{
		return (AbstractToevoegenBewerkenModalWindow<T>) super.getModalWindow();
	}
}
