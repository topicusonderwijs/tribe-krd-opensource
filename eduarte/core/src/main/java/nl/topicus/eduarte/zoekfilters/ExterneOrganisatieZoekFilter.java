package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie.ControleResultaat;
import nl.topicus.eduarte.entities.settings.GebruikLandelijkeExterneOrganisatiesSetting;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.eduarte.web.components.choice.dropdownchecklist.SoortExterneOrganisatieDropDownCheckList;
import nl.topicus.eduarte.web.components.panels.filter.renderer.ZoekFilterMultiSelectEditorContainer;
import nl.topicus.eduarte.web.components.quicksearch.plaats.PlaatsSearchEditor;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class ExterneOrganisatieZoekFilter extends AbstractZoekFilter<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String verkorteNaam;

	@AutoForm(editorClass = PlaatsSearchEditor.class)
	private String plaats;

	private String adres;

	private Boolean bpvBedrijf;

	@AutoForm(editorClass = SoortExterneOrganisatieDropDownCheckList.class, editorContainer = ZoekFilterMultiSelectEditorContainer.TYPE, label = "Soort(en)")
	private IModel<List<SoortExterneOrganisatie>> soortExterneOrganisaties;

	private String codeLeerbedrijf;

	@AutoForm(label = "Relatienr.", htmlClasses = "unit_60")
	private String relatienummer;

	private final boolean gebruikLandelijkeExterneOrganisaties;

	private Boolean hasDebiteurennummer;

	@AutoForm(editorClass = KenniscentrumCombobox.class)
	private IModel<Brin> kenniscentrum;

	private Boolean bijVooropleiding;

	private Boolean nogControleren;

	private ControleResultaat controleResultaat;

	public static final boolean getGebruikLandelijkeSetting()
	{
		GebruikLandelijkeExterneOrganisatiesSetting setting =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				GebruikLandelijkeExterneOrganisatiesSetting.class);
		return setting == null ? true : setting.getValue() == null ? true : setting.getValue()
			.booleanValue();
	}

	public ExterneOrganisatieZoekFilter()
	{
		this.gebruikLandelijkeExterneOrganisaties = getGebruikLandelijkeSetting();
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return Returns the plaats.
	 */
	public String getPlaats()
	{
		return plaats;
	}

	/**
	 * @param plaats
	 *            The plaats to set.
	 */
	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	/**
	 * @return Returns the soortExterneOrganisaties.
	 */
	public List<SoortExterneOrganisatie> getSoortExterneOrganisaties()
	{
		return getModelObject(soortExterneOrganisaties);
	}

	/**
	 * @param soortExterneOrganisaties
	 *            The soortExterneOrganisaties to set.
	 */
	public void setSoortExterneOrganisaties(List<SoortExterneOrganisatie> soortExterneOrganisaties)
	{
		this.soortExterneOrganisaties = makeModelFor(soortExterneOrganisaties);
	}

	public void setBpvBedrijf(Boolean bpvBedrijf)
	{
		this.bpvBedrijf = bpvBedrijf;
	}

	public Boolean getBpvBedrijf()
	{
		return bpvBedrijf;
	}

	public void setCodeLeerbedrijf(String codeLeerbedrijf)
	{
		this.codeLeerbedrijf = codeLeerbedrijf;
	}

	public String getCodeLeerbedrijf()
	{
		return codeLeerbedrijf;
	}

	public void setRelatienummer(String relatienummer)
	{
		this.relatienummer = relatienummer;
	}

	public String getRelatienummer()
	{
		return relatienummer;
	}

	public String getVerkorteNaam()
	{
		return verkorteNaam;
	}

	public void setVerkorteNaam(String verkorteNaam)
	{
		this.verkorteNaam = verkorteNaam;
	}

	public boolean isGebruikLandelijkeExterneOrganisaties()
	{
		return gebruikLandelijkeExterneOrganisaties;
	}

	public void setHasDebiteurennummer(Boolean hasDebiteurennummer)
	{
		this.hasDebiteurennummer = hasDebiteurennummer;
	}

	public Boolean getHasDebiteurennummer()
	{
		return hasDebiteurennummer;
	}

	public Brin getKenniscentrum()
	{
		if (kenniscentrum == null)
			kenniscentrum = ModelFactory.getModel(null);
		return kenniscentrum.getObject();
	}

	public void setKenniscentrum(Brin kenniscentrum)
	{
		this.kenniscentrum = makeModelFor(kenniscentrum);
	}

	public Boolean isBijVooropleiding()
	{
		return bijVooropleiding;
	}

	public void setBijVooropleiding(Boolean bijVooropleiding)
	{
		this.bijVooropleiding = bijVooropleiding;
	}

	public void setAdres(String adres)
	{
		this.adres = adres;
	}

	public String getAdres()
	{
		return adres;
	}

	public Boolean getNogControleren()
	{
		return nogControleren;
	}

	public void setNogControleren(Boolean nogControleren)
	{
		this.nogControleren = nogControleren;
	}

	public ControleResultaat getControleResultaat()
	{
		return controleResultaat;
	}

	public void setControleResultaat(ControleResultaat controleResultaat)
	{
		this.controleResultaat = controleResultaat;
	}

}