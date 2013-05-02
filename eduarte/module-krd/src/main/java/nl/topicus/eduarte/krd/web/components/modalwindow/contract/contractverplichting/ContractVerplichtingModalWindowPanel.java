package nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractverplichting;

import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina voor het bewerken van een {@link ContractVerplichting} in een modal window.
 * 
 * @author hoeve
 */
public class ContractVerplichtingModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<ContractVerplichting>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<ContractVerplichting> inputFields;

	private IModel<Contract> contractModel;

	public ContractVerplichtingModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<ContractVerplichting> modalWindow,
			ContractVerplichtingEditPanel contractVerplichtingPanel, IModel<Contract> contractModel)
	{
		super(id, modalWindow, contractVerplichtingPanel);
		this.contractModel = contractModel;

		inputFields = new AutoFieldSet<ContractVerplichting>("inputfields", modalWindow.getModel());
		inputFields.setOutputMarkupId(true);
		inputFields.setRenderMode(RenderMode.EDIT);
		inputFields.setPropertyNames(Arrays.asList("omschrijving", "medewerker", "begindatum",
			"einddatum", "uitgevoerd", "datumUitgevoerd"));
		inputFields.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(modalWindow
			.getModel(), "uitgevoerd"), "datumUitgevoerd"));
		inputFields
			.addFieldModifier(new EduArteAjaxRefreshModifier("uitgevoerd", "datumUitgevoerd"));
		inputFields.addFieldModifier(new LabelModifier("einddatum", "Deadline"));
		getFormContainer().add(inputFields);
		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		DatumField begindatumField = (DatumField) inputFields.findFieldComponent("begindatum");
		DatumField einddatumField = (DatumField) inputFields.findFieldComponent("einddatum");
		getForm().add(
			new BegindatumVoorEinddatumValidator(begindatumField, einddatumField,
				"BegindatumVoorEinddatumValidatorVerpl.error"));

		Date begindatumContractDate = getContract().getBegindatum();
		Date einddatumContractDate = getContract().getEinddatum();

		if (einddatumContractDate != null)
		{
			getForm().add(
				new DatumKleinerOfGelijkDatumValidator("Begindatum", begindatumField,
					einddatumContractDate));
			getForm().add(
				new DatumKleinerOfGelijkDatumValidator("Deadline", einddatumField,
					einddatumContractDate));
		}

		if (begindatumContractDate != null)
		{
			getForm().add(
				new DatumGroterOfGelijkDatumValidator("Begindatum", begindatumField,
					begindatumContractDate));
		}
	}

	@Override
	public void fillCopiedT(ContractVerplichting modelObject)
	{
		modelObject.setOmschrijving(((TextField< ? >) inputFields
			.findFieldComponent("omschrijving")).getInput());
	}

	public Contract getContract()
	{
		return contractModel.getObject();
	}
}
