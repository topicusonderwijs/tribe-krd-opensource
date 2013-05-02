package nl.topicus.eduarte.krd.web.pages.intake;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.RelatieDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntakeWizardModelTest extends EduArteTestCase
{
	@Before
	public void setUpVandaag()
	{
		tester.setupRequestAndResponse();
	}

	@After
	public void verwijderRelatiesDataAccessHelper()
	{
		// DataAccessRegistry.getInstance().unregister(RelatieDataAccessHelper.class);
	}

	/**
	 * @mantis 37611
	 */
	@Test
	public void registratieDatumIsSysteemDatum()
	{
		Date date = TimeUtil.getInstance().currentDate();
		IntakeWizardModel model = new IntakeWizardModel();
		Assert.assertEquals(date, model.getRegistratieDatum());
	}

	/**
	 * @mantis 38153
	 */
	@Test
	public void woonAdresIsPostAdresStandaardGezet()
	{
		IntakeWizardModel model = new IntakeWizardModel();
		Assert.assertEquals(1, model.getDeelnemer().getPersoon().getFysiekAdressen().size());
		Assert.assertEquals(1, model.getDeelnemer().getPersoon().getPostAdressen().size());
	}

	@Test
	public void initVanStap2KoppeltBestaandeRelaties()
	{
		Persoon broer = createPersoonWonendOp("1234AB", "123");
		Persoon vader = createPersoonWonendOp("1234AB", "123");
		Persoon moeder = createPersoonWonendOp("1234AB", "123");
		RelatieSoort soort = new RelatieSoort();
		soort.setNaam("Ouder");
		Relatie broerVader = maakRelatie(broer, vader, soort);
		Relatie broerMoeder = maakRelatie(broer, moeder, soort);

		List<Relatie> verwachteRelaties = Arrays.asList(broerVader, broerMoeder);
		tester.setRelaties(verwachteRelaties);

		IntakeWizardModel model = new IntakeWizardModel();
		Deelnemer deelnemer = model.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		persoon.getFysiekAdres().getAdres().setPostcode("1234AB");
		persoon.getFysiekAdres().getAdres().setHuisnummer("123");

		model.startStap2();

		List<Relatie> gevondenRelaties = persoon.getRelatiesRelatie();
		Assert.assertEquals(verwachteRelaties.size(), gevondenRelaties.size());
		Assert.assertEquals(verwachteRelaties.get(0).getRelatie(), gevondenRelaties.get(0)
			.getRelatie());
		Assert.assertEquals(verwachteRelaties.get(1).getRelatie(), gevondenRelaties.get(1)
			.getRelatie());
	}

	private Relatie maakRelatie(Persoon kind, Persoon ouder, RelatieSoort soort)
	{
		Relatie relatie = new Relatie();
		relatie.setDeelnemer(kind);
		relatie.setRelatie(ouder);
		relatie.setRelatieSoort(soort);
		relatie.setId(System.nanoTime());
		kind.getRelaties().add(relatie);
		return relatie;
	}

	private Persoon createPersoonWonendOp(String postcode, String huisnummer)
	{
		Persoon persoon = new Persoon();
		PersoonAdres pa = persoon.newAdres();
		pa.setFysiekadres(true);
		persoon.getAdressen().add(pa);
		pa.getAdres().setHuisnummer(huisnummer);
		pa.getAdres().setPostcode(postcode);

		return persoon;
	}

	@Test
	public void initVanStap2KoppeltGeenRelaties()
	{
		List<Relatie> verwachteRelaties = Collections.emptyList();

		RelatieDataAccessHelper helper =
			DataAccessRegistry.getHelper(RelatieDataAccessHelper.class);
		EasyMock.reset(helper);

		RelatieZoekFilter filter = new RelatieZoekFilter();
		filter.setPostcode("1234AB");
		filter.setHuisnummer("123");
		filter.setLand(Land.getNederland());

		EasyMock.expect(helper.list(filter)).andStubReturn(verwachteRelaties);
		EasyMock.replay(helper);

		IntakeWizardModel model = new IntakeWizardModel();
		Deelnemer deelnemer = model.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		persoon.getFysiekAdres().getAdres().setPostcode("1234AB");
		persoon.getFysiekAdres().getAdres().setHuisnummer("123");

		model.startStap2();

		List<Relatie> gevondenRelaties = persoon.getRelatiesRelatie();
		Assert.assertEquals(verwachteRelaties.size(), gevondenRelaties.size());
	}

	@Test
	public void voegNieuweRelatieToeKopieertAdres()
	{
		IntakeWizardModel wizard = new IntakeWizardModel();
		Persoon deelnemer = wizard.getDeelnemer().getPersoon();

		Adres deelnemerAdres = deelnemer.getFysiekAdres().getAdres();
		deelnemerAdres.setPostcode("1234AB");
		deelnemerAdres.setHuisnummer("123");
		deelnemerAdres.setStraat("Pietje Pukstraat");
		deelnemerAdres.setPlaats("Hoogrhenen");

		Relatie nieuweRelatie = wizard.addNieuwePersoonRelatie();
		Persoon relatie = nieuweRelatie.getRelatie();
		Adres relatieAdres = relatie.getFysiekAdres().getAdres();

		Assert.assertNotSame(deelnemer.getFysiekAdres(), relatie.getFysiekAdres());
		// Verwachten dat het *adres* object wel dezelfde instance is
		Assert.assertSame(deelnemerAdres, relatieAdres);

		Assert.assertEquals(deelnemerAdres.getPostcode(), relatieAdres.getPostcode());
		Assert.assertEquals(deelnemerAdres.getPlaats(), relatieAdres.getPlaats());
		Assert.assertEquals(deelnemerAdres.getStraatHuisnummer(), relatieAdres
			.getStraatHuisnummer());
	}
}
