package nl.topicus.cobra.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.security.hive.authorization.Principal;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Implies
{
	Class< ? extends IPrincipalSource< ? extends Principal>>[] value();
}
