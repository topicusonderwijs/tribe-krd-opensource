package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven.BPVCodeHerkomst;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.eduarte.web.components.choice.dropdownchecklist.SoortExterneOrganisatieDropDownCheckList;
import nl.topicus.eduarte.web.components.panels.filter.renderer.ZoekFilterMultiSelectEditorContainer;

import org.apache.wicket.model.IModel;

/**
 * @author hop
 */
public class BPVBedrijfsgegevenZoekFilter extends AbstractZoekFilter<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String verkorteNaam;

	private String plaats;

	@AutoForm(editorClass = SoortExterneOrganisatieDropDownCheckList.class, editorContainer = ZoekFilterMultiSelectEditorContainer.TYPE, label = "Soort(en)")
	private IModel<List<SoortExterneOrganisatie>> soortExterneOrganisaties;

	private String codeLeerbedrijf;

	private String relatienummer;

	@AutoForm(editorClass = KenniscentrumCombobox.class)
	private IModel<Brin> kenniscentrum;

	private IModel<BPVCodeHerkomst> herkomstCode;

	public BPVBedrijfsgegevenZoekFilter()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public List<SoortExterneOrganisatie> getSoortExterneOrganisaties()
	{
		return getModelObject(soortExterneOrganisaties);
	}

	public void setSoortExterneOrganisaties(List<SoortExterneOrganisatie> soortExterneOrganisaties)
	{
		this.soortExterneOrganisaties = makeModelFor(soortExterneOrganisaties);
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

	public void setKenniscentrum(Brin kenniscentrum)
	{
		this.kenniscentrum = makeModelFor(kenniscentrum);
	}

	public Brin getKenniscentrum()
	{
		return getModelObject(kenniscentrum);
	}

	public BPVCodeHerkomst getHerkomstCode()
	{
		return getModelObject(herkomstCode);
	}

	public void setHerkomstCode(BPVCodeHerkomst herkomstCode)
	{
		this.herkomstCode = makeModelFor(herkomstCode);
	}

}
