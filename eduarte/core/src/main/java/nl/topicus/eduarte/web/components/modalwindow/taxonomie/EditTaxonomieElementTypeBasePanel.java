package nl.topicus.eduarte.web.components.modalwindow.taxonomie;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxVerwijderButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * Pagina voor het bewerken van een taxonomie-elementtype in een modal window.
 * 
 * @author loite
 */
public class EditTaxonomieElementTypeBasePanel extends ModalWindowBasePanel<TaxonomieElementType>
{
	private static final long serialVersionUID = 1L;

	private final EditTaxonomieElementTypePanel editPanel;

	public EditTaxonomieElementTypeBasePanel(String id, TaxonomieElementType taxonomieElementType,
			CobraModalWindow<TaxonomieElementType> modalWindow)
	{
		super(id, modalWindow);
		add(editPanel = new EditTaxonomieElementTypePanel("panel", taxonomieElementType));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton opslaan = new AjaxOpslaanButton(panel, getForm())
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
		opslaan.setMakeDefault(true);
		panel.addButton(opslaan);
		AjaxVerwijderButton verwijderen =
			new AjaxVerwijderButton(panel, "Verwijderen",
				"Weet u zeker dat u het taxonomie-elementtype "
					+ getTaxonomieElementType().getNaam() + " wilt verwijderen?")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getTaxonomieElementType().isVerwijderbaar();
				}

				@Override
				protected void onClick(AjaxRequestTarget target)
				{
					getTaxonomieElementType().delete();
					getTaxonomieElementType().commit();
					getModalWindow().close(target);
				}

			};
		panel.addButton(verwijderen);
		AbstractAjaxLinkButton annuleren =
			new AbstractAjaxLinkButton(panel, "Annuleren", CobraKeyAction.ANNULEREN,
				ButtonAlignment.RIGHT)
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

	private Form<TaxonomieElementType> getForm()
	{
		return editPanel.getForm();
	}

	private TaxonomieElementType getTaxonomieElementType()
	{
		return editPanel.getTaxonomieElementType();
	}

}
