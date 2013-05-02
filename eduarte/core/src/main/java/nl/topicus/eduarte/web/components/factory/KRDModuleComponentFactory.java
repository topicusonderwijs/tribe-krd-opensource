package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;

import org.apache.wicket.model.IModel;

/**
 * Factory interface voor het aanmaken van componenten die uit de KrdModule komen.
 */
public interface KRDModuleComponentFactory extends ModuleComponentFactory
{
	public void newIntakeWizardKnop(BottomRowPanel parent);

	public void newPersoonAlsRelatieToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel);

	public void newOrganisatieAlsRelatieToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel);

	public void newVerbintenisBeeindigenKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel);

	public void newBPVBeeindigenKnop(BottomRowPanel parent,
			IModel<BPVInschrijving> bpInschrijvingModel, AbstractDeelnemerPage returnPage,
			IModel<Boolean> visibilityModel, String label, ButtonAlignment alignment);

	public void newHerIntakeKnop(BottomRowPanel parent, IModel<Deelnemer> deelnemerModel,
			SecurePage returnPage);

	public void newEditOnderwijsproductAfnameKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<OnderwijsproductAfname> afnameModel,
			IModel<Boolean> visibilityModel);

	public void newVerbintenisKopierenKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, DeelnemerVerbintenisPage returnPage,
			IModel<Boolean> visibilityModel);

	public void newBPVKopierenKnop(BottomRowPanel parent, IModel<BPVInschrijving> bpvModel,
			SecurePage returnPage, IModel<Boolean> visibilityModel);

	public void newVerbintenisToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, DeelnemerVerbintenisPage returnPage);

	public void newVerbintenisBewerkOfVerwijderenKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel,
			DeelnemerVerbintenisPage returnPage);

	public <T extends VrijVeldable< ? >> AbstractVrijVeldEntiteitPanel<T> newVrijVeldEntiteitEditPanel(
			String id, IModel<T> model, String header);

	public void koppelContractenCollectiefKnop(BottomRowPanel parent,
			IModel<Contract> verbintenisModel, ContractOverzichtPage returnPage);
}