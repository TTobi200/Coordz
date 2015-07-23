/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.db;

import java.sql.*;
import java.util.Objects;

import de.util.log.CooLog;

public class CooDerbyDB extends CooDB
{
	private static String APACHE_CLIENT_DRIVER = "org.apache.derby.jdbc.ClientDriver";

	// jdbc Connection
	private StringBuilder dbURL;
	private Connection conn = null;
	private Statement stmt = null;

	@Override
	public void connect(String dbName, String usrName,
					String password, boolean create)
	{
		dbURL = new StringBuilder("jdbc:derby:");
		dbURL.append(dbName).append(";")
			.append("create=").append(String.valueOf(create)).append(";")
			.append("user=").append(usrName).append(";")
			.append("password=").append(password).append(";");

		try
		{
			Class.forName(APACHE_CLIENT_DRIVER).newInstance();
			conn = DriverManager.getConnection(dbURL.toString());
			stmt = conn.createStatement();
		}
		catch(Exception e)
		{
			CooLog.error("Error while establish "
							+ "DB connection", e);
		}
	}

	@Override
	public void shutdown()
	{
		try
		{
			// Close statement
			if(Objects.nonNull(stmt))
			{
				stmt.close();
			} // Shutdown DB driver
			if(Objects.nonNull(dbURL))
			{
				DriverManager.getConnection(
					dbURL.append(";shutdown=true").toString());
			} // Close connection
			if(Objects.nonNull(conn))
			{
				conn.close();
			}
		}
		catch(SQLException e)
		{
			// Don't log this exception, always thrown on db shutdown
//			CoordzLog.error("Error While closing "
//							+ "DB connection", e);
		}
	}

	@Override
	public ResultSet exec(String stmt) throws SQLException
	{
		this.stmt.execute(stmt);
		return null;
//		return this.stmt.executeQuery(stmt);
	}
}