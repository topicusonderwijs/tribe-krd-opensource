package nl.topicus.eduarte.web.components.panels;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.link.DocumentTemplateDownloadLink;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class DocumentTemplateLinkPanel<T extends DocumentTemplate> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateLinkPanel(String id, IModel<T> templateModel)
	{
		super(id, templateModel);
		this.setRenderBodyOnly(true);

		Image image = new Image("image");
		image.add(new AttributeModifier("src", true, new Model<String>(getRequest()
			.getRelativePathPrefixToContextRoot()
			+ "assets/img/icons/attach.png")));

		add(new DocumentTemplateDownloadLink<T>("link", templateModel).add(image));
	}
}
