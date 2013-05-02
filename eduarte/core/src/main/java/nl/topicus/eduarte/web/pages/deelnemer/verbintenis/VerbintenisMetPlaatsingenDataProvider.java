package nl.topicus.eduarte.web.pages.deelnemer.verbintenis;

import java.util.ArrayList;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.model.IModel;

public class VerbintenisMetPlaatsingenDataProvider extends AbstractEntiteitDataProvider<IdObject>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisMetPlaatsingenDataProvider(IModel<Deelnemer> deelnemerModel)
	{
		super(deelnemerModel);
	}

	@Override
	protected void createLijst()
	{
		lijst = new ArrayList<IdObject>();

		for (Verbintenis verbintenis : getDeelnemer().getVerbintenissen())
		{
			if (verbintenis.getStatus() != VerbintenisStatus.Intake)
			{
				lijst.add(verbintenis);
				for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
				{
					lijst.add(plaatsing);
				}
			}
		}
	}

	private Deelnemer getDeelnemer()
	{
		return (Deelnemer) entiteitModel.getObject();
	}
}
