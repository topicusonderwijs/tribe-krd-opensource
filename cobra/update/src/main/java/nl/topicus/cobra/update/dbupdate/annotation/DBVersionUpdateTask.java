package nl.topicus.cobra.update.dbupdate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface DBVersionUpdateTask
{
	public static final String NO_VERSION = "NO_VERSION";

	String version() default NO_VERSION;

	boolean isFinal() default false;

	/**
	 * @return true when this Task should be indexed by the component scan; Spring
	 *         dependent Tasks should set this to false
	 */
	boolean useScan() default true;

	Class< ? > runAfter() default Object.class;

}