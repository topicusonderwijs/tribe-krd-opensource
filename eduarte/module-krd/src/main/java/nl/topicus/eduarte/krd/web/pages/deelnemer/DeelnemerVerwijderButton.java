package nl.topicus.eduarte.krd.web.pages.deelnemer;

import java.lang.reflect.Field;
import java.util.EnumSet;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.app.sidebar.datastores.RecenteDeelnemersDataStore;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerVerwijderen;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@InPrincipal(DeelnemerVerwijderen.class)
public class DeelnemerVerwijderButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVerwijderButton(BottomRowPanel bottomRow, IModel<Deelnemer> deelnemerModel)
	{
		super(bottomRow, "Deelnemer verwijderen", CobraKeyAction.GEEN, ButtonAlignment.LEFT);
		setDefaultModel(deelnemerModel);
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && isDeelnemerVerwijderbaar();
	}

	private boolean isDeelnemerVerwijderbaar()
	{
		for (Verbintenis curVerbintenis : getDeelnemer().getVerbintenissen())
		{
			if (EnumSet.of(VerbintenisStatus.Volledig, VerbintenisStatus.Afgedrukt,
				VerbintenisStatus.Definitief, VerbintenisStatus.Beeindigd).contains(
				curVerbintenis.getStatus()))
				return false;
		}
		return true;
	}

	private Deelnemer getDeelnemer()
	{
		return (Deelnemer) getDefaultModelObject();
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		ConfirmationLink<Void> ret =
			new ConfirmationLink<Void>(linkId,
				"Weet u zeker dat u de deelnemer, met alle verbintenissen wilt verwijderen? "
					+ "Dit kan niet ongedaan gemaakt worden.")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					try
					{
						// Verwijderen van groepsdeelnames waarbij groep == null
						for (Groepsdeelname deelname : getDeelnemer().getGroepsdeelnames())
						{
							if (deelname.getGroep() == null)
								deelname.delete();
						}
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

						RecenteDeelnemersDataStore deelnemerStore =
							EduArteSession.get().getSideBarDataStore(
								RecenteDeelnemersDataStore.class);
						deelnemerStore.getInschrijvingenModel().getObject().removeAll(
							getDeelnemer().getVerbintenissen());
						deelnemerStore.getInschrijvingenModel().detach();
						IChangeRecordingModel<Deelnemer> deelnemer =
							ModelFactory.getCompoundChangeRecordingModel(getDeelnemer(),
								new DefaultModelManager(VrijVeldOptieKeuze.class,
									IntakegesprekVrijVeld.class, PersoonVrijVeld.class,
									PlaatsingVrijVeld.class, RelatieVrijVeld.class,
									VerbintenisVrijVeld.class, VooropleidingVrijVeld.class,
									PersoonAdres.class, Adres.class,
									PersoonExterneOrganisatie.class, Relatie.class,
									AbstractRelatie.class, PersoonContactgegeven.class,
									OnderwijsproductAfnameContext.class,
									OnderwijsproductAfname.class, Plaatsing.class,
									VerbintenisContract.class, Intakegesprek.class,
									Bekostigingsperiode.class, Verbintenis.class,
									Vooropleiding.class, Deelnemer.class, Persoon.class)
								{
									private static final long serialVersionUID = 1L;

									@Override
									public boolean isManaged(Object object, Field field)
									{
										if (field != null && field.getName().equals("relatie")
											&& field.getDeclaringClass().equals(Relatie.class))
											return false;
										return super.isManaged(object, field);
									}
								});
						deelnemer.deleteObject();
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
						setResponsePage(DeelnemerZoekenPage.class);
					}
					catch (DataAccessException e)
					{
						error("Deze deelnemer kan niet meer verwijderd worden omdat hij reeds in gebruik is.");
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchRollback();
					}
				}
			};
		ComponentUtil.setSecurityCheck(ret, new DeelnemerSecurityCheck(new ClassSecurityCheck(
			DeelnemerVerwijderButton.class), getDeelnemer()));
		return ret;
	}
}
