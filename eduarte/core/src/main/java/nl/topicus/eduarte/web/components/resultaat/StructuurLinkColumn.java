package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.web.components.factory.StructuurLinkFactory;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public abstract class StructuurLinkColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public StructuurLinkColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public StructuurLinkColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		cell.add(new LinkPanel(componentId, rowModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && StructuurLinkColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && StructuurLinkColumn.this.isContentsVisible(rowModel);
			}
		}.setRenderBodyOnly(true));
	}

	protected abstract Resultaatstructuur getResultaatstructuur(IModel<T> rowModel);

	private class LinkPanel extends TypedPanel<Resultaatstructuur>
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, final IModel<T> model)
		{
			super(id, ModelFactory.getModel(getResultaatstructuur(model)));

			Resultaatstructuur structuur = getResultaatstructuur(model);
			Link< ? > structuurLink;
			if (structuur == null)
			{
				structuurLink = new DummyLink("structuurLink");
				structuurLink.setEnabled(false);
			}
			else
			{
				structuurLink =
					EduArteApp.get().getFirstPanelFactory(StructuurLinkFactory.class,
						EduArteContext.get().getOrganisatie()).createStructuurLink("structuurLink",
						structuur, this);
			}
			structuurLink.setBeforeDisabledLink("");
			structuurLink.setAfterDisabledLink("");
			add(structuurLink);

			structuurLink.add(new Label("label", createLabelModel(model)).setRenderBodyOnly(true));
		}
	}
}
