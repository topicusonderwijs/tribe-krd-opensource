package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.web.components.quicksearch.taxonomie.TaxonomieElementSearchEditor;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class OpleidingAanbodZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<OpleidingAanbod>
{
	private static final long serialVersionUID = 1L;

	private OpleidingZoekFilter opleidingZoekfilter;

	private IModel<Team> team;

	@AutoForm(editorClass = TaxonomieElementSearchEditor.class, htmlClasses = "unit_160")
	private IModel<Verbintenisgebied> verbintenisgebied;

	public OpleidingAanbodZoekFilter()
	{
		setOpleidingZoekfilter(new OpleidingZoekFilter());
	}

	public Team getTeam()
	{
		return getModelObject(team);
	}

	public void setTeam(Team team)
	{
		this.team = makeModelFor(team);
	}

	public OpleidingZoekFilter getOpleidingZoekfilter()
	{
		return opleidingZoekfilter;
	}

	public void setOpleidingZoekfilter(OpleidingZoekFilter opleidingZoekfilter)
	{
		this.opleidingZoekfilter = opleidingZoekfilter;
	}

	public String getOpleidingNaam()
	{
		return opleidingZoekfilter.getNaam();
	}

	public void setOpleidingNaam(String naam)
	{
		this.opleidingZoekfilter.setNaam(naam);
	}

	@AutoForm(htmlClasses = "unit_60")
	public String getCode()
	{
		return opleidingZoekfilter.getCode();
	}

	public void setCode(String code)
	{
		this.opleidingZoekfilter.setCode(code);
	}

	@AutoForm(htmlClasses = "unit_80")
	public MBOLeerweg getLeerweg()
	{
		return opleidingZoekfilter.getLeerweg();
	}

	public void setLeerweg(MBOLeerweg leerweg)
	{
		this.opleidingZoekfilter.setLeerweg(leerweg);
	}

	public String getTaxonomiecode()
	{
		return opleidingZoekfilter.getTaxonomiecode();
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.opleidingZoekfilter.setTaxonomiecode(taxonomiecode);
	}

	@AutoForm(htmlClasses = "unit_80")
	public Cohort getCohort()
	{
		return opleidingZoekfilter.getCohort();
	}

	public void setCohort(Cohort cohort)
	{
		this.opleidingZoekfilter.setCohort(cohort);
	}

	@AutoForm(editorClass = TaxonomieElementSearchEditor.class, htmlClasses = "unit_160")
	public Verbintenisgebied getVerbintenisgebied()
	{
		return getModelObject(verbintenisgebied);
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = makeModelFor(verbintenisgebied);
	}

	public void setVariantVan(Opleiding parent)
	{
		opleidingZoekfilter.setVariantVan(parent);
	}

	public Opleiding getVariantVan()
	{
		return opleidingZoekfilter.getVariantVan();
	}
}
