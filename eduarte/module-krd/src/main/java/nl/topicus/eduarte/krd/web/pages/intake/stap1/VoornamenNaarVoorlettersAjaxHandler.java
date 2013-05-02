package nl.topicus.eduarte.krd.web.pages.intake.stap1;

import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;

public class VoornamenNaarVoorlettersAjaxHandler extends HoofdletterAjaxHandler
{
	private static final long serialVersionUID = 1L;

	private FormComponent<String> voorlettersField;

	public VoornamenNaarVoorlettersAjaxHandler()
	{
		super(HoofdletterMode.ElkWoord);
	}

	public void setVoorlettersField(FormComponent<String> voorlettersField)
	{
		this.voorlettersField = voorlettersField;
		this.voorlettersField.setOutputMarkupId(true);
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		super.onUpdate(target);

		String voornamen = getComponent().getDefaultModelObjectAsString();
		String voorletters = StringUtil.genereerVoorletters(voornamen);
		if (!StringUtil.equalOrBothEmpty(voorletters, voorlettersField
			.getDefaultModelObjectAsString()))
		{
			voorlettersField.setModelObject(voorletters);
			target.addComponent(voorlettersField);
			// Als de focus niet gezet wordt, verdwijnt de focus. Zet daarom de focus op
			// de voorletters. Known issue: bij shift-tab is het onhandig dat de
			// voorletters de focus krijgen.
			target.appendJavascript("document.getElementById('" + voorlettersField.getMarkupId()
				+ "').select()");
			target.focusComponent(voorlettersField);
		}
	}
}
