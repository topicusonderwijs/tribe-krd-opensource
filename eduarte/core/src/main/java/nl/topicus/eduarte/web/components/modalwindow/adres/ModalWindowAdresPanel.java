package nl.topicus.eduarte.web.components.modalwindow.adres;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.web.components.panels.adres.AdresPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ModalWindowAdresPanel<T extends AdresEntiteit<T>> extends ModalWindowBasePanel<T>
{
	private static final long serialVersionUID = 1L;

	public ModalWindowAdresPanel(String id, CobraModalWindow<T> modalWindow)
	{
		super(id, modalWindow);
		add(new AdresPanel<T>("adres", modalWindow.getModel()));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxAnnulerenButton(panel, "Sluiten")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}
}
