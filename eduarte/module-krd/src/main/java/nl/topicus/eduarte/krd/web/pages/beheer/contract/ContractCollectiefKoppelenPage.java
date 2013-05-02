package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractVerbintenisSelectiePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

@PageInfo(title = "Contract", menu = "Relatie > Contract > [Contract] > Collectief koppelen")
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class ContractCollectiefKoppelenPage extends AbstractVerbintenisSelectiePage
{
	private static final long serialVersionUID = 1L;

	public ContractCollectiefKoppelenPage(SecurePage returnPage, IModel<Contract> contractModel)
	{
		super(returnPage, new VerbintenisZoekFilter(), new ContractKoppelenSelectieTarget(
			contractModel));
	}
}
