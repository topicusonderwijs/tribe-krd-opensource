/*
 * $Id: DatumField.java,v 1.3 2007-11-30 11:51:55 loite Exp $
 * $Revision: 1.3 $
 * $Date: 2007-11-30 11:51:55 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import java.util.Date;

import nl.topicus.cobra.providers.DatumProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.datepicker.DatePickerJavaScriptResourceReference;
import org.odlabs.wiquery.ui.datepicker.DatePickerLanguageResourceReference;
import org.odlabs.wiquery.ui.effects.CoreEffectJavaScriptResourceReference;
import org.odlabs.wiquery.ui.effects.DropEffectJavaScriptResourceReference;

/**
 * @author loite
 */
public class DatumField extends DatePicker<Date> implements DatumProvider
{
	public static String IMAGE_PATH = "assets/img/icons/calendar2.png";

	private final class DatumFieldAjaxHandler extends AjaxFormComponentUpdatingBehavior
	{
		private static final long serialVersionUID = 1L;

		public DatumFieldAjaxHandler()
		{
			super("onchange");
		}

		@Override
		protected void onUpdate(AjaxRequestTarget target)
		{
			target.addComponent(DatumField.this);
			DatumField.this.onUpdate(target, getModelObject());
		}

		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator()
		{
			return DatumField.this.getAjaxCallDecorator();
		}

	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, Date newValue)
	{
	}

	private static final long serialVersionUID = 6883784609679074032L;

	public DatumField(String id)
	{
		this(id, null);
	}

	public DatumField(String id, IModel<Date> model)
	{
		super(id, model, Date.class);
		setOutputMarkupId(true);
		add(new DatumFieldAjaxHandler());
		setDateFormat("dd-mm-yy");

		// $("#datepicker").datepicker({showOn: 'button', buttonImage:
		// 'images/calendar.gif', buttonImageOnly: true});
	}

	@Override
	public Options getOptions()
	{
		Options options = super.getOptions();
		options.put("buttonImageOnly", true);
		options.put("showAnim", "'fadeIn'");
		options.put("showOn", "'button'");
		options.put("duration", "'fast'");
		options.put("showButtonPanel", true);
		options.put("buttonImage", "'" + getRequest().getRelativePathPrefixToContextRoot()
			+ IMAGE_PATH + "'");
		return options;
	}

	@Override
	public Date getDatum()
	{
		return getModelObject();
	}

	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return null;
	}

	@Override
	public JsStatement statement()
	{
		JsStatement ret =
			new JsQuery().$("#" + getMarkupId() + " + img.ui-datepicker-trigger").chain("remove");
		ret =
			ret.append(";"
				+ new JsQuery().$("#" + getMarkupId() + ":enabled").chain("datepicker",
					getOptions().getJavaScriptOptions()).render());
		return ret;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		super.contribute(resourceManager);
		resourceManager.addJavaScriptResource(CoreEffectJavaScriptResourceReference.get());
		resourceManager.addJavaScriptResource(DropEffectJavaScriptResourceReference.get());
		resourceManager.addJavaScriptResource(DatePickerJavaScriptResourceReference.get());
		resourceManager.addJavaScriptResource(new DatePickerLanguageResourceReference(getLocale()));
	}
}
