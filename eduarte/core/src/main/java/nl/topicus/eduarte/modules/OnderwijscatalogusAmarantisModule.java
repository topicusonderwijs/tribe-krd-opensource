package nl.topicus.eduarte.modules;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.AbstractCobraModule;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.HomeMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenu;
import nl.topicus.eduarte.web.components.menu.OpleidingMenu;

public class OnderwijscatalogusAmarantisModule extends AbstractCobraModule
{
	/**
	 * 
	 * Rapportagequery voor alle opleidingen:
	 * 
	 * select opleiding.code as opleidingcode, opleiding.naam as opleidingnaam,
	 * cohort.naam, co.leerjaar, co.periode, co.onderwijstijd, product.code as
	 * productcode, product.titel as productnaam, organisatieeenheid.afkorting as
	 * orgEhdAfkorting, organisatieeenheid.naam as orgEhdNaam from
	 * curriculumonderwijsproduct co inner join curriculum cur on co.curriculum=cur.id
	 * inner join opleiding on cur.opleiding=opleiding.id inner join onderwijsproduct
	 * product on co.onderwijsproduct=product.id inner join cohort on cur.cohort=cohort.id
	 * inner join organisatieeenheid on cur.organisatieEenheid=organisatieeenheid.id order
	 * by opleidingcode, cohort.naam, leerjaar, periode ;
	 */

	public OnderwijscatalogusAmarantisModule()
	{
		super(EduArteModuleKey.ONDERWIJSCATALOGUS_AMARANTIS);
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

			return EduArteApp.get().isModuleActive(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
				&& EduArteApp.get().isModuleActive(EduArteModuleKey.ONDERWIJSCATALOGUS)
				&& helper.isModuleAfgenomen((EduArteModuleKey) getKey(), encryptionProvider);
		}
		return false;
	}

	@Override
	protected void registerMenuExtenders()
	{
		super.registerMenuExtenders();
		addMenuExtender(OnderwijsproductMenu.class,
			new OnderwijscatalogusAmarantisOnderwijsproductMenuExtender());
		addMenuExtender(OpleidingMenu.class, new OnderwijscatalogusAmarantisOpleidingMenuExtender());
		addMenuExtender(BeheerMenu.class, new OnderwijscatalogusAmarantisBeheerMenuExtender());
		addMenuExtender(HomeMenu.class, new OnderwijscatalogusAmarantisHomeMenuExtender());
		addMenuExtender(OnderwijsCollectiefMenu.class,
			new OnderwijscatalogusAmarantisOnderwijsCollectiefMenuExtender());
	}
}
