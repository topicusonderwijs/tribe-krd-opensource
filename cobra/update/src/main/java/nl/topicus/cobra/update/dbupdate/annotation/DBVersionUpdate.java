package nl.topicus.cobra.update.dbupdate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Markeert een methode als update. Voor deze methode wordt automatisch een mark
 * geplaatst.
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface DBVersionUpdate
{
	public enum UPDATE_TYPE
	{
		DEFAULT,
		SCHEMA,
		DATA,
		CONSTRAINT,
		FINISH;
	}

	String name() default "";

	int volgnummer() default 0;

	UPDATE_TYPE type() default UPDATE_TYPE.DEFAULT;

	boolean isFinal() default true;
}