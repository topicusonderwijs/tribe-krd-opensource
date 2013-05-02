package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.jobs.OverschrijfActie;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class ResultaatstructuurKopieerSettings implements IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Kopiëren uit cohort", readOnly = true)
	private IModel<Cohort> bronCohort;

	@AutoForm(label = "Kopiëren naar cohort", editorClass = CohortCombobox.class, required = true)
	private IModel<Cohort> doelCohort;

	@AutoForm(label = "Kopiëer", required = true)
	private SchrijfSelectie kopieerActie;

	@AutoForm(label = "Bij bestaande resultaatstructuren", required = true)
	private OverschrijfActie actieBijBestaandeStructuur;

	@AutoForm(label = "Bij bestaande verwijzingen", required = true)
	private OverschrijfActie actieBijBestaandeVerwijzingen;

	private Type type;

	private String code;

	public ResultaatstructuurKopieerSettings(Cohort bronCohort, Cohort doelCohort)
	{
		setBronCohort(bronCohort);
		setDoelCohort(doelCohort);
		setKopieerActie(SchrijfSelectie.StructurenEnVerwijzingen);
		setActieBijBestaandeStructuur(OverschrijfActie.Overslaan);
		setActieBijBestaandeVerwijzingen(OverschrijfActie.Overslaan);
	}

	public Cohort getBronCohort()
	{
		return bronCohort.getObject();
	}

	public void setBronCohort(Cohort bronCohort)
	{
		this.bronCohort = ModelFactory.getModel(bronCohort);
	}

	public Cohort getDoelCohort()
	{
		return doelCohort.getObject();
	}

	public void setDoelCohort(Cohort doelCohort)
	{
		this.doelCohort = ModelFactory.getModel(doelCohort);
	}

	public SchrijfSelectie getKopieerActie()
	{
		return kopieerActie;
	}

	public void setKopieerActie(SchrijfSelectie kopieerActie)
	{
		this.kopieerActie = kopieerActie;
	}

	public OverschrijfActie getActieBijBestaandeStructuur()
	{
		return actieBijBestaandeStructuur;
	}

	public void setActieBijBestaandeStructuur(OverschrijfActie actieBijBestaandeStructuur)
	{
		this.actieBijBestaandeStructuur = actieBijBestaandeStructuur;
	}

	public OverschrijfActie getActieBijBestaandeVerwijzingen()
	{
		return actieBijBestaandeVerwijzingen;
	}

	public void setActieBijBestaandeVerwijzingen(OverschrijfActie actieBijBestaandeVerwijzingen)
	{
		this.actieBijBestaandeVerwijzingen = actieBijBestaandeVerwijzingen;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public boolean isStructurenKopieren()
	{
		return getKopieerActie() != SchrijfSelectie.Toetsverwijzingen;
	}

	public boolean isVerwijzingenKopieren()
	{
		return getKopieerActie() != SchrijfSelectie.Resultaatstructuren;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(doelCohort);
		ComponentUtil.detachQuietly(bronCohort);
	}
}
