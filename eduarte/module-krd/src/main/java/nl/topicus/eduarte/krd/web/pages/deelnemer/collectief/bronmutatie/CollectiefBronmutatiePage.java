package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
@PageInfo(title = "Collectief bronmutatie aanmaken", menu = {"Deelnemer > Collectief > BRON-mutaties"})
@InPrincipal(App.class)
public class CollectiefBronmutatiePage extends SecurePage
{
	private Form<Void> form;

	public CollectiefBronmutatiePage()
	{
		this(new CollectiefBronmutatieModel());
	}

	public CollectiefBronmutatiePage(CollectiefBronmutatieModel mutatieModel)
	{
		super(mutatieModel, CoreMainMenuItem.Deelnemer);

		form = new Form<Void>("form");
		List<Enum< ? >> choices = new ArrayList<Enum< ? >>();
		choices.add(SoortMutatie.Aanpassing);
		choices.add(nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.Aanpassing);
		AbstractAjaxDropDownChoice<Enum< ? >> dropdown =
			new AbstractAjaxDropDownChoice<Enum< ? >>("soortMutatie", new PropertyModel<Enum< ? >>(
				getDefaultModel(), "soortMutatie"), choices, new IChoiceRenderer<Enum< ? >>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(Enum< ? > object)
				{
					if (object instanceof SoortMutatie)
					{
						return object.toString() + " (VO)";
					}
					if (object instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
					{
						return object.toString() + " (BVE)";
					}
					return object.toString();
				}

				@Override
				public String getIdValue(Enum< ? > object, int index)
				{
					return String.valueOf(index);
				}

			})
			{
				private static final long serialVersionUID = 1L;
			};
		dropdown.setRequired(true);
		form.add(dropdown);
		add(form);

		createComponents();
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
				setResponsePage(new CollectiefBronmutatieSelectiePage(
					(CollectiefBronmutatieModel) CollectiefBronmutatiePage.this.getDefaultModel(),
					CollectiefBronmutatiePage.this));
			}
		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, CollectiefBronmutatieOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BronMutatiesAanmaken);
	}
}
