package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieAdresDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class BestaandAdresValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieModel externeOrganisatieModel;

	/**
	 * De getVolledigAdres van het adres dat bij de volgende submit als valide moet worden
	 * gezien (alsnog opslaan ook al bestaat het adres al). Hier wordt een string gebruikt
	 * omdat daar voldoende informatie in zit en je dan geen gedoe hebt met detachen ed.
	 */
	private String ignoreAdres;

	public BestaandAdresValidator(ExterneOrganisatieModel externeOrganisatieModel)
	{
		this.externeOrganisatieModel = externeOrganisatieModel;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return null;
	}

	@Override
	public void validate(Form< ? > form)
	{
		ExterneOrganisatie organisatie = externeOrganisatieModel.getExterneOrganisatie();
		ExterneOrganisatieAdres extOrgAdres = organisatie.getFysiekAdres();
		if (extOrgAdres != null && extOrgAdres.getAdres() != null)
		{
			if (extOrgAdres.getAdres().getVolledigAdres().equals(ignoreAdres))
				return;

			if (extOrgAdres.getAdres().getVolledigAdres().equals(""))
				return;

			List<ExterneOrganisatie> bestaandeOrganisaties =
				DataAccessRegistry.getHelper(ExterneOrganisatieAdresDataAccessHelper.class)
					.getBestaandeExtOrganisatiesOpZelfdeAdres(organisatie, extOrgAdres);
			if (!bestaandeOrganisaties.isEmpty())
			{
				List<String> namen = new ArrayList<String>();
				for (ExterneOrganisatie curOrg : bestaandeOrganisaties)
				{
					namen.add(curOrg.getNaam());
				}

				ignoreAdres = extOrgAdres.getAdres().getVolledigAdres();
				form.error("Er bestaan al externe organisaties op dit adres: "
					+ StringUtil.toString(namen, ", ", "") + ". "
					+ "Klik nogmaals op opslaan om alsnog op te slaan.");
			}
		}
	}
}
