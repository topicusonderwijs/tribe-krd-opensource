package nl.topicus.cobra.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Retention(RetentionPolicy.RUNTIME)
@Target( {})
public @interface ExtraPermission
{
	Class< ? extends Permission> type() default DataPermission.class;

	Class< ? extends Component> componentPrefix() default Component.class;

	String name();

	Class< ? extends WaspAction>[] actions() default {Render.class};
}
