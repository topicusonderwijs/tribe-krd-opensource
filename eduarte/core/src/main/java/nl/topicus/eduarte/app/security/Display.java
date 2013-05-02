package nl.topicus.eduarte.app.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.topicus.eduarte.app.EduArteModuleKey;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Display
{
	String parent();

	String label();

	EduArteModuleKey[] module();

	String group() default "";
}
