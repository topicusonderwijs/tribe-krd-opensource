package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;

import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class ResultaatstructuurZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Resultaatstructuur> implements
		ICodeNaamActiefZoekFilter<Resultaatstructuur>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_120")
	private IModel<Onderwijsproduct> onderwijsproduct;

	private String taxonomiecode;

	@AutoForm(required = true, htmlClasses = "unit_100")
	private IModel<Cohort> cohort;

	private IModel<List<Deelnemer>> deelnemers;

	private IModel<Verbintenis> contextVerbintenis;

	@AutoForm(label = "Gekoppeld aan verbintenis")
	private boolean alleenGekoppeldAanVerbintenis;

	private boolean alleenActieveVerbintenissen;

	private Type type;

	private IModel<Medewerker> medewerker;

	@AutoForm(label = "status", editorClass = ActiefCombobox.class)
	private Boolean actief;

	private Boolean verwijsbareToets;

	@AutoForm(htmlClasses = "unit_160")
	private String code;

	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	private IModel<ResultaatstructuurCategorie> categorie;

	private IModel<Medewerker> auteur;

	private IModel<Groep> groepDirect;

	public ResultaatstructuurZoekFilter()
	{
		this(null, EduArteContext.get().getMedewerker());
	}

	public ResultaatstructuurZoekFilter(Onderwijsproduct onderwijsproduct)
	{
		this(onderwijsproduct, EduArteContext.get().getMedewerker());
	}

	public ResultaatstructuurZoekFilter(Onderwijsproduct onderwijsproduct, Medewerker medewerker)
	{
		setOnderwijsproduct(onderwijsproduct);
		setMedewerker(medewerker);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public void setCohort(Cohort cohort)
	{
		if (this.cohort != null)
			this.cohort.setObject(cohort);
		else
			this.cohort = makeModelFor(cohort);
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohortModel(IModel<Cohort> cohortModel)
	{
		this.cohort = cohortModel;
	}

	public List<Deelnemer> getDeelnemers()
	{
		return getModelObject(deelnemers);
	}

	public void setDeelnemers(List<Deelnemer> deelnemers)
	{
		this.deelnemers = makeModelFor(deelnemers);
	}

	public Verbintenis getContextVerbintenis()
	{
		return getModelObject(contextVerbintenis);
	}

	public void setContextVerbintenis(Verbintenis contextVerbintenis)
	{
		this.contextVerbintenis = makeModelFor(contextVerbintenis);
	}

	public void setContextVerbintenis(IModel<Verbintenis> sorteerVerbintenis)
	{
		this.contextVerbintenis = sorteerVerbintenis;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
	}

	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	public void setAlleenGekoppeldAanVerbintenis(boolean alleenGekoppeldAanVerbintenis)
	{
		this.alleenGekoppeldAanVerbintenis = alleenGekoppeldAanVerbintenis;
	}

	public boolean isAlleenGekoppeldAanVerbintenis()
	{
		return alleenGekoppeldAanVerbintenis;
	}

	public void setAlleenActieveVerbintenissen(boolean alleenActieveVerbintenissen)
	{
		this.alleenActieveVerbintenissen = alleenActieveVerbintenissen;
	}

	public boolean isAlleenActieveVerbintenissen()
	{
		return alleenActieveVerbintenissen;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public boolean isOokVanAnderen()
	{
		return new DataSecurityCheck(Resultaatstructuur.ANDERMANS_STRUCTUREN)
			.isActionAuthorized(EduArteApp.get().getActionFactory().getAction(Render.class));
	}

	@Override
	public Boolean getActief()
	{
		return actief;
	}

	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	@Override
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public ResultaatstructuurCategorie getCategorie()
	{
		return getModelObject(categorie);
	}

	public void setCategorie(ResultaatstructuurCategorie categorie)
	{
		this.categorie = makeModelFor(categorie);
	}

	public Medewerker getAuteur()
	{
		return getModelObject(auteur);
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = makeModelFor(auteur);
	}

	public Groep getGroepDirect()
	{
		return getModelObject(groepDirect);
	}

	public void setGroepDirect(Groep groepDirect)
	{
		this.groepDirect = makeModelFor(groepDirect);
	}

	@Override
	public Class<Resultaatstructuur> getEntityClass()
	{
		return Resultaatstructuur.class;
	}

	public Boolean getVerwijsbareToets()
	{
		return verwijsbareToets;
	}

	public void setVerwijsbareToets(Boolean verwijsbareToets)
	{
		this.verwijsbareToets = verwijsbareToets;
	}
}
