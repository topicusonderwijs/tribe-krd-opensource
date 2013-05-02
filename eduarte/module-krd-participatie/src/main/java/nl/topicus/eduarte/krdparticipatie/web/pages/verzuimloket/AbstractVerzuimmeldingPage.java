package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimdag;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.krdparticipatie.util.IbgVerzuimloketUtil;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.model.IModel;

public abstract class AbstractVerzuimmeldingPage extends AbstractDeelnemerPage
{
	protected IChangeRecordingModel<IbgVerzuimmelding> verzuimmeldingModel;

	public AbstractVerzuimmeldingPage(MenuItemKey selectedMenuItem, Deelnemer deelnemer,
			Verbintenis verbintenis, IbgVerzuimmelding melding)
	{
		super(selectedMenuItem, deelnemer, verbintenis);
		verzuimmeldingModel =
			ModelFactory.getCompoundChangeRecordingModel(melding, new DefaultModelManager(
				IbgVerzuimdag.class, IbgVerzuimmelding.class));

	}

	@SuppressWarnings("unchecked")
	public AbstractVerzuimmeldingPage(MenuItemKey selectedMenuItem, Deelnemer deelnemer,
			Verbintenis verbintenis, IModel meldingModel)
	{
		super(selectedMenuItem, deelnemer, verbintenis);
		verzuimmeldingModel = (IChangeRecordingModel<IbgVerzuimmelding>) meldingModel;
	}

	protected static IbgVerzuimmelding getDefaultVerzuimmelding(Verbintenis inschrijving)
	{
		IbgVerzuimmelding melding = IbgVerzuimmelding.createIbgVerzuimmelding(inschrijving);
		IbgVerzuimloketUtil.setDefaultStatus(melding);
		IbgVerzuimloketUtil.generateNextMeldingsnummer(melding);
		Medewerker medewerker = getIngelogdeMedewerker();
		melding.setAanduidingContactpersoon(medewerker.getPersoon().getAchternaamVoorletters());
		PersoonContactgegeven emailadres = medewerker.getPersoon().getEersteEmailAdres();
		if (emailadres != null)
		{
			melding.setEmailadresMelder(emailadres.getContactgegeven());
		}
		// PersoonContactgegeven telefoonnummer =
		// medewerker.getPersoon().getEersteTelefoon();
		//
		// if (telefoonnummer != null)
		// {
		// melding.setAbonneenummerMelder(telefoonnummer.toString());
		// }

		return melding;
	}

	protected IbgVerzuimmelding getContextIbgVerzuimmelding()
	{
		return verzuimmeldingModel.getObject();
	}

	protected IChangeRecordingModel<IbgVerzuimmelding> getVerzuimmeldingModel()
	{
		return verzuimmeldingModel;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(verzuimmeldingModel);
	}
}
