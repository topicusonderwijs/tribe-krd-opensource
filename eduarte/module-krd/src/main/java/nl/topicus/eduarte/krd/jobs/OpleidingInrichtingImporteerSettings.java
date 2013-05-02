package nl.topicus.eduarte.krd.jobs;

import java.io.File;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.file.TempFileUploadField;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.jobs.OverschrijfActie;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class OpleidingInrichtingImporteerSettings implements IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = TempFileUploadField.class, required = true, htmlClasses = "unit_max")
	private File bestand;

	@AutoForm(label = "Importeer", required = true, htmlClasses = "unit_max")
	private SchrijfSelectie importeerActie;

	@AutoForm(label = "Bij bestaande criteria", required = true, htmlClasses = "unit_max")
	private OverschrijfActie actieBijBestaandCriterium;

	@AutoForm(label = "Bij bestaande productregels", required = true, htmlClasses = "unit_max")
	private OverschrijfActie actieBijBestaandeProductregel;

	@AutoForm(htmlClasses = "unit_max")
	private IModel<Opleiding> opleiding;

	@AutoForm(htmlClasses = "unit_max")
	private IModel<Cohort> cohort;

	public OpleidingInrichtingImporteerSettings()
	{
		setImporteerActie(SchrijfSelectie.CriteriaEnProductregels);
		setActieBijBestaandCriterium(OverschrijfActie.Overslaan);
		setActieBijBestaandeProductregel(OverschrijfActie.Overslaan);
	}

	public File getBestand()
	{
		return bestand;
	}

	public void setBestand(File bestand)
	{
		this.bestand = bestand;
	}

	public SchrijfSelectie getImporteerActie()
	{
		return importeerActie;
	}

	public void setImporteerActie(SchrijfSelectie importeerActie)
	{
		this.importeerActie = importeerActie;
	}

	public OverschrijfActie getActieBijBestaandCriterium()
	{
		return actieBijBestaandCriterium;
	}

	public void setActieBijBestaandCriterium(OverschrijfActie actieBijBestaandCriterium)
	{
		this.actieBijBestaandCriterium = actieBijBestaandCriterium;
	}

	public OverschrijfActie getActieBijBestaandeProductregel()
	{
		return actieBijBestaandeProductregel;
	}

	public void setActieBijBestaandeProductregel(OverschrijfActie actieBijBestaandeProductregel)
	{
		this.actieBijBestaandeProductregel = actieBijBestaandeProductregel;
	}

	public Opleiding getOpleiding()
	{
		return opleiding == null ? null : opleiding.getObject();
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding == null ? null : ModelFactory.getModel(opleiding);
	}

	public Cohort getCohort()
	{
		return cohort == null ? null : cohort.getObject();
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort == null ? null : ModelFactory.getModel(cohort);
	}

	@AutoForm(include = false)
	public boolean isCriteriaImporteren()
	{
		return getImporteerActie() != SchrijfSelectie.Productregels;
	}

	@AutoForm(include = false)
	public boolean isProductregelsImporteren()
	{
		return getImporteerActie() != SchrijfSelectie.Criteria;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(opleiding);
	}
}
