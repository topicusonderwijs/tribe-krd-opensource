package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

@Entity
public class BPVCriteria extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	public static enum BPVCriteriaStatus
	{
		Nieuw,
		Goedgekeurd,
		Afgekeurd
	}

	@Column(nullable = false)
	private Boolean actief;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = false, name = "toegestaanKoppelExtOrg")
	@AutoForm(label = "Koppelen met externe organisatie is toegestaan")
	private boolean toegestaanKoppelenMetExterneOrganisatie = true;

	@Column(nullable = false, name = "toegestaanKoppelStagePlaats")
	@AutoForm(label = "Koppelen met stage plaats is toegestaan")
	private boolean toegestaanKoppelenMetStagePlaats = true;

	@Column(nullable = false, name = "toegestaanKoppelOP")
	@AutoForm(label = "Koppelen met onderwijsproduct is toegestaan")
	private boolean toegestaanKoppelenMetOnderwijsproduct = true;

	@Column(nullable = false, name = "toegestaanKoppelStageProfiel")
	@AutoForm(label = "Koppelen met stage profiel is toegestaan")
	private boolean toegestaanKoppelenMetStageProfiel = true;

	@Column(nullable = false, name = "toegestaanKoppelStageKandidaat")
	@AutoForm(label = "Koppelen met stage kandidaat is toegestaan")
	private boolean toegestaanKoppelenMetStageKandidaat = true;

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setToegestaanKoppelenMetExterneOrganisatie(
			boolean toegestaanKoppelenMetExterneOrganisatie)
	{
		this.toegestaanKoppelenMetExterneOrganisatie = toegestaanKoppelenMetExterneOrganisatie;
	}

	public boolean isToegestaanKoppelenMetExterneOrganisatie()
	{
		return toegestaanKoppelenMetExterneOrganisatie;
	}

	public void setToegestaanKoppelenMetStagePlaats(boolean toegestaanKoppelenMetStagePlaats)
	{
		this.toegestaanKoppelenMetStagePlaats = toegestaanKoppelenMetStagePlaats;
	}

	public boolean isToegestaanKoppelenMetStagePlaats()
	{
		return toegestaanKoppelenMetStagePlaats;
	}

	public void setToegestaanKoppelenMetStageProfiel(boolean toegestaanKoppelenMetStageProfiel)
	{
		this.toegestaanKoppelenMetStageProfiel = toegestaanKoppelenMetStageProfiel;
	}

	public boolean isToegestaanKoppelenMetStageProfiel()
	{
		return toegestaanKoppelenMetStageProfiel;
	}

	public void setToegestaanKoppelenMetStageKandidaat(boolean toegestaanKoppelenMetStageKandidaat)
	{
		this.toegestaanKoppelenMetStageKandidaat = toegestaanKoppelenMetStageKandidaat;
	}

	public boolean isToegestaanKoppelenMetStageKandidaat()
	{
		return toegestaanKoppelenMetStageKandidaat;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Boolean isActief()
	{
		return actief;
	}

	public void setToegestaanKoppelenMetOnderwijsproduct(
			boolean toegestaanKoppelenMetOnderwijsproduct)
	{
		this.toegestaanKoppelenMetOnderwijsproduct = toegestaanKoppelenMetOnderwijsproduct;
	}

	public boolean isToegestaanKoppelenMetOnderwijsproduct()
	{
		return toegestaanKoppelenMetOnderwijsproduct;
	}

	@Override
	public String toString()
	{
		return naam;
	}

}
