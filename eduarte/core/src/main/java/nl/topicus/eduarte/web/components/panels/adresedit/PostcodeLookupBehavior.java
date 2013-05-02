package nl.topicus.eduarte.web.components.panels.adresedit;

import nl.topicus.cobra.converters.PostcodeConverter;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper.Adres;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.web.components.quicksearch.land.LandSearchEditor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PostcodeLookupBehavior extends AjaxFormComponentUpdatingBehavior
{
	private static final Logger log = LoggerFactory.getLogger(PostcodeLookupBehavior.class);

	private static final long serialVersionUID = 1L;

	private TextField<String> postcodeField;

	private TextField<String> huisnummerField;

	private LandSearchEditor landField;

	private TextField<String> huisnummerToevoegingField;

	public PostcodeLookupBehavior(String event, TextField<String> postcode,
			TextField<String> huisnummer, LandSearchEditor land,
			TextField<String> huisnummerToevoeging)
	{
		super(event);
		this.postcodeField = postcode;
		this.huisnummerField = huisnummer;
		this.landField = land;
		this.huisnummerToevoegingField = huisnummerToevoeging;
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		if (e != null)
		{
			log.error(e.getMessage(), e);
			getComponent().error(e.toString());
		}
		((FeedbackComponent) postcodeField.getPage()).refreshFeedback(target);
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		/**
		 * beetje jammer, omdat we juist niet het ingevulde willen hebben maar wat in het
		 * object komt. dit is nog niet zo dus moeten we het ff zelf omzetten.
		 */
		IConverter converter = postcodeField.getConverter(PostcodeConverter.class);

		String postcode = null;
		if (postcodeField.getModelObject() != null)
			postcode =
				converter.convertToObject(postcodeField.getModelObject().toString(), null)
					.toString();
		String huisnummer = null;
		if (huisnummerField.getModelObject() != null)
			huisnummer =
				converter.convertToObject(huisnummerField.getModelObject().toString(), null)
					.toString();

		String huisnummerToevoeging = null;
		if (huisnummerToevoegingField.getModelObject() != null)
			huisnummerToevoeging =
				converter.convertToObject(huisnummerToevoegingField.getModelObject().toString(),
					null).toString();

		Land land = landField.getModelObject();

		if (StringUtil.isEmpty(postcode) || StringUtil.isEmpty(huisnummer) || land == null)
			return;

		// geen postcode lookup bij een buitenlands adres
		if (!Land.getNederland().equals(land))
			return;

		PostcodeDataAccessHelper.Adres result =
			DataAccessRegistry.getHelper(PostcodeDataAccessHelper.class)
				.fillAdresByPostcodeHuisnummer(postcode, huisnummer, huisnummerToevoeging);

		if (StringUtil.isEmpty(result.getStraatnaam())
			|| StringUtil.isEmpty(result.getPlaatsnaam()))
			getComponent().error("Geen adres gevonden bij deze postcode/huisnummer combinatie");
		onPostcode(target, result);
		FeedbackComponent feedbackComponent = postcodeField.findParent(FeedbackComponent.class);
		feedbackComponent.refreshFeedback(target);
	}

	protected abstract void onPostcode(AjaxRequestTarget target, Adres result);
}
