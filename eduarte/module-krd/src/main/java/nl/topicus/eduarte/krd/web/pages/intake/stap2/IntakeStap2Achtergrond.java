package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.pages.intake.stap1.IntakeStap1Personalia;
import nl.topicus.eduarte.krd.web.pages.intake.stap3.IntakeStap3Intakegesprekken;
import nl.topicus.eduarte.krd.web.pages.intake.stap4.IntakeStap4Verbintenis;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RelatieTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VooropleidingTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

@PageInfo(title = "Intake stap 2", menu = {"Deelnemer > intake"})
public class IntakeStap2Achtergrond extends IntakeWizardPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	public IntakeStap2Achtergrond(IntakeWizardModel model)
	{
		setWizard(model);
		Deelnemer obj = model.getDeelnemer();

		if (obj.getPersoon().getGeboortedatum() != null)
			createMeldingen(obj);

		voegRelatiesComponentenToe();
		voegVooropleidingenComponentenToe();

		form = new Form<Void>("form");
		form.add(new BronValidatingFormComponent("bronvalidator", this, form));

		createComponents();
	}

	private void createMeldingen(Deelnemer obj)
	{
		boolean heeftBetalingsplichtige = false;
		boolean heeftVertegenwoordiger = false;
		StringBuilder melding = new StringBuilder();

		if (!obj.getPersoon().isMeerderjarig())
		{
			melding.append("Deelnemer is minderjarig ");
			if (obj.getPersoon().getRelaties().size() > 0)
			{
				for (AbstractRelatie relatie : obj.getPersoon().getRelaties())
				{
					if (relatie.isBetalingsplichtige())
						heeftBetalingsplichtige = true;
					if (relatie.isWettelijkeVertegenwoordiger())
						heeftVertegenwoordiger = true;

				}
			}

			if (!heeftBetalingsplichtige && !heeftVertegenwoordiger)
			{
				melding
					.append("en heeft geen betalingsplichtige en geen wettelijke vertegenwoordiger");
			}
			else
			{
				if (!heeftBetalingsplichtige)
					melding.append("en heeft geen betalingsplichtige");
				if (!heeftVertegenwoordiger)
					melding.append("en heeft geen wettelijke vertegenwoordiger");
			}
			if (!heeftBetalingsplichtige || !heeftVertegenwoordiger)
				info(melding.toString());
		}
	}

	private void voegRelatiesComponentenToe()
	{
		ListModelDataProvider<AbstractRelatie> list =
			new ListModelDataProvider<AbstractRelatie>(new RelatiesModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<AbstractRelatie> model(AbstractRelatie object)
				{
					return getWizard().getModel(object);
				}

			};
		CustomDataPanel<AbstractRelatie> datapanel =
			new EduArteDataPanel<AbstractRelatie>("deelnemerRelaties", list,
				new RelatieTable<AbstractRelatie>(getWizard().getDeelnemer().getPersoon()
					.isMeerderjarig()));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<AbstractRelatie>(
			IntakeStap2BewerkPersoonRelatiePage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<AbstractRelatie> item)
			{
				Object obj = item.getModelObject();

				if (obj instanceof Relatie)
				{
					setResponsePage(new IntakeStap2BewerkPersoonRelatiePage(getWizard(),
						(Relatie) obj, false));
				}
				else if (obj instanceof PersoonExterneOrganisatie)
				{
					setResponsePage(new IntakeStap2BewerkOrganisatieRelatiePage(getWizard(),
						(PersoonExterneOrganisatie) obj, false));
				}
			}
		});
		add(datapanel);

		voegNieuweRelatieKnoppenToe();
	}

	private void voegVooropleidingenComponentenToe()
	{
		ListModelDataProvider<Vooropleiding> list =
			new ListModelDataProvider<Vooropleiding>(new VooropleidingenModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<Vooropleiding> model(Vooropleiding object)
				{
					return getWizard().getModel(object);
				}

			};
		CustomDataPanel<Vooropleiding> datapanel =
			new EduArteDataPanel<Vooropleiding>("deelnemerVooropleidingen", list,
				new VooropleidingTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> createTitleModel(String title)
				{
					return new Model<String>("Vooropleidingen");
				}

			};
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Vooropleiding>(
			IntakeStap2BewerkVooropleidingPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Vooropleiding> item)
			{
				Vooropleiding vooropleiding = item.getModelObject();

				setResponsePage(new IntakeStap2BewerkVooropleidingPage(getWizard(), vooropleiding,
					false));
			}
		});
		add(datapanel);
		BottomRowPanel knoppenbalk = new BottomRowPanel("nieuweVooropleidingenRow");
		add(knoppenbalk);
		knoppenbalk.addButton(new NieuweVooropleidingKnop(knoppenbalk));
	}

	private void voegNieuweRelatieKnoppenToe()
	{
		BottomRowPanel knoppenbalk = new BottomRowPanel("nieuweRelatiesRow");
		add(knoppenbalk);
		knoppenbalk.addButton(new NieuwePersoonRelatieKnop(knoppenbalk));
		knoppenbalk.addButton(new NieuweOrganisatieRelatieKnop(knoppenbalk));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		voegVorigeKnopToe(panel);
		voegVolgendeKnopToe(panel);
		voegVoltooienKnopToe(panel, form);
		voegAnnulerenKnopToe(panel);
	}

	private void voegVorigeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(new IntakeStap1Personalia(getWizard()));
			}
		});
	}

	private void voegVolgendeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VolgendeButton(panel, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				if (getWizard().getDefaultOrganisatieEenheid().isIntakeWizardStap3Overslaan())
					setResponsePage(new IntakeStap4Verbintenis(getWizard()));
				else
					setResponsePage(new IntakeStap3Intakegesprekken(getWizard()));
			}
		});
	}

	private final class NieuwePersoonRelatieKnop extends AbstractBottomRowButton
	{
		private static final long serialVersionUID = 1L;

		private NieuwePersoonRelatieKnop(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Persoon als relatie toevoegen", null, ButtonAlignment.RIGHT);
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			return new Link<Void>(linkId)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					IntakeWizardModel wizard = getWizard();
					Relatie relatie = wizard.addNieuwePersoonRelatie();
					setResponsePage(new IntakeStap2BewerkPersoonRelatiePage(wizard, relatie, true));
				}
			};
		}
	}

	private final class NieuweOrganisatieRelatieKnop extends AbstractBottomRowButton
	{
		private static final long serialVersionUID = 1L;

		private NieuweOrganisatieRelatieKnop(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Organisatie als relatie toevoegen", null, ButtonAlignment.RIGHT);
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			return new Link<Void>(linkId)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					IntakeWizardModel wizard = getWizard();
					setResponsePage(new IntakeStap2SelecteerOrganisatieRelatiePage(wizard));
				}
			};
		}
	}

	private final class NieuweVooropleidingKnop extends AbstractBottomRowButton
	{
		private static final long serialVersionUID = 1L;

		private NieuweVooropleidingKnop(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Vooropleiding toevoegen", null, ButtonAlignment.RIGHT);
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			return new Link<Void>(linkId)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					IntakeWizardModel wizard = getWizard();
					Vooropleiding vooropleiding = wizard.createNieuweVooropleiding();
					setResponsePage(new IntakeStap2BewerkVooropleidingPage(getWizard(),
						vooropleiding, true));
				}
			};
		}
	}

	public class RelatiesModel extends LoadableDetachableModel<List<AbstractRelatie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<AbstractRelatie> load()
		{
			return getWizard().getDeelnemer().getPersoon().getRelaties();
		}
	}

	public class VooropleidingenModel extends LoadableDetachableModel<List<Vooropleiding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Vooropleiding> load()
		{
			return getWizard().getDeelnemer().getVooropleidingen();
		}
	}

	@Override
	protected int getStapNummer()
	{
		return 2;
	}

	@Override
	protected String getStapTitel()
	{
		return "Achtergrond";
	}

}
