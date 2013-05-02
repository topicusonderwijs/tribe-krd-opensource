package nl.topicus.eduarte.krd.web.pages.beheer.vrijveld;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldType;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.VrijVeldenPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.VrijVeldKeuzeOptieTable;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.vrijveldkeuzeoptie.VrijVeldKeuzeOptieEditPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Vrij veld", menu = "Beheer > Beheer tabellen > [nieuw VrijVeld], Beheer > Beheer tabellen > [VrijVeld] > Bewerken")
@InPrincipal(VrijVeldenPrincipal.class)
public class VrijVeldEditPage extends AbstractBeheerPage<VrijVeld> implements
		IModuleEditPage<VrijVeld>
{
	private Form<Void> form;

	private VrijVeldModel vrijveldModel;

	private VrijVeldKeuzeOptieEditPanel keuzeOptiesPanel;

	private IsTaxonomieEnabled isTaxonomieEnabled = new IsTaxonomieEnabled();

	private AutoFieldSet<VrijVeld> algemeenFieldSet;

	public VrijVeldEditPage(VrijVeldZoekenPage returnPage)
	{
		this(ModelFactory.getModel(new VrijVeld()), returnPage);
	}

	public VrijVeldEditPage(IModel<VrijVeld> vrijveldModel, VrijVeldZoekenPage returnPage)
	{
		this(new VrijVeldModel(vrijveldModel.getObject()), returnPage);
	}

	private VrijVeldEditPage(VrijVeldModel vrijveld, VrijVeldZoekenPage returnPage)
	{
		super(vrijveld.getEntiteitModel(), BeheerMenuItem.VrijeVelden);
		vrijveldModel = vrijveld;
		setReturnPage(returnPage);

		form = new Form<Void>("form");
		add(form);
		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent[] {};
			}

			@Override
			public void validate(Form< ? > frm)
			{
				Boolean dossierscherm =
					(Boolean) ((FormComponent< ? >) algemeenFieldSet.getFieldProperties(
						"dossierscherm").getComponent()).getConvertedInput();
				Boolean intakescherm =
					(Boolean) ((FormComponent< ? >) algemeenFieldSet.getFieldProperties(
						"intakescherm").getComponent()).getConvertedInput();
				boolean valid =
					(dossierscherm != null && dossierscherm.booleanValue())
						|| (intakescherm != null && intakescherm.booleanValue());
				if (!valid)
				{
					frm.error("Intakescherm of dossierscherm moet aangevinkt worden");
				}
			}
		});

		keuzeOptiesPanel =
			new VrijVeldKeuzeOptieEditPanel("keuzeOptiesPanel",
				new PropertyModel<List<VrijVeldKeuzeOptie>>(getDefaultModel(),
					"vrijVeldKeuzeOpties"), vrijveldModel.getEntiteitManager(),
				new VrijVeldKeuzeOptieTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public VrijVeldKeuzeOptie createNewT()
				{
					VrijVeldKeuzeOptie optie = new VrijVeldKeuzeOptie();
					optie.setVrijVeld(getVrijVeld());
					return optie;
				}

				@Override
				protected boolean isDeletable()
				{
					return !getVrijVeld().isSaved() || !getVrijVeld().isInGebruik();
				}
			};

		keuzeOptiesPanel.setVisible(VrijVeldType.KEUZELIJST.equals(getVrijVeld().getType())
			|| VrijVeldType.MULTISELECTKEUZELIJST.equals(getVrijVeld().getType()));
		form.add(keuzeOptiesPanel);
		addAlgemeenFieldSet();
		createComponents();
	}

	private void addAlgemeenFieldSet()
	{
		algemeenFieldSet = new AutoFieldSet<VrijVeld>("vrijveld", getContextModel(), "Vrij veld");
		algemeenFieldSet.setPropertyNames("naam", "categorie", "taxonomie", "type", "intakescherm",
			"dossierscherm", "uitgebreidzoeken", "actief");
		algemeenFieldSet.setRenderMode(RenderMode.EDIT);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);
		form.add(algemeenFieldSet);
		algemeenFieldSet.addFieldModifier(new EnableModifier("taxonomie", isTaxonomieEnabled));
		IModel<List<VrijVeldCategorie>> choices =
			ModelFactory.getModel(VrijVeldCategorie.getKRDCategorieeeen());
		algemeenFieldSet.addFieldModifier(new ConstructorArgModifier("categorie", choices));
		algemeenFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("categorie", "taxonomie"));
		algemeenFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("type", keuzeOptiesPanel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (getVrijVeld().getType().equals(VrijVeldType.KEUZELIJST)
					|| getVrijVeld().getType().equals(VrijVeldType.MULTISELECTKEUZELIJST))
					keuzeOptiesPanel.setVisible(true);
				else
					keuzeOptiesPanel.setVisible(false);
			}
		});
		algemeenFieldSet.addFieldModifier(new EnableModifier("type",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return !getVrijVeld().isSaved()
						|| getVrijVeld().getVrijVeldKeuzeOpties().size() == 0;
				}
			}));

		form.add(new UniqueConstraintFormValidator(algemeenFieldSet, "functie", "naam"));
	}

	private final class IsTaxonomieEnabled extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean getObject()
		{
			return VrijVeldCategorie.VERBINTENIS.equals(getVrijVeld().getCategorie())
				|| VrijVeldCategorie.UITSCHRIJVING.equals(getVrijVeld().getCategorie());
		}

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				// maak de keuzelijst leeg wanneer we geen keuzelijst vrijveld aanmaken,
				// dit is natuurlijk onnodig.
				if (!getVrijVeld().getType().equals(VrijVeldType.KEUZELIJST)
					&& !getVrijVeld().getType().equals(VrijVeldType.MULTISELECTKEUZELIJST)
					&& getVrijVeld().getVrijVeldKeuzeOpties().size() > 0)
					getVrijVeld().getVrijVeldKeuzeOpties().clear();

				VrijVeldEditPage.this.vrijveldModel.save();
				EduArteRequestCycle.get().setResponsePage(getReturnPage());
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return getReturnPageClass();
			}

		}));

		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVrijVeld().isSaved() && !getVrijVeld().isInGebruik();
			}

			@Override
			protected void onClick()
			{
				if (getVrijVeld().isSaved() && !getVrijVeld().isInGebruik())
				{
					if (getVrijVeld().getType().equals(VrijVeldType.MULTISELECTKEUZELIJST)
						|| getVrijVeld().getType().equals(VrijVeldType.KEUZELIJST))
					{
						for (VrijVeldKeuzeOptie optie : getVrijVeld().getVrijVeldKeuzeOpties())
						{
							optie.delete();
						}
					}

					getVrijVeld().delete();
					getVrijVeld().commit();

					setResponsePage(getReturnPage());
				}
			}
		});
	}

	private VrijVeld getVrijVeld()
	{
		return (VrijVeld) getDefaultModelObject();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		if (vrijveldModel != null)
			vrijveldModel.detach();
	}
}
