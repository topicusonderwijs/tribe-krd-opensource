package nl.topicus.eduarte.modules;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.AbstractCobraModule;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;

/**
 * Module waarmee aangegeven kan worden of een instelling Vasco tokens gebruikt voor het
 * authenticeren van gebruikers.
 */
public class VascoTokenAuthenticatieModule extends AbstractCobraModule
{
	public VascoTokenAuthenticatieModule()
	{
		super(EduArteModuleKey.VASCO_TOKENS);
	}

	private final EncryptionProvider encryptionProvider = new CobraEncryptonProvider();

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	@Override
	public boolean isModuleActive(IOrganisatie instelling)
	{
		if (instelling instanceof Organisatie)
		{
			Organisatie organisatie = (Organisatie) instelling;
			if (organisatie.getRechtenSoort() == RechtenSoort.INSTELLING)
			{
				OrganisatieDataAccessHelper helper =
					DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);

				boolean afgenomen =
					helper.isModuleAfgenomen((EduArteModuleKey) getKey(), encryptionProvider);
				return afgenomen;
			}
		}
		return false;
	}

	@Override
	protected void registerMenuExtenders()
	{
		super.registerMenuExtenders();
		addMenuExtender(BeheerMenu.class, new VascoBeheerMenuExtender());
	}
}
