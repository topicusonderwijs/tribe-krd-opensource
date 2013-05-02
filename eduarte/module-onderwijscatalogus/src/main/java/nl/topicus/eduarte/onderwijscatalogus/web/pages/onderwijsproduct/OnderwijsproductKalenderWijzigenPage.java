package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductKalenderWrite;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Pagina om de kalender te wijzigen
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct kalender bewerken", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Kalender > Bewerken"})
@InPrincipal(OnderwijsproductKalenderWrite.class)
public class OnderwijsproductKalenderWijzigenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private final VersionedForm<Onderwijsproduct> form;

	private final SecurePage returnToPage;

	public OnderwijsproductKalenderWijzigenPage(Onderwijsproduct onderwijsproduct,
			SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Kalender, ModelFactory.getCompoundChangeRecordingModel(
			onderwijsproduct, new DefaultModelManager(Onderwijsproduct.class)));
		this.returnToPage = returnToPage;
		form = new VersionedForm<Onderwijsproduct>("form", getContextOnderwijsproductModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				Onderwijsproduct product = getModelObject();
				product.saveOrUpdate();
				product.commit();
				setResponsePage(OnderwijsproductKalenderWijzigenPage.this.returnToPage);
			}
		};
		form.add(new TextArea<String>("kalender").add(StringValidator.maximumLength(ComponentUtil
			.findLengthInAnnotations(Onderwijsproduct.class, "kalender"))));
		form.add(new TextArea<String>("uitvoeringsfrequentie").add(StringValidator
			.maximumLength(ComponentUtil.findLengthInAnnotations(Onderwijsproduct.class,
				"uitvoeringsfrequentie"))));
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}
}
