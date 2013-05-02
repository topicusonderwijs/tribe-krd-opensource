package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Column voor genereren van een rapportage. Deze column zal je doorsturen naar een
 * selectie pagina. De abstract functie moet een link terug geven zodat hiernaartoe kan
 * worden genavigeerd.
 * 
 * @author hoeve
 */
public abstract class MultiEntityRapportageGenereerLinkColumn<T extends DocumentTemplate> extends
		AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private DocumentTemplateType eindType;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Id van de column (niet het wicket id)
	 * @param header
	 *            De tekst van de kolomheader.
	 */
	public MultiEntityRapportageGenereerLinkColumn(String id, String header)
	{
		super(id, header);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Id van de column (niet het wicket id)
	 * @param header
	 *            De tekst van de kolomheader.
	 * @param eindType
	 *            het eind type van het document. bv rtf -> pdf.
	 */
	public MultiEntityRapportageGenereerLinkColumn(String id, String header,
			DocumentTemplateType eindType)
	{
		super(id, header);
		this.eindType = eindType;
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		DocumentTemplate template = rowModel.getObject();

		LinkPanel container = new LinkPanel(componentId);
		if (template.isValid() && template.getZzzBestand() != null
			&& template.getZzzBestand().length > 0)
		{
			Image image = new Image("image");
			DocumentTemplateType type = eindType;
			if (type == null)
				type = template.getType();

			if (type == DocumentTemplateType.DOCX || type == DocumentTemplateType.DOCM
				|| type == DocumentTemplateType.DOTX || type == DocumentTemplateType.DOTM)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/word2.gif")));
			else if (type == DocumentTemplateType.JRXML)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/printPDF.png")));
			else if (type == DocumentTemplateType.PDF)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/printPDF.png")));
			else if (type == DocumentTemplateType.RTF)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/word2.gif")));
			else if (type == DocumentTemplateType.XLS)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/excel.gif")));
			else if (type == DocumentTemplateType.XLSX)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/excel.gif")));
			else if (type == DocumentTemplateType.CSV)
				image.add(new AttributeModifier("src", true, new Model<String>(EduArteRequestCycle
					.get().getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/excel.gif")));

			container.add(getRedirectLink("link", rowModel).add(image));
		}
		else
		{
			// geen bestand en geen link ==> toon niets
			container.add(new DummyLink("link").add(new Image("image")));
			container.setVisible(false);
		}

		cellItem.add(container);
	}

	protected abstract Link< ? > getRedirectLink(String id, final IModel<T> rowModel);

	public DocumentTemplateType getEindType()
	{
		return eindType;
	}

	private static final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id)
		{
			super(id);
		}
	}
}
