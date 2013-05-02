package nl.topicus.cobra.templates.validators;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.rtf.RtfDocument;
import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;

public class BeanPropertyRtfValidator implements IValidator<BeanPropertyResolver>
{

	/**
	 * That document that is to be validated
	 */
	private RtfDocument document;

	public BeanPropertyRtfValidator(RtfDocument document)
	{
		this.document = document;
	}

	@Override
	public List<String> validateAndReturnErrors(BeanPropertyResolver resolver)
	{
		List<String> errors = null;
		List<FieldInfo> info = document.getFieldInfo(resolver);
		for (FieldInfo fieldInfo : info)
		{
			boolean hasFieldClass = fieldInfo.getFieldClass() != null;
			if (!hasFieldClass)
			{
				if (errors == null)
					errors = new ArrayList<String>();

				errors.add(fieldInfo.getMessage());
			}
		}
		return errors;
	}

}
