package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.EigenGroepenCombobox;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class PersoonlijkeGroepZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<PersoonlijkeGroep>
{
	private static final long serialVersionUID = 1L;

	private String code;

	private String omschrijving;

	private IModel<Medewerker> medewerkerEigenaar;

	private IModel<Deelnemer> deelnemerEigenaar;

	private IModel<Deelnemer> deelnemer;

	/**
	 * Geeft aan of de gedeelde groepen van anderen ook getoond moeten worden.
	 */
	@AutoForm(editorClass = EigenGroepenCombobox.class)
	private boolean toonGroepenVanAnderen;

	public PersoonlijkeGroepZoekFilter()
	{
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Medewerker getMedewerkerEigenaar()
	{
		return getModelObject(medewerkerEigenaar);
	}

	public void setMedewerkerEigenaar(Medewerker medewerkerEigenaar)
	{
		this.medewerkerEigenaar = makeModelFor(medewerkerEigenaar);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Deelnemer getDeelnemerEigenaar()
	{
		return getModelObject(deelnemerEigenaar);
	}

	public void setDeelnemerEigenaar(Deelnemer deelnemerEigenaar)
	{
		this.deelnemerEigenaar = makeModelFor(deelnemerEigenaar);
	}

	public boolean isToonGroepenVanAnderen()
	{
		return toonGroepenVanAnderen;
	}

	public void setToonGroepenVanAnderen(boolean toonGroepenVanAnderen)
	{
		this.toonGroepenVanAnderen = toonGroepenVanAnderen;
	}
}
