package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.ImageColumn;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

public class OrganisatieEenheidInheritanceColumn<T extends OrganisatieEenheidProvider> extends
		ImageColumn<T>
{

	private static final long serialVersionUID = 1L;

	private OrganisatieEenheidProvider orgEhdProvider;

	public OrganisatieEenheidInheritanceColumn(String id, OrganisatieEenheidProvider orgEhdProvider)
	{
		super(id, "", null, new InheritedImageFactory<T>(orgEhdProvider));
		this.orgEhdProvider = orgEhdProvider;
	}

	private static class InheritedImageFactory<T extends OrganisatieEenheidProvider> implements
			ImageFactory<T>
	{
		private static final long serialVersionUID = 1L;

		private OrganisatieEenheidProvider orgEhdProvider;

		public InheritedImageFactory(OrganisatieEenheidProvider orgEhdProvider)
		{
			this.orgEhdProvider = orgEhdProvider;
		}

		@Override
		public Image getImage(String id, IModel<T> data)
		{
			OrganisatieEenheid objectOE = data.getObject().getOrganisatieEenheid();
			OrganisatieEenheid filterOE = orgEhdProvider.getOrganisatieEenheid();
			Image ret = new Image(id);
			String imageName;
			if (filterOE != null && filterOE.isParentOf(objectOE))
			{
				imageName = "related.png";
				ret.add(new SimpleAttributeModifier("title",
					"Is beschikbaar in een lagergelegen organisatie-eenheid"));
			}
			else
			{
				imageName = "inherited.png";
				ret.add(new SimpleAttributeModifier("title",
					"Wordt geerfd van een hogergelegen organisatie-eenheid"));
			}
			ret.add(new SimpleAttributeModifier("src", RequestCycle.get().getRequest()
				.getRelativePathPrefixToContextRoot()
				+ "assets/img/icons/" + imageName));

			ret.add(new SimpleAttributeModifier("alt", "test"));
			ret.setVisible(filterOE != null && !objectOE.equals(filterOE));
			return ret;
		}
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(orgEhdProvider);
	}

	@Override
	public String getCssClass()
	{
		return "unit_30";
	}
}
