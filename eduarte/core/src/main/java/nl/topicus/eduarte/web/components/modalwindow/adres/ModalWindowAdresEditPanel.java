package nl.topicus.eduarte.web.components.modalwindow.adres;

import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.web.components.panels.adresedit.AdresEditPanel;

public class ModalWindowAdresEditPanel<T extends AdresEntiteit<T>> extends
		AbstractToevoegenBewerkenModalWindowPanel<T>
{
	private static final long serialVersionUID = 1L;

	public ModalWindowAdresEditPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow,
			ListAdressenEditPanel<T, ? > listAdressenEditPanel)
	{
		super(id, modalWindow, listAdressenEditPanel);
		getFormContainer().add(
			new AdresEditPanel<T>("adres", modalWindow.getModel(), AdresEditPanel.Mode.POPUP));
		createComponents();
	}
}
