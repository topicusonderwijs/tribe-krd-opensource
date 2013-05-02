package nl.topicus.eduarte.krd.web.components.modalwindow.verbintenis.verbinteniscontract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.choice.ContractOnderdeelCombobox;
import nl.topicus.eduarte.web.components.choice.ExterneOrganisatieContactPersoonCombobox;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Pagina voor het toevoegen van een contract aan een verbintenis
 * 
 * @author vandekamp
 */
public class VerbintenisContractModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<VerbintenisContract>
{

	private static final class ContractDatumValidator extends AbstractValidator<Date>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Contract> contractModel;

		public ContractDatumValidator(IModel<Contract> iModel)
		{
			this.contractModel = iModel;
		}

		@Override
		protected void onValidate(IValidatable<Date> validatable)
		{
			if (contractModel != null && contractModel.getObject() != null)
			{
				if (validatable.getValue().compareTo(contractModel.getObject().getBegindatum()) < 0)
				{
					error(validatable);
				}
			}
		}

		@Override
		protected String resourceKey()
		{
			return "begindatum";
		}
	}

	private static final long serialVersionUID = 1L;

	private ContractSearchEditor contractField;

	private ExterneOrganisatieContactPersoonCombobox extOrgContactPersoonComboBox;

	public VerbintenisContractModalWindowPanel(String id,
			final AbstractToevoegenBewerkenModalWindow<VerbintenisContract> modalWindow,
			VerbintenisContractEditPanel verbintenisContractEditPanel)
	{
		super(id, modalWindow, verbintenisContractEditPanel);

		WebMarkupContainer beginEindDatumContainer = new WebMarkupContainer("beginEindDatum");
		getFormContainer().add(beginEindDatumContainer);
		DatumField beginDatum = new DatumField("begindatum");
		DatumField eindDatum = new DatumField("einddatum");
		beginEindDatumContainer.add(beginDatum.setRequired(true));
		beginEindDatumContainer.add(eindDatum);

		final ContractOnderdeelCombobox onderdelenCombobox =
			new ContractOnderdeelCombobox("onderdeel",
				new AbstractReadOnlyModel<List<ContractOnderdeel>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public List<ContractOnderdeel> getObject()
					{
						Contract obj = contractField.getModelObject();
						if (obj == null)
							return new ArrayList<ContractOnderdeel>();

						return obj.getContractOnderdelen();
					}
				});
		onderdelenCombobox.setAddSelectedItemToChoicesWhenNotInList(false);
		onderdelenCombobox.setOutputMarkupId(true);
		onderdelenCombobox.setNullValid(true);

		ContractZoekFilter contractZoekFilter = new ContractZoekFilter();
		// #0058130
		contractZoekFilter.setToonInactief(true);
		contractZoekFilter.setInschrijfdatumModel(new PropertyModel<Date>(modalWindow.getModel(),
			"begindatum"));
		contractField =
			new ContractSearchEditor("contract", new PropertyModel<Contract>(
				modalWindow.getModel(), "contract"), contractZoekFilter);
		contractField.setRequired(true);
		contractField.addListener(new ISelectListener()

		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				VerbintenisContract vContract = modalWindow.getModelObject();
				Contract contract = contractField.getModelObject();
				if (contract == null
					|| !contract.getContractOnderdelen().contains(vContract.getOnderdeel()))
				{
					vContract.setOnderdeel(null);
				}
				target.addComponent(onderdelenCombobox);
				vContract.setExterneOrganisatieContactPersoon(null);
				target.addComponent(extOrgContactPersoonComboBox);
			}
		});
		getFormContainer().add(contractField);

		extOrgContactPersoonComboBox =
			new ExterneOrganisatieContactPersoonCombobox("externeOrganisatieContactPersoon",
				new PropertyModel<ExterneOrganisatieContactPersoon>(modalWindow.getModel(),
					"externeOrganisatieContactPersoon"), getContactPersonenListModel(modalWindow));
		getFormContainer().add(extOrgContactPersoonComboBox);

		getFormContainer().add(onderdelenCombobox);
		getFormContainer().add(
			new TextField<String>("externNummer", new PropertyModel<String>(modalWindow.getModel(),
				"externNummer")));

		String description =
			"Extern nummer - Met het externe nummer kan geregistreerd worden onder welk nummer de deelnemer bekend is bij de externe organisatie waarvoor het contract geldt";
		WebMarkupContainer descriptionContainer = new WebMarkupContainer("description");
		descriptionContainer.add(new AttributeModifier("title", true,
			new Model<String>(description)));
		getFormContainer().add(descriptionContainer);
		getFormContainer().add(new DatumField("datumBeschikking"));
		beginDatum.add(new ContractDatumValidator(contractField.getModel()));
		getForm().add(new BegindatumVoorEinddatumValidator(beginDatum, eindDatum));

		createComponents();
	}

	private IModel<List<ExterneOrganisatieContactPersoon>> getContactPersonenListModel(
			final AbstractToevoegenBewerkenModalWindow<VerbintenisContract> modalWindow)
	{
		return new LoadableDetachableModel<List<ExterneOrganisatieContactPersoon>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ExterneOrganisatieContactPersoon> load()
			{
				if (modalWindow.getModelObject().getContract() != null
					&& modalWindow.getModelObject().getContract().getExterneOrganisatie() != null)
				{
					return modalWindow.getModelObject().getContract().getExterneOrganisatie()
						.getContactPersonen();
				}
				return new ArrayList<ExterneOrganisatieContactPersoon>();
			}
		};
	}
}
