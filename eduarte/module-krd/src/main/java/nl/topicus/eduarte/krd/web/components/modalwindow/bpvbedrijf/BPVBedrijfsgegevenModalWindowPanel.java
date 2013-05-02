package nl.topicus.eduarte.krd.web.components.modalwindow.bpvbedrijf;

import java.util.List;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven.BPVCodeHerkomst;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.factory.BPVModuleComponentFactory;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevens;
import nl.topicus.eduarte.web.components.factory.StagemarktServiceAdapter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;

/**
 * Pagina voor het bewerken van een {@link BPVBedrijfsgegeven} in een modal window.
 * 
 * @author hoeve
 */
public class BPVBedrijfsgegevenModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	public BPVBedrijfsgegevenModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<BPVBedrijfsgegeven> modalWindow,
			BPVBedrijfsgegevenEditPanel bedrijfsgegevenEditPanel)
	{
		super(id, modalWindow, bedrijfsgegevenEditPanel);

		final AutoFieldSet<BPVBedrijfsgegeven> gegevensAutoFieldSet =
			new AutoFieldSet<BPVBedrijfsgegeven>("gegevensAutoFieldSet", modalWindow.getModel(),
				"Nieuwe BPV-bedrijfsgegevens");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(false);
		gegevensAutoFieldSet.setPropertyNames("kenniscentrum", "relatienummer", "codeLeerbedrijf",
			"herkomstCode");
		gegevensAutoFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("kenniscentrum",
			"codeLeerbedrijf"));

		gegevensAutoFieldSet.addModifier("kenniscentrum", new OnChangeAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				BPVBedrijfsgegeven bedrijfsgegeven = getModalWindow().getModelObject();
				if (bedrijfsgegeven.getHerkomstCode() == BPVCodeHerkomst.Systeem)
				{
					genereerCodeLeerbedrijf(gegevensAutoFieldSet, target, bedrijfsgegeven);
				}
			}
		});
		gegevensAutoFieldSet.addModifier("codeLeerbedrijf", new OnChangeAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				BPVBedrijfsgegeven bedrijfsgegeven = getModalWindow().getModelObject();
				if (StringUtil.isNotEmpty(bedrijfsgegeven.getCodeLeerbedrijf()))
				{
					bedrijfsgegeven.setHerkomstCode(BPVCodeHerkomst.Invoer);
				}
				target.addComponent(gegevensAutoFieldSet.findFieldComponent("herkomstCode"));
			}
		});

		// gegevensAutoFieldSet.setFieldModifiers(new EditorClassModifier( ))

		gegevensAutoFieldSet.addModifier("herkomstCode",
			new AjaxFormChoiceComponentUpdatingBehavior()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					BPVBedrijfsgegeven bedrijfsgegeven = getModalWindow().getModelObject();
					if (BPVCodeHerkomst.Systeem.equals(bedrijfsgegeven.getHerkomstCode()))
					{
						genereerCodeLeerbedrijf(gegevensAutoFieldSet, target, bedrijfsgegeven);
					}
					else if (BPVCodeHerkomst.BRON.equals(bedrijfsgegeven.getHerkomstCode()))
					{
						bedrijfsgegeven.setCodeLeerbedrijf(null);
						target.addComponent(gegevensAutoFieldSet
							.findFieldComponent("codeLeerbedrijf"));
					}
					else if (BPVCodeHerkomst.COLO.equals(bedrijfsgegeven.getHerkomstCode()))
					{
						opvragenColoGegevens(gegevensAutoFieldSet, target, bedrijfsgegeven);
					}
				}
			});

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}

	private void genereerCodeLeerbedrijf(
			final AutoFieldSet<BPVBedrijfsgegeven> gegevensAutoFieldSet, AjaxRequestTarget target,
			BPVBedrijfsgegeven bedrijfsgegeven)
	{
		if (bedrijfsgegeven.getCodeLeerbedrijf() == null
			|| bedrijfsgegeven.getCodeLeerbedrijf().length() != 17)
		{
			bedrijfsgegeven.setCodeLeerbedrijf(bedrijfsgegeven.genereerSysteemCodeLeerbedrijf());
		}
		else
		{
			// Eerder ingevulde code
			// Alleen BRIN-nummer vervangen
			String code =
				bedrijfsgegeven.getKenniscentrum().getCode()
					+ bedrijfsgegeven.getCodeLeerbedrijf().substring(4);
			bedrijfsgegeven.setCodeLeerbedrijf(code);
		}
		target.addComponent(gegevensAutoFieldSet.findFieldComponent("codeLeerbedrijf"));
		target.addComponent(gegevensAutoFieldSet.findFieldComponent("herkomstCode"));
	}

	private List<StagemarktBedrijfsgegevens> getStagemartBedrijfgegevens(
			BPVBedrijfsgegeven bedrijfsgegeven)
	{
		List<StagemarktBedrijfsgegevens> result = null;

		List<BPVModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BPVModuleComponentFactory.class);
		if (factories.size() > 0)
		{
			StagemarktServiceAdapter adapter = factories.get(0).getStagemarktServiceAdapter();

			String bedrijfsnaam = null;
			Adres adres = null;
			String postcode = null;
			int huisnummer = 0;
			if (bedrijfsgegeven.getExterneOrganisatie().getPostAdres() != null)
			{
				adres = bedrijfsgegeven.getExterneOrganisatie().getPostAdres().getAdres();
				postcode = adres.getPostcode();
				huisnummer = Integer.parseInt(adres.getHuisnummer());
			}

			String brinKenniscentrum = null;
			if (bedrijfsgegeven.getKenniscentrum() != null)
			{
				bedrijfsgegeven.getKenniscentrum().getCode();
			}

			result =
				adapter.getBedrijfgegevens(bedrijfsnaam, postcode, huisnummer, brinKenniscentrum);
		}
		return result;
	}

	private void opvragenColoGegevens(final AutoFieldSet<BPVBedrijfsgegeven> gegevensAutoFieldSet,
			AjaxRequestTarget target, BPVBedrijfsgegeven bedrijfsgegeven)
	{
		if (bedrijfsgegeven.getCodeLeerbedrijf() == null)
		{
			StagemarktBedrijfsgegevens selectie = null;

			List<StagemarktBedrijfsgegevens> gegevens = getStagemartBedrijfgegevens(bedrijfsgegeven);

			if (gegevens.size() > 1)
			{
				// TODO: popup (die onder de COLO knop hergebruiken wanneer deze af is)
			}
			else if (gegevens.size() > 0)
			{
				selectie = gegevens.get(0);
			}

			if (selectie != null)
				bedrijfsgegeven.setCodeLeerbedrijf(selectie.getCodeleerbedrijf());
		}
		target.addComponent(gegevensAutoFieldSet.findFieldComponent("codeLeerbedrijf"));
		target.addComponent(gegevensAutoFieldSet.findFieldComponent("herkomstCode"));
	}
}