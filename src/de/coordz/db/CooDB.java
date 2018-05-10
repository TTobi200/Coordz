/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.sql.*;

public abstract class CooDB
{
	/**
	 * Method to establish connection to this DB.
	 * @param dbHost = the database host
	 * @param dbPort = the database port
	 * @param dbName = the database name
	 * @param usrName = the user name
	 * @param password = the user password
	 * @param create = boolean if database should be created
	 */
	public abstract void connect(String dbHost, String dbPort, String dbName, 
		String usrName, String password, boolean create);
	
	/**
	 * Method to execute committed SQL-Statement on this DB.
	 * @param sql = the SQL-Statement to execute
	 * @return true if statement completed successful
	 * @throws SQLException - if statement could not be executed
	 */
	public abstract boolean exec(String sql) throws SQLException;
	
	/**
	 * Method to execute committed SQL-Query-Statement on this DB.
	 * @param sql = the SQL-Query-Statement to execute
	 * @return {@link ResultSet} with found data
	 * @throws SQLException - if statement could not be executed
	 */
	public abstract ResultSet execQuery(String sql) throws SQLException;
	
	/**
	 * Method to execute committed {@link CooDBSelectStmt} on this DB.
	 * @param stmt = the {@link CooDBSelectStmt} to execute
	 * @return {@link ResultSet} with found data
	 * @throws SQLException - if statement could not be executed
	 */
	public abstract ResultSet execQuery(CooDBSelectStmt stmt) throws SQLException;
	
	/**
	 * Method to execute committed SQL-Update-Statement on this DB.
	 * @param sql = the SQL-Update-Statement to execute
	 * @return integer with resulting count
	 * @throws SQLException - if statement could not be executed
	 */
	public abstract int execUpdate(String sql) throws SQLException;
	
	/**
	 * Method to create a prepared SQL-Update-Statement for this DB.
	 * @param sql = the SQL-Update-Statement to prepare
	 * @return the created PreparedStatement 
	 * @throws SQLException - if prepared statement could not be created
	 */
	public abstract PreparedStatement prepareStatement(String sql) throws SQLException;
	
	/**
	 * Method to clean up this DB. Restores an empty DB.
	 * @param dropTable = boolean flag if table should be dropped
	 * @throws SQLException - when cleanup failed
	 */
	public abstract void cleanUp(boolean dropTable) throws SQLException;
	
	/**
	 * Method to shutdown this database instance.
	 * @throws SQLException when shutdown went wrong
	 */
	public abstract void shutdown() throws SQLException;
	
	/**
	 * Method to get the data type for this specified {@link CooDB}.
	 * @param type = the {@link CooDBValTypes} type
	 * @param column = the column name
	 * @return a {@link String} containing the data type
	 */
	public abstract String getDataType(CooDBValTypes type, String column);
	
	/**
	 * Method to get escape committed column for this specified {@link CooDB}.
	 * @param column = the column name
	 * @return a {@link String} containing the escaped column
	 */
	public abstract String escapeColumn(String column);
	
	/**
	 * Method to get create a {@link Blob} for this specified {@link CooDB}.
	 * @return a new created empty {@link Blob}
	 * @throws SQLException when {@link Blob} creation failed
	 */
	public abstract Blob createBlob()  throws SQLException;
}