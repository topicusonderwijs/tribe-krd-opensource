package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class KopieerSettings implements IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(include = false)
	private IModel<Cohort> bronCohortModel;

	@AutoForm(include = false)
	private IModel<Cohort> doelCohortModel;

	@AutoForm(include = false)
	private boolean kopieerOnderwijsproducten = true;

	public KopieerSettings(Cohort cohort)
	{
		setBronCohort(cohort);
		setDoelCohort(cohort.getVolgende());
	}

	@AutoForm(include = true, label = "Kopiëren uit cohort", readOnly = true)
	public Cohort getBronCohort()
	{
		return bronCohortModel.getObject();
	}

	public void setBronCohort(Cohort bronCohort)
	{
		this.bronCohortModel = ModelFactory.getModel(bronCohort);
	}

	@AutoForm(include = true, label = "Kopiëren naar cohort", editorClass = CohortCombobox.class)
	public Cohort getDoelCohort()
	{
		return doelCohortModel.getObject();
	}

	public void setDoelCohort(Cohort doelCohort)
	{
		this.doelCohortModel = ModelFactory.getModel(doelCohort);
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(doelCohortModel);
		ComponentUtil.detachQuietly(bronCohortModel);
	}

	public void setKopieerOnderwijsproducten(boolean kopieerOnderwijsproducten)
	{
		this.kopieerOnderwijsproducten = kopieerOnderwijsproducten;
	}

	public boolean isKopieerOnderwijsproducten()
	{
		return kopieerOnderwijsproducten;
	}
}
