package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.testen.TestCategorie;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

public class TestDefinitieZoekFilter extends AbstractZoekFilter<TestDefinitie>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_200")
	private String naam;

	@AutoForm(htmlClasses = "unit_200", label = "Categorie")
	private IModel<TestCategorie> testCategorie;

	@AutoForm(label = "status", editorClass = ActiefCombobox.class)
	private Boolean actief;

	public TestDefinitieZoekFilter()
	{
		addOrderByProperty("naam");
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public TestCategorie getTestCategorie()
	{
		return getModelObject(testCategorie);
	}

	public void setTestCategorie(TestCategorie testCategorie)
	{
		this.testCategorie = makeModelFor(testCategorie);
	}

	public void setTestCategorieModel(IModel<TestCategorie> testCategorieModel)
	{
		this.testCategorie = testCategorieModel;
	}

	public Boolean isActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}
}
