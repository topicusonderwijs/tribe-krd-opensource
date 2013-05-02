package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;
import nl.topicus.eduarte.entities.signalering.settings.VerzuimTaakEventAbonnementConfiguration;

/**
 * 
 */
public class VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelZoekFilter
		extends
		AbstractOrganisatieEenheidLocatieZoekFilter<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel>
{
	private static final long serialVersionUID = 1L;

	private VerzuimTaakSignaalDefinitie signaalDefinitie;

	private VerzuimTaakEventAbonnementConfiguration abonnementConfiguration;

	public void setSignaalDefinitie(VerzuimTaakSignaalDefinitie signaalDefinitie)
	{
		this.signaalDefinitie = signaalDefinitie;
	}

	public VerzuimTaakSignaalDefinitie getSignaalDefinitie()
	{
		return signaalDefinitie;
	}

	public void setAbonnementConfiguration(VerzuimTaakEventAbonnementConfiguration abonnementConfiguration)
	{
		this.abonnementConfiguration = abonnementConfiguration;
	}

	public VerzuimTaakEventAbonnementConfiguration getAbonnementConfiguration()
	{
		return abonnementConfiguration;
	}

}
