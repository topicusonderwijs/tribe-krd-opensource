package nl.topicus.eduarte.krd.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

/**
 * Laat de gebruiker een {@link Intensiteit} selecteren als de verbintenis een BO
 * verbintenis is (CGO of MBO opleiding). Bij VO kan de intensiteit Examendeelnemer worden
 * gekozen om niet-bekostigde deelnemers op te voeren.
 * 
 * @author dashorst
 */
public class IntensiteitCombobox extends EnumCombobox<Intensiteit>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisProvider provider;

	public IntensiteitCombobox(String id, boolean ajaxUpdate, VerbintenisProvider provider)
	{
		super(id, ajaxUpdate, Intensiteit.values());
		this.provider = provider;
	}

	@Override
	public boolean isRequired()
	{
		Verbintenis verbintenis = provider.getVerbintenis();
		if (verbintenis == null)
			return false;
		return verbintenis.getStatus().isBronCommuniceerbaar();
	}

	@Override
	public boolean isVisible()
	{
		Verbintenis verbintenis = provider.getVerbintenis();
		if (verbintenis == null)
			return true;
		return verbintenis.isBOVerbintenis() || verbintenis.isVOVerbintenis();
	}
}
