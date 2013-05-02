package nl.topicus.eduarte.resultaten.jobs;

import java.io.File;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.file.TempFileUploadField;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.jobs.OverschrijfActie;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class ResultaatstructuurImporteerSettings implements IDetachable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = TempFileUploadField.class, required = true, htmlClasses = "unit_max")
	private File bestand;

	@AutoForm(label = "Importeer", required = true, htmlClasses = "unit_max")
	private SchrijfSelectie importeerActie;

	@AutoForm(label = "Bij bestaande resultaatstructuren", required = true, htmlClasses = "unit_max")
	private OverschrijfActie actieBijBestaandeStructuur;

	@AutoForm(label = "Bij bestaande verwijzingen", required = true, htmlClasses = "unit_max")
	private OverschrijfActie actieBijBestaandeVerwijzingen;

	@AutoForm(htmlClasses = "unit_max")
	private IModel<Onderwijsproduct> onderwijsproduct;

	@AutoForm(htmlClasses = "unit_max")
	private Type type;

	@AutoForm(htmlClasses = "unit_max")
	private String code;

	public ResultaatstructuurImporteerSettings()
	{
		setImporteerActie(SchrijfSelectie.StructurenEnVerwijzingen);
		setActieBijBestaandeStructuur(OverschrijfActie.Overslaan);
		setActieBijBestaandeVerwijzingen(OverschrijfActie.Overslaan);
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

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct == null ? null : onderwijsproduct.getObject();
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct =
			onderwijsproduct == null ? null : ModelFactory.getModel(onderwijsproduct);
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

	@AutoForm(include = false)
	public boolean isStructurenImporteren()
	{
		return getImporteerActie() != SchrijfSelectie.Toetsverwijzingen;
	}

	@AutoForm(include = false)
	public boolean isVerwijzingenImporteren()
	{
		return getImporteerActie() != SchrijfSelectie.Resultaatstructuren;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(onderwijsproduct);
	}
}
