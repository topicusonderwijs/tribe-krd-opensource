package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.AjaxRadioChoice;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Collectief aanmaken", menu = {"Deelnemer > Collectief > Aanmaken"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class CollectiefAanmakenPage extends SecurePage
{
	private Form<Void> form;

	private CollectiefVerbintenisAanmakenPanel verbintenisPanel;

	private CollectiefPlaatsingAanmakenPanel plaatsingPanel;

	public CollectiefAanmakenPage()
	{
		this(new CollectiefAanmakenModel(Verbintenis.class));
	}

	public CollectiefAanmakenPage(CollectiefAanmakenModel collectiefAanmakenModel)
	{
		super(collectiefAanmakenModel, CoreMainMenuItem.Deelnemer);

		setupVerbintenis();

		form = new Form<Void>("form");

		createPlaatsingOfVerbintenisSelectieChoice("soort");
		createNieuwPlaatsingPanel("nieuwePlaatsing");
		createNieuwVerbintenisPanel("nieuweVerbintenis");

		add(form);

		createComponents();
	}

	private void setupVerbintenis()
	{
		Verbintenis verbintenis = getCollectiefAanmakenModel().getNieuweVerbintenis();

		verbintenis.setStatus(VerbintenisStatus.Voorlopig);
		verbintenis.nieuwePlaatsing();
	}

	private void createPlaatsingOfVerbintenisSelectieChoice(String id)
	{
		AjaxRadioChoice<Class< ? extends Entiteit>> choice =
			new AjaxRadioChoice<Class< ? extends Entiteit>>(id,
				new PropertyModel<Class< ? extends Entiteit>>(getDefaultModel(), "soort"),
				getKeuzesAanmaken(), new IChoiceRenderer<Class< ? extends Entiteit>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(Class< ? extends Entiteit> object)
					{
						return object.getSimpleName();
					}

					@Override
					public String getIdValue(Class< ? extends Entiteit> object, int index)
					{
						return object.getName();
					}
				})
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, Object newSelection)
				{
					target.addComponent(plaatsingPanel);
					target.addComponent(verbintenisPanel);
				}
			};

		form.add(choice);
	}

	private List<Class< ? extends Entiteit>> getKeuzesAanmaken()
	{
		List<Class< ? extends Entiteit>> ret = new ArrayList<Class< ? extends Entiteit>>();
		ret.add(Verbintenis.class);
		ret.add(Plaatsing.class);
		return ret;
	}

	private void createNieuwVerbintenisPanel(String id)
	{
		verbintenisPanel =
			new CollectiefVerbintenisAanmakenPanel(id, (CollectiefAanmakenModel) getDefaultModel(),
				form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && isVerbintenisPanelZichtbaar();
				}

				@Override
				protected IModel<Boolean> getOnderwijsproductAfnamesAanmakenModel()
				{
					return new PropertyModel<Boolean>(
						CollectiefAanmakenPage.this.getDefaultModel(),
						"onderwijsproductAfnamesAanmaken");
				}
			};
		verbintenisPanel.setOutputMarkupPlaceholderTag(true);

		form.add(verbintenisPanel);
	}

	private void createNieuwPlaatsingPanel(String id)
	{
		plaatsingPanel =
			new CollectiefPlaatsingAanmakenPanel(id, new PropertyModel<Plaatsing>(
				getDefaultModel(), id), form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && isPlaatsingPanelZichtbaar();
				}
			};
		plaatsingPanel.setOutputMarkupPlaceholderTag(true);

		form.add(plaatsingPanel);
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
				if (Verbintenis.class.equals(getSoort()))
				{
					setResponsePage(new CollectiefAanmakenDeelnemerSelectiePage(
						getCollectiefAanmakenModel(), CollectiefAanmakenPage.this));
				}
				else if (Plaatsing.class.equals(getSoort()))
				{
					setResponsePage(new CollectiefAanmakenVerbintenisSelectiePage(
						getCollectiefAanmakenModel(), CollectiefAanmakenPage.this));
				}
			}
		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, CollectiefAanmakenOverzichtPage.class));
	}

	private boolean isPlaatsingPanelZichtbaar()
	{
		return (getCollectiefAanmakenModel().getSoort().equals(Plaatsing.class));
	}

	private boolean isVerbintenisPanelZichtbaar()
	{
		return (getCollectiefAanmakenModel().getSoort().equals(Verbintenis.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Aanmaken);
	}

	private Class< ? extends Entiteit> getSoort()
	{
		return getCollectiefAanmakenModel().getSoort();
	}

	private CollectiefAanmakenModel getCollectiefAanmakenModel()
	{
		return (CollectiefAanmakenModel) getDefaultModel();
	}
}