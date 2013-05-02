package nl.topicus.eduarte.zoekfilters.bpv;

import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.entities.bpv.BPVCriteria.BPVCriteriaStatus;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author schimmel
 */
public class BPVCriteriaZoekFilter extends AbstractZoekFilter<BPVCriteria>
{
	private static final long serialVersionUID = 1L;

	private boolean actief = true;

	private String naam;

	private String omschrijving;

	private IModel<BPVCriteriaStatus> status;

	private boolean toegestaanKoppelenMetExterneOrganisatie = true;

	private boolean toegestaanKoppelenMetStagePlaats = true;

	private boolean toegestaanKoppelenMetOnderwijsproduct = true;

	private boolean toegestaanKoppelenMetStageProfiel = true;

	private boolean toegestaanKoppelenMetStageKandidaat = true;

	public static BPVCriteriaZoekFilter createDefaultFilter()
	{
		BPVCriteriaZoekFilter ret = new BPVCriteriaZoekFilter();
		ret.addOrderByProperty("naam");
		return ret;
	}

	public void setStatus(BPVCriteriaStatus status)
	{
		this.status = makeModelFor(status);
	}

	public BPVCriteriaStatus getStatus()
	{
		return getModelObject(status);
	}

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

	public void setToegestaanKoppelenMetOnderwijsproduct(
			boolean toegestaanKoppelenMetOnderwijsproduct)
	{
		this.toegestaanKoppelenMetOnderwijsproduct = toegestaanKoppelenMetOnderwijsproduct;
	}

	public boolean isToegestaanKoppelenMetOnderwijsproduct()
	{
		return toegestaanKoppelenMetOnderwijsproduct;
	}

	public void setToegestaanKoppelenStageProfiel(boolean toegestaanKoppelenMetStageProfiel)
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

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isActief()
	{
		return actief;
	}

}
