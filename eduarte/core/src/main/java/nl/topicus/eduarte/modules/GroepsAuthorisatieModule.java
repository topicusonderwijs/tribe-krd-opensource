package nl.topicus.eduarte.modules;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.CobraModule;
import nl.topicus.cobra.modules.IModuleKey;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;

public class GroepsAuthorisatieModule implements CobraModule
{
	private final EncryptionProvider encryptionProvider = new CobraEncryptonProvider();

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	@Override
	public boolean isModuleActive(IOrganisatie instelling)
	{
		if (EduArteContext.get().getOrganisatie().getRechtenSoort().equals(RechtenSoort.INSTELLING))
		{
			OrganisatieDataAccessHelper helper =
				DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
			return helper.isModuleAfgenomen((EduArteModuleKey) getKey(), encryptionProvider);
		}
		return true;
	}

	@Override
	public IModuleKey getKey()
	{
		return EduArteModuleKey.GROEPS_AUTHORISATIE;
	}

	@Override
	public String getName()
	{
		return EduArteModuleKey.GROEPS_AUTHORISATIE.getName();
	}
}
