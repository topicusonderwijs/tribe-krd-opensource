package nl.topicus.eduarte.web.components.factory;

import java.util.Date;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Factory interface voor het aanmaken van componenten die uit de Participatiemodule
 * komen.
 */
public interface ParticipatieModuleComponentFactory extends ModuleComponentFactory
{
	public void newHerhalendeAbsentieMeldingToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel);

	public void newAbsentieMeldingToevoegenKnop(BottomRowPanel parent,
			IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel);

	public Page newAbsentieMeldingToevoegenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			AbsentieMelding absentieMelding, Page returnToPage);

	public void newWaarnemingToevoegenKnop(BottomRowPanel parent, IModel<Deelnemer> deelnemerModel,
			IModel<Verbintenis> verbintenisModel, Page returnToPage);

	public Class< ? extends Page> getDeelnemerAgendaPageClass();

	public CustomDataPanelPageLinkRowFactory<Waarneming> getEditWaarnemingenPageRowFactory(
			Page parent, IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel);

	public Page newDeelnemerAgendaPage(Deelnemer deelnemer, Verbintenis verbintenis, Date startDate);

	public Panel newInloopCollegesHomePanel(String id, Deelnemer deelnemer);

	public Page getParticipatieMaandOverzichtPageLink(String id, IModel<Deelnemer> deelnemerModel);

}