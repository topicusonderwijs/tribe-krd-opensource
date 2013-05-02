package nl.topicus.cobra.web.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.UrlValidator;

/**
 * Validator welke toestaat dat men geen url scheme invult. Speciaal voor leken.
 * 
 * @author hoeve
 */
public class LekenUrlValidator extends UrlValidator
{
	private static final long serialVersionUID = 1L;

	/**
	 * This expression derived/taken from the BNF for URI (RFC2396).
	 */
	private static final String URL_PATTERN =
		"^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";

	/**
	 * Schema / Protocol (<code>http:</code>, <code>ftp:</code>, <code>file:</code>, etc).
	 */
	private static final int PARSE_URL_SCHEME = 2;

	private TextField<String> waardeField;

	public LekenUrlValidator(TextField<String> waardeField)
	{
		this.waardeField = waardeField;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String url = validatable.getValue();

		if (url != null)
		{
			Matcher matchUrlPat = Pattern.compile(URL_PATTERN).matcher(url);
			matchUrlPat.matches();

			String urlscheme = matchUrlPat.group(PARSE_URL_SCHEME);
			if (urlscheme == null || urlscheme.isEmpty() && waardeField != null)
			{
				String[] newurl = new String[1];
				newurl[0] = "http://" + url;
				waardeField.setModelValue(newurl);
			}
		}

		super.onValidate(validatable);
	}
}
