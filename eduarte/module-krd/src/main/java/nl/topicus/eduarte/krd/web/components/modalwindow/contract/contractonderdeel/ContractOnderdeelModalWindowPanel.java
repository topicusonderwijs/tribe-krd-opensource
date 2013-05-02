package nl.topicus.eduarte.krd.web.components.modalwindow.contract.contractonderdeel;

import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.krd.web.validators.SomOnderdelenValidator;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het bewerken van een {@link ContractOnderdeel} in een modal window.
 * 
 * @author hoeve
 */
public class ContractOnderdeelModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<ContractOnderdeel>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<ContractOnderdeel> inputfields;

	private IModel<Contract> contractModel;

	private IsInburgeringModel isInburgeringModel = new IsInburgeringModel();

	public ContractOnderdeelModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<ContractOnderdeel> modalWindow,
			ContractOnderdeelEditPanel contractPanel, IModel<Contract> contractModel)
	{
		super(id, modalWindow, contractPanel);
		this.contractModel = contractModel;

		inputfields = new AutoFieldSet<ContractOnderdeel>("inputfields", modalWindow.getModel());
		inputfields.setOutputMarkupId(true);
		inputfields.setPropertyNames(Arrays
			.asList("naam", "minimumAantalDeelnemers", "maximumAantalDeelnemers", "prijs",
				"begindatum", "einddatum", "frequentieAanwezigheid", "groepsgrootte",
				"begeleidingsintensiteit", "studiebelasting"));
		inputfields.setRenderMode(RenderMode.EDIT);
		inputfields.addFieldModifier(new LabelModifier("begindatum", "Geldig vanaf"));
		inputfields.addFieldModifier(new LabelModifier("einddatum", "Geldig tot"));
		inputfields
			.addFieldModifier(new VisibilityModifier(isInburgeringModel, "frequentieAanwezigheid",
				"groepsgrootte", "begeleidingsintensiteit", "studiebelasting"));

		inputfields.addModifier("prijs", new SomOnderdelenValidator(contractModel));

		getFormContainer().add(inputfields);

		createComponents();
	}

	private final class IsInburgeringModel extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean getObject()
		{
			return getContract().isInburgering();
		}
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		DatumField begindatumField = (DatumField) inputfields.findFieldComponent("begindatum");
		DatumField einddatumField = (DatumField) inputfields.findFieldComponent("einddatum");

		getForm().add(
			new BegindatumVoorEinddatumValidator(begindatumField, einddatumField,
				"BegindatumVoorEinddatumValidatorOnd.error"));

		Date begindatumContractDate = getContract().getBegindatum();
		Date einddatumContractDate = getContract().getEinddatum();

		if (einddatumContractDate != null)
		{
			getForm().add(
				new DatumKleinerOfGelijkDatumValidator("Geldig vanaf", begindatumField,
					einddatumContractDate));
			getForm().add(
				new DatumKleinerOfGelijkDatumValidator("Geldig Tot", einddatumField,
					einddatumContractDate));
		}

		if (begindatumContractDate != null)
		{
			getForm().add(
				new DatumGroterOfGelijkDatumValidator("Geldig vanaf", begindatumField,
					begindatumContractDate));
		}

	}

	public Contract getContract()
	{
		return contractModel.getObject();
	}
}
