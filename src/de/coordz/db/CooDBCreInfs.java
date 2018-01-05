/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.coordz.db.xml.*;
import de.util.log.CooLog;

public class CooDBCreInfs
{
	public static final String PREFIX = "Inf";
	
	public void create(CooDBModel model) throws SAXException, 
		IOException, ParserConfigurationException, SQLException
	{
		// Create the DB informations
		createDBInfs(model);
	}

	private void createDBInfs(CooDBModel model)
	{
		CooLog.debug("Create the database infomation objects");
		
		for(CooDBTable table : model.getTables())
		{
			// Generate the Inf name
			String infName = PREFIX + table.nameProperty().get();
			File infFile = new File("src/" + CooDBDao.class.getPackage()
				.getName().replace(".", "/") + "/gen/inf", infName + ".java");
			// Create folders if they not exists
			infFile.getParentFile().mkdirs();
			
			CooLog.debug("Creating Inf for <" + infName + ">");
			
			try(PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(infFile))))
			{
				// Add the package declaration
				out.println(CooDBDao.class.getPackage() + ".gen.inf;");
				out.println();
				
				out.println("public final class " + infName);
				out.println("{");
				
					// Add the constant variables
					addConstantVars(out, table.nameProperty().get(), 
						table.getColumns());
				
				out.print("}");
				
				out.flush();
				out.close();
			}
			catch(IOException e)
			{
				CooLog.error("Error while creating Inf", e);
			}
		}
	}

	private void addConstantVars(PrintWriter out, 
		String tableName, List<CooDBColumn> columns)
	{
		// Add constant for table name
		out.println("\t/** {@link String} constant for table name */");
		out.println("\tpublic static final String NAME = \""+ tableName +"\";");

		// Loop through all table columns
		for(CooDBColumn col : columns)
		{
			String colName = col.nameProperty().get();
			
			// Add the constant for column
			out.println("\t/** {@link String} constant for column " + colName + " */");
			out.println("\tpublic static final String " + colName.toUpperCase() + " = \"" + colName + "\";");
			
			// Add the constant for table column
			out.println("\t/** {@link String} constant for table column " + colName + " */");
			out.println("\tpublic static final String T_" + colName.toUpperCase() + " = NAME + \"." + colName + "\";");
		}
	}
	
	public static String getInfName(CooDBTable table)
	{
		return CooDBCreInfs.PREFIX + table.nameProperty().get();
	}
	
	public static String getColConst(CooDBTable table, String column)
	{
		return getColConst(table, column, Boolean.FALSE);
	}
	
	public static String getColConst(CooDBTable table, 
			String column, boolean addTable)
	{
		return PREFIX + table.nameProperty().get() + "." 
			+ (addTable ? "T_" : "") + column.toUpperCase();
	}
}