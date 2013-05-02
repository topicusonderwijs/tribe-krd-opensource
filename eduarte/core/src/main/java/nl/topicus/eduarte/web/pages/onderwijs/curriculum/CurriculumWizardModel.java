package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.CurriculumDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.model.IModel;

public class CurriculumWizardModel implements IModel<CurriculumWizardModel>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = true, htmlClasses = "unit_max")
	private IModel<Opleiding> opleiding;

	@AutoForm(required = true)
	private IModel<Cohort> cohort;

	@AutoForm(required = true, htmlClasses = "unit_max")
	private IModel<OrganisatieEenheid> organisatieEenheid;

	@AutoForm(htmlClasses = "unit_max")
	private IModel<Locatie> locatie;

	private IChangeRecordingModel<Curriculum> curriculum;

	private IChangeRecordingModel<Onderwijsproduct> onderwijsproduct;

	private IModel<List<CurriculumOnderwijsproduct>> curriculumOnderwijsproducten;

	private DefaultModelManager modelManager =
		new DefaultModelManager(SoortOnderwijsproduct.class, CurriculumOnderwijsproduct.class,
			Onderwijsproduct.class, Curriculum.class);

	public CurriculumWizardModel()
	{
		/**
		 * Lijstje van CurriculumOnderwijsproducten zelf bijhouden, anders worden ook
		 * CurriculumOnderwijsproducten met een ander Onderwijsproduct getoond.
		 */
		List<CurriculumOnderwijsproduct> bla = new ArrayList<CurriculumOnderwijsproduct>();
		curriculumOnderwijsproducten = ModelFactory.getModel(bla, getModelManager());
	}

	public Opleiding getOpleiding()
	{
		if (opleiding == null)
			opleiding = ModelFactory.getModel(null);
		return opleiding.getObject();
	}

	public Cohort getCohort()
	{
		if (cohort == null)
			cohort = ModelFactory.getModel(null);
		return cohort.getObject();
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		if (organisatieEenheid == null)
			organisatieEenheid = ModelFactory.getModel(null);
		return organisatieEenheid.getObject();
	}

	public Locatie getLocatie()
	{
		if (locatie == null)
			return null;
		return locatie.getObject();
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = ModelFactory.getModel(locatie);
	}

	public Curriculum getCurriculum()
	{
		if (curriculum == null)
			return null;
		return curriculum.getObject();
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		if (onderwijsproduct == null)
			return null;
		return onderwijsproduct.getObject();
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = ModelFactory.getModel(opleiding);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = ModelFactory.getModel(cohort);
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = ModelFactory.getModel(organisatieEenheid);
	}

	public void setCurriculum(Curriculum curriculum)
	{
		this.curriculum =
			ModelFactory.getCompoundChangeRecordingModel(curriculum, getModelManager());
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct =
			ModelFactory.getCompoundChangeRecordingModel(onderwijsproduct, getModelManager());
	}

	public void setCurriculumOnderwijsproducten(
			List<CurriculumOnderwijsproduct> curriculumOnderwijsproducten)
	{
		this.curriculumOnderwijsproducten =
			ModelFactory.getListModel(curriculumOnderwijsproducten, getModelManager());
	}

	public List<CurriculumOnderwijsproduct> getCurriculumOnderwijsproducten()
	{
		return curriculumOnderwijsproducten.getObject();
	}

	public ModelManager getModelManager()
	{
		return modelManager;
	}

	public void setModelManager(DefaultModelManager modelManager)
	{
		this.modelManager = modelManager;
	}

	public void createOnderwijsproduct()
	{
		if (getOnderwijsproduct() == null)
		{
			Onderwijsproduct nieuwOnderwijsproduct = new Onderwijsproduct();
			nieuwOnderwijsproduct.setHeeftWerkstuktitel(false);
			nieuwOnderwijsproduct.setAlleenExtern(false);
			nieuwOnderwijsproduct.setBijIntake(false);
			nieuwOnderwijsproduct.setStartonderwijsproduct(false);
			nieuwOnderwijsproduct.setStatus(OnderwijsproductStatus.InOntwikkeling);
			setDefaultAggregatieNiveau(nieuwOnderwijsproduct);

			setOnderwijsproduct(nieuwOnderwijsproduct);
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultAggregatieNiveau(Onderwijsproduct nieuwOnderwijsproduct)
	{
		Aggregatieniveau leereenheidNiveau =
			(Aggregatieniveau) DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).get("LE",
				Aggregatieniveau.class);

		if (leereenheidNiveau != null)
			nieuwOnderwijsproduct.setAggregatieniveau(leereenheidNiveau);
	}

	private void createOnderwijsproductafname()
	{
		OnderwijsproductAanbod aanbod = new OnderwijsproductAanbod();
		aanbod.setOnderwijsproduct(getOnderwijsproduct());
		aanbod.setOrganisatieEenheid(getOrganisatieEenheid());
		aanbod.setLocatie(getLocatie());
		aanbod.save();
	}

	public void createCurriculum()
	{
		if (getCurriculum() == null)
		{
			Curriculum geselecteerdeCurriculum = getBestaandCurriculum();

			if (geselecteerdeCurriculum == null)
				geselecteerdeCurriculum = createNieuwCurriculum();

			setCurriculum(geselecteerdeCurriculum);
			voegNieuwCurriculumOnderwijsproductToe(true);
		}
	}

	private Curriculum getBestaandCurriculum()
	{
		return DataAccessRegistry.getHelper(CurriculumDataAccessHelper.class).get(
			getOrganisatieEenheid(), getLocatie(), getOpleiding(), getCohort());
	}

	private Curriculum createNieuwCurriculum()
	{
		Curriculum nieuwCurriculum = new Curriculum();
		nieuwCurriculum.setCohort(getCohort());
		nieuwCurriculum.setOrganisatieEenheid(getOrganisatieEenheid());
		nieuwCurriculum.setLocatie(getLocatie());
		nieuwCurriculum.setOpleiding(getOpleiding());

		return nieuwCurriculum;
	}

	public void voegNieuwCurriculumOnderwijsproductToe()
	{
		voegNieuwCurriculumOnderwijsproductToe(false);
	}

	/**
	 * Voegt een CurriculumOnderwijsproduct toe aan het huidige curriculum;
	 */
	public void voegNieuwCurriculumOnderwijsproductToe(boolean vulin)
	{
		CurriculumOnderwijsproduct curOnderwijsproduct =
			new CurriculumOnderwijsproduct(getCurriculum());
		curOnderwijsproduct.setOnderwijsproduct(getOnderwijsproduct());
		if (vulin)
		{
			curOnderwijsproduct.setPeriode(1);
			curOnderwijsproduct.setLeerjaar(1);
			curOnderwijsproduct.setOnderwijstijd(new BigDecimal(0));
		}

		getCurriculumOnderwijsproducten().add(curOnderwijsproduct);
	}

	/**
	 * Voegt een kopie van het meegeven CurriculumOnderwijsproduct toe aan het huidige
	 * curriculum;
	 */
	public void voegNieuwCurriculumOnderwijsproductToe(CurriculumOnderwijsproduct kopie)
	{
		CurriculumOnderwijsproduct curOnderwijsproduct =
			new CurriculumOnderwijsproduct(getCurriculum());
		curOnderwijsproduct.setOnderwijsproduct(getOnderwijsproduct());
		curOnderwijsproduct.setPeriode(kopie.getPeriode());
		curOnderwijsproduct.setLeerjaar(kopie.getLeerjaar());
		curOnderwijsproduct.setOnderwijstijd(kopie.getOnderwijstijd());

		getCurriculumOnderwijsproducten().add(curOnderwijsproduct);
	}

	public void save()
	{
		dupliceerVeldenOnderwijsproduct();
		onderwijsproduct.saveObject();
		curriculum.saveObject();
		slaLijstCurriculumOnderwijsproductOp();
		createOnderwijsproductafname();
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	private void dupliceerVeldenOnderwijsproduct()
	{
		getOnderwijsproduct().setOmvang(getOnderwijsproduct().getOnderwijstijd());
	}

	private void slaLijstCurriculumOnderwijsproductOp()
	{
		for (CurriculumOnderwijsproduct curOnderwijsproduct : getCurriculumOnderwijsproducten())
		{
			getCurriculum().getCurriculumOnderwijsproducten().add(curOnderwijsproduct);
			curOnderwijsproduct.save();
		}
	}

	@AutoForm(label = "Onderwijstijd")
	public BigDecimal getOnderwijsproductOnderwijstijd()
	{
		if (getOnderwijsproduct() != null)
			return getOnderwijsproduct().getOnderwijstijd();
		return null;
	}

	@Override
	public CurriculumWizardModel getObject()
	{
		return this;
	}

	@Override
	public void setObject(CurriculumWizardModel object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(opleiding);
		ComponentUtil.detachQuietly(cohort);
		ComponentUtil.detachQuietly(organisatieEenheid);
		ComponentUtil.detachQuietly(locatie);
		ComponentUtil.detachQuietly(curriculum);
		ComponentUtil.detachQuietly(onderwijsproduct);
	}

	public IModel< ? extends List<CurriculumOnderwijsproduct>> getCurriculumOnderwijsproductenModel()
	{
		return curriculumOnderwijsproducten;
	}
}