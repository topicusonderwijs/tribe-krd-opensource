package nl.topicus.cobra.web.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.security.checks.ISecurityCheck;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredSecurityCheck
{
	Class< ? extends ISecurityCheck> value();

	Class< ? extends ISecurityCheckFactory> factory() default ModelBasedSecurityCheckFactory.class;
}
