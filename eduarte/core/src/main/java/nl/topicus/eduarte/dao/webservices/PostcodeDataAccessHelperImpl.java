package nl.topicus.eduarte.dao.webservices;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.GemeenteDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Haalt het adres op via een REST service.
 * <p>
 * Een service call geeft het volgende bericht terug:
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * &lt;response&gt;
 *     &lt;reeksid&gt;489979&lt;/reeksid&gt;
 *     &lt;huisnr_van&gt;1&lt;/huisnr_van&gt;
 *     &lt;huisnr_tm&gt;11&lt;/huisnr_tm&gt;
 *     &lt;wijkcode&gt;7411&lt;/wijkcode&gt;
 *     &lt;lettercombinatie&gt;HR&lt;/lettercombinatie&gt;
 *     &lt;reeksindicatie&gt;0&lt;/reeksindicatie&gt;
 *     &lt;straatid&gt;179967&lt;/straatid&gt;
 *     &lt;straatnaam&gt;Brinkpoortstraat&lt;/straatnaam&gt;
 *     &lt;straatnaam_nen&gt;Brinkpoortstraat&lt;/straatnaam_nen&gt;
 *     &lt;straatnaam_ptt&gt;BRINKPOORTSTR&lt;/straatnaam_ptt&gt;
 *     &lt;straatnaam_extract&gt;BRINK&lt;/straatnaam_extract&gt;
 *     &lt;plaatsid&gt;1419&lt;/plaatsid&gt;
 *     &lt;plaatsnaam&gt;DEVENTER&lt;/plaatsnaam&gt;
 *     &lt;plaatsnaam_ptt&gt;DEVENTER&lt;/plaatsnaam_ptt&gt;
 *     &lt;plaatsnaam_extract&gt;DEVE&lt;/plaatsnaam_extract&gt;
 *     &lt;gemeenteid&gt;401&lt;/gemeenteid&gt;
 *     &lt;gemeentenaam&gt;DEVENTER&lt;/gemeentenaam&gt;
 *     &lt;gemeentecode&gt;150&lt;/gemeentecode&gt;
 *     &lt;cebucocode&gt;130&lt;/cebucocode&gt;
 *     &lt;provinciecode&gt;E&lt;/provinciecode&gt;
 *     &lt;provincienaam&gt;Overijssel&lt;/provincienaam&gt;
 * &lt;/response&gt;
 * </pre>
 */
public class PostcodeDataAccessHelperImpl extends AbstractPostcodeDataAccessHelper
{
	private static final Logger log = LoggerFactory.getLogger(PostcodeDataAccessHelperImpl.class);

	private static final String USERNAME = "";

	private static final String PASSWORD = "";

	private static final String REST_URL =
		"https://ws1.webservices.nl/rpc/get-simplexml/addressReeksPostcodeSearch";

	private Map<String, AdresImpl> cache =
		Collections.synchronizedMap(new LinkedHashMap<String, AdresImpl>());

	@Override
	public Adres fillAdresByPostcodeHuisnummer(String postcode, String huisnummer,
			String huisnummerToevoeging)
	{
		String key = postcode.toUpperCase() + huisnummer;
		if (cache.containsKey(key))
		{
			AdresImpl ret = new AdresImpl(cache.get(key));
			ret.setHuisnummerToevoeging(huisnummerToevoeging);
			return ret;
		}

		AdresImpl result = newAdres(postcode, huisnummer, huisnummerToevoeging);
		if (postcode.contains(" "))
			return result;

		String text = null;

		HttpClient httpclient = new HttpClient();
		String uri = REST_URL + "/" + USERNAME + "/" + PASSWORD + "/" + key;
		GetMethod httpget = null;
		try
		{
			httpget = new GetMethod(uri);

			httpclient.executeMethod(httpget);
			System.out.println(httpget.getStatusLine());
			text = httpget.getResponseBodyAsString();
			int statusCode = httpget.getStatusCode();
			if (statusCode >= 500)
				return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return result;
		}
		finally
		{
			if (httpget != null)
				httpget.releaseConnection();
		}

		// controleer op een foutmelding
		if (StringUtil.isNotEmpty(getTagValue(text, "faultCode")))
			return result;

		GemeenteDataAccessHelper gemeentes =
			DataAccessRegistry.getHelper(GemeenteDataAccessHelper.class);
		ProvincieDataAccessHelper provincies =
			DataAccessRegistry.getHelper(ProvincieDataAccessHelper.class);

		String gemeentecode =
			StringUtil.voegVoorloopnullenToe(getTagValue(text, "gemeentecode"), 4);

		result.setGemeente(gemeentes.get(gemeentecode));

		String code = provincieCodes.get(getTagValue(text, "provinciecode").toUpperCase());
		if (code != null)
			result.setProvincie(provincies.get(code));

		result.setPlaatsnaam(getTagValue(text, "plaatsnaam"));
		result.setStraatnaam(getTagValue(text, "straatnaam"));

		cache.put(key, result);
		while (cache.size() > 1000)
		{
			String firstKey = cache.keySet().iterator().next();
			cache.remove(firstKey);
		}
		return result;
	}

	private String getTagValue(String text, String tagname)
	{
		Pattern pattern = Pattern.compile("<" + tagname + ">(.*)</" + tagname + ">");
		Matcher matcher = pattern.matcher(text);
		if (!matcher.find())
			return null;
		String tagvalue = matcher.group(1);
		return tagvalue;
	}
}
