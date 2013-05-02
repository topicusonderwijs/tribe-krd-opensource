package nl.topicus.eduarte.web.components.panels.bijlage;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class BijlageToevoegenBewerkenPanel<E extends BijlageEntiteit, T extends IBijlageKoppelEntiteit<E>>
		extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	private static final PopupSettings POPUP =
		new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.MENU_BAR
			| PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS | PopupSettings.STATUS_BAR
			| PopupSettings.TOOL_BAR);

	private EnumCombobox<TypeBijlage> type;

	private FileUploadField bestandField;

	private TextField<String> linkField;

	private WebMarkupContainer refreshContainer;

	public BijlageToevoegenBewerkenPanel(String id, IModel<T> model)
	{
		super(id, model);
		final Form<Void> bijlageForm = new Form<Void>("bijlagenForm");
		bijlageForm.setOutputMarkupId(true);
		add(bijlageForm);

		ListView<BijlageEntiteit> bijlagen =
			new ListView<BijlageEntiteit>("bijlagen", new PropertyModel<List<BijlageEntiteit>>(
				model, "bijlagen"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<BijlageEntiteit> item)
				{
					TextField<String> bijlageOmschrijving =
						new TextField<String>("omschrijving", new PropertyModel<String>(item
							.getModel(), "bijlage.omschrijving"));
					ComponentUtil.fixLength(bijlageOmschrijving, Bijlage.class);
					item.add(bijlageOmschrijving);

					Bijlage bijlage = (item.getModelObject()).getBijlage();
					if (bijlage.getBestand() != null)
					{
						BijlageLink<Bijlage> link =
							new BijlageLink<Bijlage>("link", new PropertyModel<Bijlage>(item
								.getModel(), "bijlage"));
						item.add(link);
					}
					else if (bijlage.getLink() != null && !bijlage.getLink().isEmpty())
					{
						ExternalLink el = new ExternalLink("link", bijlage.getLink());
						el.add(new Label("label", bijlage.getLink()));
						el.setPopupSettings(POPUP);
						el.add(new SimpleAttributeModifier("title",
							"Link openen in een nieuw venster"));
						item.add(el);
					}

					item.add(new ConfirmationAjaxLink<Void>("delete",
						"Weet u zeker dat u deze bijlage wilt verwijderen?")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							removeBijlage(item.getModelObject());
							target.addComponent(bijlageForm);
						}
					});
				}
			};
		bijlageForm.add(bijlagen);

		refreshContainer = new WebMarkupContainer("refreshContainer");
		refreshContainer.setOutputMarkupId(true);
		bijlageForm.add(refreshContainer);

		type =
			new EnumCombobox<TypeBijlage>("type", new Model<TypeBijlage>(TypeBijlage.Bestand),
				true, TypeBijlage.Bestand, TypeBijlage.Link)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, TypeBijlage newSelection)
				{
					target.addComponent(refreshContainer);
				}
			};
		type.setNullValid(false);
		refreshContainer.add(type);

		bestandField = new FileUploadField("bestand", new Model<FileUpload>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && TypeBijlage.Bestand.equals(type.getModelObject());
			}
		};
		refreshContainer.add(bestandField);

		linkField = new TextField<String>("link", new Model<String>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && TypeBijlage.Link.equals(type.getModelObject());
			}
		};
		refreshContainer.add(linkField);

		bijlageForm.add(new SubmitLink("upload")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				Bijlage newBijlage = new Bijlage();
				newBijlage.setTypeBijlage(type.getModelObject());

				if (TypeBijlage.Bestand.equals(newBijlage.getTypeBijlage()))
				{
					FileUpload upload = bestandField.getFileUpload();
					if (upload == null)
					{
						error("Selecteer een bestand.");
						return;
					}
					newBijlage.setBestandsnaam(upload.getClientFileName());
					newBijlage.setBestand(upload.getBytes());
				}
				if (TypeBijlage.Link.equals(newBijlage.getTypeBijlage()))
				{
					if (StringUtil.isEmpty(linkField.getDefaultModelObjectAsString()))
					{
						error("Geef een link op.");
						return;
					}
					newBijlage.setLink(linkField.getDefaultModelObjectAsString());
					linkField.setModelObject(null);
				}

				getEntiteit().addBijlage(newBijlage);
			}
		});
	}

	protected void removeBijlage(BijlageEntiteit bijlageEntiteit)
	{
		getEntiteit().getBijlagen().remove(bijlageEntiteit);
	}

	private T getEntiteit()
	{
		return getModelObject();
	}
}
