package nl.topicus.eduarte.modules;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.AbstractCobraModule;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;

public class ExactCsvExportModule extends AbstractCobraModule
{
	public ExactCsvExportModule()
	{
		super(EduArteModuleKey.EXACT_CSV_EXPORT);
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
		if (EduArteContext.get().getOrganisatie() != null
			&& EduArteContext.get().getOrganisatie().getRechtenSoort().equals(
				RechtenSoort.INSTELLING))
		{
			OrganisatieDataAccessHelper helper =
				DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
			return helper.isModuleAfgenomen((EduArteModuleKey) getKey(), encryptionProvider);
		}
		return false;
	}

	@Override
	protected void registerMenuExtenders()
	{
		super.registerMenuExtenders();
		addMenuExtender(DeelnemerCollectiefMenu.class, new ExactCsvExportMenuExtender());
	}
}
