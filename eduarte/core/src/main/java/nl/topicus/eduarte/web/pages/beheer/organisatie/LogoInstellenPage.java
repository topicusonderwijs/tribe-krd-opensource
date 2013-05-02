package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.dao.helpers.InstellingsLogoDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.organisatie.InstellingsLogo;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.Model;

@PageInfo(title = "Logo instellen", menu = "Beheer > Organisatie-eenheid -> Logo instellen")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class LogoInstellenPage extends AbstractBeheerPage<Void>
{
	private Form<Void> form;

	public LogoInstellenPage()
	{
		super(BeheerMenuItem.Organisatie_eenheden);
		form = new Form<Void>("form");
		form.add(new Image("logo", getImageResource()));
		form.add(new FileUploadField("bestand", new Model<FileUpload>()).setRequired(true));
		add(form);
		createComponents();
	}

	private Bijlage createAndSaveLogo(FileUpload upload)
	{
		Bijlage bijlage = new Bijlage();
		bijlage.setOmschrijving("Instellings logo");
		bijlage.setTypeBijlage(TypeBijlage.Bestand);
		bijlage.setBestand(upload.getBytes());
		bijlage.setBestandsnaam(upload.getClientFileName());
		bijlage.save();
		InstellingsLogo logo = new InstellingsLogo();
		logo.setLogo(bijlage);
		logo.save();
		bijlage.commit();
		return bijlage;
	}

	private DynamicImageResource getImageResource()
	{
		return new DynamicImageResource("jpg")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] getImageData()
			{
				InstellingsLogo logo = getLogo();
				if (logo == null || logo.getLogo().getBestand() == null)
					return new byte[0];
				return logo.getLogo().getBestand();
			}
		};
	}

	private InstellingsLogo getLogo()
	{
		InstellingsLogo logo =
			DataAccessRegistry.getHelper(InstellingsLogoDataAccessHelper.class)
				.getInstellingsLogo();
		return logo;
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
				FileUpload upload = ((FileUploadField) form.get("bestand")).getFileUpload();
				String extension = getExtension(upload.getClientFileName());
				if (extension.equals(".jpg") || extension.equals(".png")
					|| extension.equals(".bmp"))
				{
					InstellingsLogo logo = getLogo();
					if (logo != null)
					{
						Bijlage bijlage = logo.getLogo();
						bijlage.setBestand(upload.getBytes());
						bijlage.setBestandsnaam(upload.getClientFileName());
						bijlage.save();
						bijlage.commit();
					}
					else
					{
						createAndSaveLogo(upload);
					}
					upload.closeStreams();
					super.onSubmit();
				}
				else
					error("Bestandstype wordt niet ondersteund");
			}
		});
		panel.addButton(new TerugButton(panel, OrganisatieEenheidZoekenPage.class));
	}

	private String getExtension(String fileExt)
	{
		String ext = fileExt;
		int i = ext.lastIndexOf('.');
		if (i >= 0)
		{
			ext = ext.substring(i);
		}
		else
			ext = '.' + ext;
		return ext.toLowerCase();
	}

}
