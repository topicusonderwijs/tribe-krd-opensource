package nl.topicus.eduarte.web.components.modalwindow.contract;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class MyContractVerplichtingModalWindow extends ModalWindowBasePanel<ContractVerplichting>
{

	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private IModel<ContractVerplichting> contractVerplichtingModel;

	public MyContractVerplichtingModalWindow(String id,
			IModel<ContractVerplichting> contractVerplichtingModel,
			CobraModalWindow<ContractVerplichting> window)
	{
		super(id, window);
		ContractVerplichting converpl = (contractVerplichtingModel.getObject());
		Contract contract = converpl.getContract();
		window.setTitle("" + contract.getNaam() + " - " + contract.getCode());
		this.contractVerplichtingModel = contractVerplichtingModel;
		AutoFieldSet<ContractVerplichting> inputFields =
			new AutoFieldSet<ContractVerplichting>("inputfields", contractVerplichtingModel);
		inputFields.setOutputMarkupId(true);
		inputFields.setRenderMode(RenderMode.EDIT);
		inputFields.setPropertyNames(Arrays.asList("omschrijving", "medewerker", "begindatum",
			"einddatum", "uitgevoerd", "datumUitgevoerd"));
		inputFields.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(
			contractVerplichtingModel, "uitgevoerd"), "datumUitgevoerd"));
		inputFields.addFieldModifier(new EnableModifier(false, "omschrijving", "medewerker",
			"begindatum", "einddatum"));
		inputFields
			.addFieldModifier(new EduArteAjaxRefreshModifier("uitgevoerd", "datumUitgevoerd"));
		inputFields.addFieldModifier(new LabelModifier("einddatum", "Deadline"));

		form = new Form<Void>("form");
		form.add(inputFields);
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > formulier)
			{
				ContractVerplichting cvm = contractVerplichtingModel.getObject();
				cvm.saveOrUpdate();
				cvm.commit();
				ModalWindow.closeCurrent(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > formulier)
			{
				refreshFeedback(target);

			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Annuleren", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				ModalWindow.closeCurrent(target);
			}
		});
	}

}
