package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

/**
 * Opslaan button die via Ajax werkt.
 * 
 * @author loite
 */
public abstract class AjaxOpslaanButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private Form< ? > formToSubmit;

	private boolean makeDefault;

	public AjaxOpslaanButton(BottomRowPanel bottomRow, Form< ? > form)
	{
		super(bottomRow, "Opslaan", CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.formToSubmit = form;
	}

	public AjaxOpslaanButton(BottomRowPanel bottomRow, Form< ? > form, String label)
	{
		super(bottomRow, label, CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.formToSubmit = form;
	}

	public AjaxOpslaanButton(BottomRowPanel bottomRow, Form< ? > form, String label,
			CobraKeyAction cobraKeyAction)
	{
		super(bottomRow, label, cobraKeyAction, ButtonAlignment.RIGHT);
		this.formToSubmit = form;
	}

	public AjaxOpslaanButton setMakeDefault(boolean makeDefault)
	{
		this.makeDefault = makeDefault;
		return this;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		AjaxSubmitLink ret = new AjaxSubmitLink(linkId, formToSubmit)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				AjaxOpslaanButton.this.onSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > form)
			{
				AjaxOpslaanButton.this.onError(target, form);
			}

		};
		if (makeDefault)
			formToSubmit.setDefaultButton(ret);
		return ret;
	}

	public Form< ? > getForm()
	{
		return formToSubmit;
	}

	protected abstract void onSubmit(AjaxRequestTarget target, Form< ? > form);

	protected abstract void onError(AjaxRequestTarget target, Form< ? > form);
}
