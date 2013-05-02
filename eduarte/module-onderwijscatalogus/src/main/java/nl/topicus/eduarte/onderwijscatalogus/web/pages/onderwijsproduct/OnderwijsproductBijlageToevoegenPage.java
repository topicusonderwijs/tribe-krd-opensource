package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductBijlage;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductBijlagenWrite;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;
import nl.topicus.eduarte.web.validators.BestandValidator;
import nl.topicus.eduarte.web.validators.LinkValidator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Pagina voor toevoegen van een bijlage aan een onderwijsproduct
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct bijlage toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Bijlagen > Toevoegen"})
@InPrincipal(OnderwijsproductBijlagenWrite.class)
public class OnderwijsproductBijlageToevoegenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<OnderwijsproductBijlage>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private final WebMarkupContainer bestandContainer;

	private final WebMarkupContainer linkContainer;

	private final VersionedForm<Bijlage> form;

	private IModel<OnderwijsproductBijlage> onderwijsproductBijlageModel;

	private Bijlage getDefaultBijlage()
	{
		Bijlage bijlage = new Bijlage();
		bijlage.setTypeBijlage(TypeBijlage.Bestand);
		return bijlage;
	}

	public OnderwijsproductBijlageToevoegenPage(OnderwijsproductBijlage onderwijsproductBijlage,
			SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Bijlagen, ModelFactory.getModel(onderwijsproductBijlage
			.getOnderwijsproduct()));
		onderwijsproductBijlageModel =
			ModelFactory.getModel(onderwijsproductBijlage, new DefaultModelManager(
				OnderwijsproductBijlage.class));
		this.returnToPage = returnToPage;
		form =
			new VersionedForm<Bijlage>("form", ModelFactory.getCompoundModel(getDefaultBijlage(),
				new DefaultModelManager(Bijlage.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					Bijlage bijlage = getBijlage();
					if (bijlage.getTypeBijlage().equals(TypeBijlage.Bestand))
					{
						FileUpload upload =
							((FileUploadField) bestandContainer.get("bestand")).getFileUpload();
						bijlage.setBestand(upload.getBytes());
						bijlage.setBestandsnaam(upload.getClientFileName());
						upload.closeStreams();
					}
					else if (bijlage.getTypeBijlage().equals(TypeBijlage.Link))
					{
						String link = bijlage.getLink();
						String bestandsnaam;
						if (link.lastIndexOf('/') == -1)
						{
							bestandsnaam = link;
						}
						else
							bestandsnaam = link.substring(link.lastIndexOf('/') + 1);
						if (!bestandsnaam.isEmpty())
						{
							bijlage.setBestandsnaam(bestandsnaam);
						}
					}
					bijlage.save();
					OnderwijsproductBijlage ondprodBijlage =
						onderwijsproductBijlageModel.getObject();
					ondprodBijlage.setBijlage(bijlage);
					ondprodBijlage.getOnderwijsproduct().getBijlagen().add(ondprodBijlage);
					ondprodBijlage.save();
					ondprodBijlage.commit();
					setResponsePage(OnderwijsproductBijlageToevoegenPage.this.returnToPage);
				}
			};
		add(form);
		form.add(new RequiredTextField<String>("omschrijving"));

		final WebMarkupContainer parentContainer = new WebMarkupContainer("parentContainer");
		parentContainer.setOutputMarkupId(true);
		form.add(parentContainer);

		FileUploadField bestand = new FileUploadField("bestand", new Model<FileUpload>());
		bestandContainer = new WebMarkupContainer("bestandContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& TypeBijlage.Bestand.equals(getBijlage().getTypeBijlage());
			}
		};
		bestandContainer.add(bestand);
		parentContainer.add(bestandContainer);

		TextField<String> link = new TextField<String>("link");
		linkContainer = new WebMarkupContainer("linkContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && TypeBijlage.Link.equals(getBijlage().getTypeBijlage());
			}
		};
		linkContainer.add(link);
		parentContainer.add(linkContainer);

		RadioChoice<TypeBijlage> rc =
			new RadioChoice<TypeBijlage>("typeBijlage", Arrays.asList(TypeBijlage.values()));
		rc.add(new AjaxFormChoiceComponentUpdatingBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(parentContainer);
			}
		});
		form.add(rc);
		form.add(new BestandValidator(bestand, rc));
		form.add(new LinkValidator(link, rc));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(onderwijsproductBijlageModel);
		super.onDetach();
	}

	private Bijlage getBijlage()
	{
		return form.getModelObject();
	}
}
