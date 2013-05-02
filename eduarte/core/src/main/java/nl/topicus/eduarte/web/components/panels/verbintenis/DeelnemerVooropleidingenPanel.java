package nl.topicus.eduarte.web.components.panels.verbintenis;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVerificatieStatus;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding.SoortVooropleidingOrganisatie;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VooropleidingVakResultaatTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DeelnemerVooropleidingenPanel extends TypedPanel<Vooropleiding>
{

	private static final long serialVersionUID = 1L;

	private WebMarkupContainer verificatie;

	public DeelnemerVooropleidingenPanel(String id, IModel<Vooropleiding> model)
	{
		super(id, model);
		createAutoFieldSet();
	}

	private void createAutoFieldSet()
	{
		AutoFieldSet<Vooropleiding> details =
			new AutoFieldSet<Vooropleiding>("details", getVooropleidingModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getModelObject() != null;
				}
			};
		details.setRenderMode(RenderMode.DISPLAY);
		details.setPropertyNames("soortOrganisatie", "naam", "plaats", "brincode",
			"soortVooropleiding", "soortOnderwijs", "schooladvies", "citoscore", "diplomaBehaald",
			"land.naam", "begindatum", "einddatum", "aantalJarenOnderwijs");
		details.setSortAccordingToPropertyNames(true);
		details.addFieldModifier(new LabelModifier("soortOnderwijs", "Categorie vooropleiding"));

		details.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return isBuitenlandseVooropleiding();
			}

		}, "land.naam"));

		details.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return heeftBrincode();
			}

		}, "brincode"));

		details.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return heeftCitoscore();
			}

		}, "citoscore"));

		add(details);

		add(new Label("noDetails", "Geen vooropleiding gevonden")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& DeelnemerVooropleidingenPanel.this.getDefaultModelObject() == null;
			}
		});

		VrijVeldEntiteitPanel<VooropleidingVrijVeld, Vooropleiding> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<VooropleidingVrijVeld, Vooropleiding>("vrijVelden",
				getVooropleidingModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getVooropleiding().getVrijVelden().size() > 0;
				}

			};
		vrijVeldenPanel.setDossierScherm(true);
		add(vrijVeldenPanel);

		PropertyModel<List<VooropleidingVakResultaat>> pmodel =
			new PropertyModel<List<VooropleidingVakResultaat>>(getVooropleidingModel(),
				"vooropleidingVakResultaten");

		DeelnemerVooropleidingVakResultaatEditPanel panel =
			new DeelnemerVooropleidingVakResultaatEditPanel("vakresultaateditpanel", pmodel, null,
				new VooropleidingVakResultaatTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public VooropleidingVakResultaat createNewT()
				{
					VooropleidingVakResultaat ret =
						new VooropleidingVakResultaat(getVooropleiding());
					ret.setVak(new VooropleidingVak());
					return ret;
				}

				@Override
				public boolean isVisible()
				{
					return isHogerOnderwijs();
				}

				@Override
				protected boolean isEditable()
				{
					return false;
				}

			};
		add(panel);
		add(createVooropleidingVerificatiePanel());
	}

	private WebMarkupContainer createVooropleidingVerificatiePanel()
	{
		verificatie = new WebMarkupContainer("verificatie");
		verificatie.add(createVooropleidingVerificatieAutoFieldSet(null));
		verificatie.setVisible(isHogerOnderwijs());
		return verificatie;
	}

	private AutoFieldSet<Vooropleiding> createVooropleidingVerificatieAutoFieldSet(
			List<VooropleidingVerificatieStatus> enumAvailableStatusList)
	{
		AutoFieldSet<Vooropleiding> verificatieAutoFieldSet =
			new AutoFieldSet<Vooropleiding>("verificatieFieldset", getVooropleidingModel(),
				"Verificatie");
		verificatieAutoFieldSet.setPropertyNames("verificatieStatus", "verificatieDatum",
			"verificatieDoor", "verificatieDoorInstelling", "verificatieDoorMedewerker");

		if (enumAvailableStatusList != null)
		{
			verificatieAutoFieldSet.addFieldModifier(new ConstructorArgModifier(
				"verificatieStatus", ModelFactory.getModel(enumAvailableStatusList)));
		}

		verificatieAutoFieldSet.setRenderMode(RenderMode.DISPLAY);
		return verificatieAutoFieldSet;
	}

	private boolean isBuitenlandseVooropleiding()
	{
		return getVooropleiding().getSoortOrganisatie().equals(
			SoortVooropleidingOrganisatie.Buitenland);
	}

	private boolean heeftBrincode()
	{
		return getVooropleiding().getSoortOrganisatie().equals(
			SoortVooropleidingOrganisatie.ExterneOrganisatie);
	}

	private boolean heeftCitoscore()
	{
		SoortVooropleiding soort = getVooropleiding().getSoortVooropleiding();
		return soort != null
			&& (soort.getSoortOnderwijsMetDiploma().equals(SoortOnderwijs.Basisonderwijs) || soort
				.getSoortOnderwijsZonderDiploma().equals(SoortOnderwijs.Basisonderwijs));
	}

	public Vooropleiding getVooropleiding()
	{
		return getModelObject();
	}

	public IModel<Vooropleiding> getVooropleidingModel()
	{
		return getModel();
	}

	protected Boolean isHogerOnderwijs()
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS);
	}

}
