package nl.topicus.cobra.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.Inherit;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.hive.authorization.permissions.ComponentPermission;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface InPrincipal
{
	Class< ? extends IPrincipalSource< ? extends Principal>>[] value();

	Actions[] actions() default {@Actions( {Inherit.class, Render.class}),
		@Actions( {Enable.class})};

	Class< ? extends Permission> permissionClass() default ComponentPermission.class;
}
