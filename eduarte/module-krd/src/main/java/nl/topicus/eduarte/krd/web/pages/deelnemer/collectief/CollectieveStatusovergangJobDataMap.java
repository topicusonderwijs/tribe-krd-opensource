package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class CollectieveStatusovergangJobDataMap<T extends Enum<T>> extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public static final String WIJZIGEN_NA_MUTATIEBEPERKING = "WIJZIGEN_NA_MUTATIEBEPERKING";

	public CollectieveStatusovergangJobDataMap(
			ISelectionComponent< ? extends IdObject, ? extends IdObject> selection,
			CollectieveStatusovergangEditModel<T> collectiefEditModel)
	{
		setSelectie(selection.getSelectedElements());
		setBeginstatus(collectiefEditModel.getBeginstatus());
		setEindstatus(collectiefEditModel.getEindstatus());
		setRedenUitschrijving(collectiefEditModel.getRedenUitschrijving());
		setEinddatum(collectiefEditModel.getEinddatum());
		setDocumentTemplate(collectiefEditModel.getDocumentTemplate());
		setToelichting(collectiefEditModel.getToelichting());
		setStatusovergangBPVInschrijvingToegestaan();
	}

	private void setToelichting(String toelichting)
	{
		put("toelichting", toelichting);
	}

	private void setDocumentTemplate(DocumentTemplate documentTemplate)
	{
		put("documentTemplate", documentTemplate);
	}

	private void setRedenUitschrijving(RedenUitschrijving redenUitschrijving)
	{
		put("redenUitschrijving", redenUitschrijving);
	}

	private void setEinddatum(Date einddatum)
	{
		put("einddatum", einddatum);
	}

	private void setBeginstatus(Enum< ? > beginstatus)
	{
		put("beginstatus", beginstatus);
	}

	private void setEindstatus(Enum< ? > eindstatus)
	{
		put("eindstatus", eindstatus);
	}

	public void setSelectie(List< ? extends IdObject> selectie)
	{
		put("selectie", selectie);
	}

	public void setBPVInschrijvingen(List< ? extends IdObject> bpvInschrijvingen)
	{
		put("bpvInschrijvingen", bpvInschrijvingen);
	}

	private void setStatusovergangBPVInschrijvingToegestaan()
	{
		put("statusovergangBPVInschrijvingToegestaan", new DataSecurityCheck(
			WIJZIGEN_NA_MUTATIEBEPERKING).isActionAuthorized(Enable.class));

	}
}