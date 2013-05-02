package nl.topicus.eduarte.krd.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.filter.OnbeeindigdAdresFilter;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerPersonaliaWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.TeVerhuizenRelatiesSelectiePanel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.adresedit.AdresEditPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "Personalia Bewerken", menu = {"Deelnemer > [deelnemer] > Bewerken > verhuizen"})
@InPrincipal(DeelnemerPersonaliaWrite.class)
public class DeelnemerVerhuizenPage extends AbstractDeelnemerPage implements IEditPage
{
	private EditDeelnemerPersonaliaPage returnPage;

	private Form<Void> form;

	private IdBasedModelSelection<Relatie> selectie = new IdBasedModelSelection<Relatie>();

	private IModel<PersoonAdres> nieuwAdresModel;

	private IModel<List<Relatie>> relaties;

	private IChangeRecordingModel<Deelnemer> deelnemerModel;

	public DeelnemerVerhuizenPage(EditDeelnemerPersonaliaPage returnPage,
			IChangeRecordingModel<Deelnemer> deelnemerModel)
	{
		super(DeelnemerMenuItem.Personalia, deelnemerModel.getObject());
		this.deelnemerModel = deelnemerModel;
		this.returnPage = returnPage;

		relaties = new LoadableDetachableModel<List<Relatie>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Relatie> load()
			{
				return getRelatiesOpZelfdeAdres();
			}
		};
		for (Relatie curRelatie : getRelatiesOpZelfdeAdres())
			selectie.add(curRelatie);

		add(form = new Form<Void>("form"));

		form.add(new TeVerhuizenRelatiesSelectiePanel("relaties", selectie, relaties)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Persoon getPersoon()
			{
				return DeelnemerVerhuizenPage.this.getPersoon();
			}
		});

		PersoonAdres nieuwPersoonAdres = getPersoon().newAdres();
		nieuwPersoonAdres.setPostadres(true);
		nieuwPersoonAdres.setFysiekadres(true);
		nieuwPersoonAdres.setFactuuradres(true);
		nieuwAdresModel = deelnemerModel.getManager().getModel(nieuwPersoonAdres, null);

		form.add(new AdresEditPanel<PersoonAdres>("nieuwAdres", nieuwAdresModel,
			AdresEditPanel.Mode.ADRES_ONLY, "Het nieuwe adres"));

		PersoonAdres oudPersoonAdres = getEersteOnbeeindigdeAdres(getPersoon());
		AutoFieldSet<PersoonAdres> ingang =
			new AutoFieldSet<PersoonAdres>("ingang", nieuwAdresModel, "Overige gegevens");
		ingang.setRenderMode(RenderMode.EDIT);
		ingang.setPropertyNames("begindatum");
		ingang.addFieldModifier(new LabelModifier("begindatum", "Verhuisdatum"));
		if (oudPersoonAdres != null)
		{
			final Date minDatum =
				TimeUtil.getInstance().addDays(oudPersoonAdres.getBegindatum(), 1);
			ingang.addFieldModifier(new PostProcessModifier("begindatum")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
						FieldProperties<T, ? , ? > fieldProperties)
				{
					form.add(new DatumGroterOfGelijkDatumValidator("Verhuisdatum",
						(DatumField) field, minDatum));
				}
			});
		}
		form.add(ingang);

		createComponents();
	}

	private Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	private Persoon getPersoon()
	{
		return getDeelnemer().getPersoon();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AnnulerenButton(panel, returnPage));
		panel.addButton(new OpslaanButton(panel, form, "Verhuizing doorvoeren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Persoon deelnemer = getPersoon();
				PersoonAdres nieuwAdres = nieuwAdresModel.getObject();
				List<Persoon> verhuizendePersonen = new ArrayList<Persoon>();
				verhuizendePersonen.add(deelnemer);
				for (Relatie curRelatie : getRelatiesOpZelfdeAdres())
					if (selectie.isSelected(curRelatie))
						verhuizendePersonen.add(curRelatie.getRelatie());

				for (Persoon curPersoon : verhuizendePersonen)
				{
					PersoonAdres curAdresEntiteit = curPersoon.newAdres();
					curAdresEntiteit.setAdres(nieuwAdres.getAdres());
					curAdresEntiteit.setPostadres(true);
					curAdresEntiteit.setFysiekadres(true);
					curAdresEntiteit.setFactuuradres(true);
					curAdresEntiteit.setBegindatum(nieuwAdres.getBegindatum());

					for (PersoonAdres oudAdres : AdresseerbaarUtil.getAdressen(curPersoon,
						new OnbeeindigdAdresFilter(true)))
					{
						oudAdres.setEinddatum(TimeUtil.getInstance().addDays(
							nieuwAdres.getBegindatum(), -1));
					}
					curPersoon.getAdressen().add(0, curAdresEntiteit);
				}

				setResponsePage(returnPage);
			}
		});
	}

	private List<Relatie> getRelatiesOpZelfdeAdres()
	{
		Persoon persoon = getPersoon();
		Adres laatsteAdres = getEersteOnbeeindigdeAdres(persoon).getAdres();
		if (laatsteAdres == null)
			return Collections.emptyList();

		List<Relatie> ret = new ArrayList<Relatie>();
		for (Relatie curRelatie : persoon.getRelatiesRelatie())
		{
			PersoonAdres persoonAdres = getEersteOnbeeindigdeAdres(curRelatie.getRelatie());
			if (persoonAdres != null)
			{
				Adres curRelatieAdres = persoonAdres.getAdres();
				if (laatsteAdres.equals(curRelatieAdres))
					ret.add(curRelatie);
			}
		}
		return ret;
	}

	private PersoonAdres getEersteOnbeeindigdeAdres(Persoon persoon)
	{
		return AdresseerbaarUtil.getEersteAdres(persoon, new OnbeeindigdAdresFilter(false));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		nieuwAdresModel.detach();
		selectie.detach();
		relaties.detach();
		deelnemerModel.detach();
	}
}
