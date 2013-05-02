package nl.topicus.cobra.templates.validators;

import java.util.List;

import nl.topicus.cobra.templates.resolvers.FieldResolver;

public interface IValidator<T extends FieldResolver>
{
	List<String> validateAndReturnErrors(T resolver);
}
