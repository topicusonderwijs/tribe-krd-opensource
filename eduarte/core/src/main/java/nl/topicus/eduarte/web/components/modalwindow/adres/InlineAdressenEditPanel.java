package nl.topicus.eduarte.web.components.modalwindow.adres;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.web.components.panels.adresedit.AdresEditPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.tabs.Tabs;

public class InlineAdressenEditPanel<T extends AdresEntiteit<T>, A extends Adresseerbaar<T>>
		extends TypedPanel<A>
{
	private static final long serialVersionUID = 1L;

	private boolean postWoonVerschil;

	private IModel<T> woonModel;

	private IModel<T> postModel;

	InlineAdressenEditPanel(String id, IModel<A> entiteitModel)
	{
		super(id, entiteitModel);

		postWoonVerschil =
			!getEntiteit().getAdressen().isEmpty()
				&& !getEntiteit().getAdressen().get(0).isPostadres();
		woonModel = new PropertyModel<T>(entiteitModel, "adressen[0]");
		postModel = new PropertyModel<T>(entiteitModel, "adressen[1]");

		final Tabs tabs = new Tabs("adressen");
		add(tabs);

		final String fysiekStart =
			getEntiteit().getFysiekAdresOmschrijving().substring(0,
				getEntiteit().getFysiekAdresOmschrijving().length() - 5);

		Label woonLabel = new Label("woonLink", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				if (postWoonVerschil)
					return getEntiteit().getFysiekAdresOmschrijving();
				else
					return fysiekStart + "- en postadres/factuuradres";
			}
		});
		tabs.add(woonLabel);
		Label postLabel = new Label("postLink", "Postadres/factuuradres")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && postWoonVerschil;
			}
		};
		tabs.add(postLabel);

		AdresEditPanel<T> woonEditPanel =
			new AdresEditPanel<T>("woonEditPanel", woonModel, AdresEditPanel.Mode.INLINE);
		woonLabel.add(new SimpleAttributeModifier("href", "#" + woonEditPanel.getMarkupId()));
		woonEditPanel.setOutputMarkupId(true);
		tabs.add(woonEditPanel);

		AdresEditPanel<T> postEditPanel =
			new AdresEditPanel<T>("postEditPanel", postModel, AdresEditPanel.Mode.INLINE)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && postWoonVerschil;
				}
			};
		postLabel.add(new SimpleAttributeModifier("href", "#" + postEditPanel.getMarkupId()));
		postEditPanel.setOutputMarkupId(true);
		tabs.add(postEditPanel);

		AjaxCheckBox postWoonVerschilCheck =
			new AjaxCheckBox("postWoonVerschil", new PropertyModel<Boolean>(this,
				"postWoonVerschil"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					if (postWoonVerschil)
					{
						T post = getEntiteit().newAdres();
						post.setPostadres(true);
						post.setFactuuradres(true);
						getEntiteit().getAdressen().add(post);
					}
					else
					{
						getEntiteit().getAdressen().remove(1);
					}
					getEntiteit().getAdressen().get(0).setPostadres(!postWoonVerschil);
					getEntiteit().getAdressen().get(0).setFactuuradres(!postWoonVerschil);
					target.addComponent(tabs);
				}
			};
		add(postWoonVerschilCheck);

		add(new Label("woonBezoekLabel", fysiekStart));
	}

	@Override
	protected void onBeforeRender()
	{
		if (getEntiteit().getAdressen().isEmpty())
		{
			T newAdres = getEntiteit().newAdres();
			newAdres.setPostadres(true);
			newAdres.setFysiekadres(true);
			newAdres.setFactuuradres(true);
			getEntiteit().getAdressen().add(newAdres);
		}
		super.onBeforeRender();
	}

	private A getEntiteit()
	{
		return getModelObject();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();

		woonModel.detach();
		postModel.detach();
	}
}
