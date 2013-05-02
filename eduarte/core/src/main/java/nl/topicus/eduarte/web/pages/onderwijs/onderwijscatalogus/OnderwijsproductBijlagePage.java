package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductBijlagen;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductBijlage;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.DocumentenPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Koppeling van het onderwijsproduct met bijlagen
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct Bijlage", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Bijlagen"})
@InPrincipal(OnderwijsproductBijlagen.class)
public class OnderwijsproductBijlagePage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductBijlagePage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Bijlagen, ModelFactory.getModel(onderwijsproduct));

		DocumentenPanel<OnderwijsproductBijlage, Onderwijsproduct> bijlagenPanel =
			new DocumentenPanel<OnderwijsproductBijlage, Onderwijsproduct>("datapanel",
				getContextOnderwijsproductModel(), true);
		add(bijlagenPanel);

		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new ModuleEditPageButton<OnderwijsproductBijlage>(panel, "Toevoegen",
			CobraKeyAction.TOEVOEGEN, OnderwijsproductBijlage.class,
			OnderwijsproductBijlagePage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected OnderwijsproductBijlage getEntity()
			{
				Onderwijsproduct onderwijsproduct =
					(Onderwijsproduct) OnderwijsproductBijlagePage.this.getDefaultModelObject();
				OnderwijsproductBijlage onderwijsproductBijlage = new OnderwijsproductBijlage();
				onderwijsproductBijlage.setOnderwijsproduct(onderwijsproduct);
				return onderwijsproductBijlage;
			}
		});
	}
}
