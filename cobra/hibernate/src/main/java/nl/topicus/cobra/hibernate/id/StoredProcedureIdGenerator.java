package nl.topicus.cobra.hibernate.id;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.persistence.SequenceGenerator;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

@SequenceGenerator(name = "StoredProcedureIdGenerator")
public class StoredProcedureIdGenerator implements IdentifierGenerator
{

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException
	{
		Long id = null;
		try
		{
			CallableStatement stat =
				session.connection().prepareCall(
					"{call [dbo].[$SSMA_sp_get_nextval_HIBERNATE_SEQUENCE](?)}");
			stat.registerOutParameter(1, java.sql.Types.BIGINT);
			stat.execute();
			id = stat.getLong(1);
			stat.close();
		}
		catch (SQLException e)
		{
			throw new HibernateException("Unable to get new identifier value", e);
		}
		return id;
	}

}
