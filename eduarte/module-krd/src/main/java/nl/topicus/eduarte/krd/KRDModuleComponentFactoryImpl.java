package nl.topicus.eduarte.krd;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.components.contracten.buttons.ContractCollectiefKoppelenButton;
import nl.topicus.eduarte.krd.web.components.intake.buttons.IntakeWizardButton;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.components.relatie.buttons.DeelnemerOrganisatieAlsRelatieToevoegenButton;
import nl.topicus.eduarte.krd.web.components.relatie.buttons.DeelnemerPersoonAlsRelatieToevoegenButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.EditOnderwijsproductAfnameButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.HerIntakeButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.NieuweVerbintenisButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.VerbintenisBeeindigenButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.VerbintenisBewerkOfBeeindigButton;
import nl.topicus.eduarte.krd.web.components.verbintenis.buttons.VerbintenisKopierenButton;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons.BPVInschrijvingBeeindigenButton;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.buttons.BPVKopierenButton;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;

import org.apache.wicket.model.IModel;

/**
 * Factory voor componenten binnen de KRD Module waaraan door de kern module wordt
 * gerefereerd.
 */
public class KRDModuleComponentFactoryImpl extends AbstractModuleComponentFactory implements
		KRDModuleComponentFactory
{
	/**
	 * Constructor.
	 */
	public KRDModuleComponentFactoryImpl()
	{
		super(1);
	}

	public void newIntakeWizardKnop(BottomRowPanel parent)
	{
		parent.addButton(new IntakeWizardButton(parent));
	}

	public void newPersoonAlsRelatieToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel)
	{
		parent.addButton(new DeelnemerPersoonAlsRelatieToevoegenButton(parent, deelnemerModel,
			verbintenisModel));
	}

	public void newOrganisatieAlsRelatieToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel)
	{
		parent.addButton(new DeelnemerOrganisatieAlsRelatieToevoegenButton(parent, deelnemerModel,
			verbintenisModel));
	}

	public void newVerbintenisBeeindigenKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel)
	{
		AbstractBottomRowButton button =
			new VerbintenisBeeindigenButton(parent, verbintenisModel, visibilityModel);
		button.setAlignment(ButtonAlignment.LEFT);

		parent.addButton(button);
	}

	public void newBPVBeeindigenKnop(BottomRowPanel parent,
			IModel<BPVInschrijving> bpInschrijvingModel, AbstractDeelnemerPage returnPage,
			IModel<Boolean> visibilityModel, String label, ButtonAlignment alignment)
	{
		AbstractBottomRowButton button =
			new BPVInschrijvingBeeindigenButton(parent, bpInschrijvingModel, returnPage,
				visibilityModel, label);
		button.setAlignment(alignment);
		parent.addButton(button);
	}

	public void newHerIntakeKnop(BottomRowPanel parent, IModel<Deelnemer> deelnemerModel,
			SecurePage returnPage)
	{
		parent.addButton(new HerIntakeButton(parent, deelnemerModel, returnPage));
	}

	@Override
	public void newEditOnderwijsproductAfnameKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<OnderwijsproductAfname> afnameModel,
			IModel<Boolean> visibilityModel)
	{
		parent.addButton(new EditOnderwijsproductAfnameButton(parent, verbintenisModel,
			afnameModel, visibilityModel));

	}

	@Override
	public void newVerbintenisKopierenKnop(BottomRowPanel parent,
			final IModel<Verbintenis> verbintenisModel, final DeelnemerVerbintenisPage returnPage,
			IModel<Boolean> visibilityModel)
	{
		parent.addButton(new VerbintenisKopierenButton(parent, verbintenisModel, returnPage,
			visibilityModel));
	}

	@Override
	public void newBPVKopierenKnop(BottomRowPanel parent, final IModel<BPVInschrijving> bpvModel,
			final SecurePage returnPage, IModel<Boolean> visibilityModel)
	{
		parent.addButton(new BPVKopierenButton(parent, bpvModel, returnPage, visibilityModel));
	}

	@Override
	public void newVerbintenisToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, DeelnemerVerbintenisPage returnPage)
	{
		parent.addButton(new NieuweVerbintenisButton(parent, deelnemerModel, returnPage));

	}

	@Override
	public void newVerbintenisBewerkOfVerwijderenKnop(BottomRowPanel parent,
			IModel<Verbintenis> verbintenisModel, IModel<Boolean> visibilityModel,
			DeelnemerVerbintenisPage returnPage)
	{
		parent.addButton(new VerbintenisBewerkOfBeeindigButton(parent, verbintenisModel,
			visibilityModel, returnPage));

	}

	@Override
	public <T extends VrijVeldable< ? >> AbstractVrijVeldEntiteitPanel<T> newVrijVeldEntiteitEditPanel(
			String id, IModel<T> model, String header)
	{
		return new VrijVeldEntiteitEditPanel<T>(id, model, header);
	}

	@Override
	public void koppelContractenCollectiefKnop(BottomRowPanel parent,
			final IModel<Contract> contractModel, final ContractOverzichtPage returnPage)
	{
		parent.addButton(new ContractCollectiefKoppelenButton(parent, contractModel, returnPage));

	}
}
