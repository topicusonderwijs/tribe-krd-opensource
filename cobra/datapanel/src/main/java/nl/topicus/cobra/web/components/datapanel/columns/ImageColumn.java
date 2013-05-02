/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.io.Serializable;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

/**
 * @author Remco
 */
public class ImageColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final ImageFactory<T> image;

	public ImageColumn(String id, String header, ImageFactory<T> image)
	{
		this(id, header, null, image);
	}

	public ImageColumn(String id, String header, String sortProperty, ImageFactory<T> image)
	{
		super(id, header, sortProperty);
		this.image = image;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new ImagePanel(componentId, rowModel, image));
	}

	private final class ImagePanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public ImagePanel(String id, final IModel<T> rowModel, ImageFactory<T> image)
		{
			super(id, rowModel);
			add(image.getImage("img", rowModel));
		}

		@Override
		public boolean isEnabled()
		{
			return super.isEnabled() && ImageColumn.this.isContentsEnabled(getModel());
		}

		@Override
		public boolean isVisible()
		{
			return super.isVisible() && ImageColumn.this.isContentsVisible(getModel());
		}
	}

	/**
	 * Factory om een {@link Image} te genereren gebaseerd op willekeurige data.
	 * img.add(new SimpleAttributeModifier("src", data.getObject())); img.add(new
	 * SimpleAttributeModifier("title", "foobar")); </code>
	 * 
	 * @author Remco
	 */
	public static interface ImageFactory<T> extends Serializable
	{
		/**
		 * Mak een image.
		 * 
		 * @param id
		 *            id die het image moet krijgen
		 * @param data
		 *            row data
		 * @return image, nooit null
		 */
		public Image getImage(String id, IModel<T> data);
	}
}
