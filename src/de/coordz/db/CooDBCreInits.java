/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.coordz.db.xml.*;
import de.util.log.CooLog;

public class CooDBCreInits
{
	public static final String PREFX = "InitTbl";
	
	public void create(CooDBModel model) throws SAXException, 
		IOException, ParserConfigurationException, SQLException
	{
		// Create the DB Inits
		createDBInits(model);
	}

	private void createDBInits(CooDBModel model)
	{
		CooLog.debug("Create the database initializes objects");
		
		for(CooDBTable table : model.getTables())
		{
			// Generate the Init name
			String initName = PREFX + table.nameProperty().get();
			File initFile = new File("src/" + CooDBDao.class.getPackage()
				.getName().replace(".", "/") + "/gen/init", initName + ".java");
			// Create folders if they not exists
			initFile.getParentFile().mkdirs();
			
			CooLog.debug("Creating Init for <" + initName + ">");
			
			try(PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(initFile))))
			{
				// Add the package declaration
				out.println(CooDBDao.class.getPackage() + ".gen.init;");
				out.println();
				
				// Define the used imports
				out.println("import java.sql.*;");
				out.println("import " + CooDBDao.class.getPackage().getName() + ".gen.dao.*;");
				out.println();
				
				out.println("public abstract class " + initName + " extends " + CooDBInit.class.getName());
				out.print("{");
				
					// Add the put methods
					addPutMethods(out, table);
					// Add the put method
					addPutMethod(out, table);
				
				out.print("}");
				
				out.flush();
				out.close();
			}
			catch(IOException e)
			{
				CooLog.error("Error while creating Init", e);
			}
		}
	}
	
	private void addPutMethods(PrintWriter out, CooDBTable table)
	{
		// Get the table columns
		List<CooDBColumn> columns = table.getColumns();
		List<CooDBColumn> columnsCopy = new ArrayList<>(columns.size());
		
		// Skip the primary key and start at column 1
		for(int i = 1; i < columns.size() - 1; i++)
		{
			out.println();
			out.print("\tprotected void put(");
			columnsCopy.add(columns.get(i));
			
			// Loop through the columns
			for(int j = 0; j < columnsCopy.size(); j++)
			{
				// Construct the put method head with parameter
				CooDBColumn col = columnsCopy.get(j);
				out.print(col.typeProperty().get().getPrimitive() + " " + col.nameProperty().get() 
					+ (j < columnsCopy.size() - 1 ? ", " : ") throws SQLException"));
			}
			out.println();
			out.println("\t{");
			
			// Construct the put call
			out.print("\t\tput(");
			// Loop through the columns
			for(int j = 0; j < columnsCopy.size(); j++)
			{
				// Add the variables to put call - add default last
				CooDBColumn col = columnsCopy.get(j);
				out.print(col.nameProperty().get() 
					+ (j < columnsCopy.size() - 1 ? ", " : ", " +
						getDefault(columns.get(j + 2))));
			}
			out.print(");");
			out.println();
			out.println("\t}");
		}
	}

	private void addPutMethod(PrintWriter out, CooDBTable table)
	{
		out.println();
		out.print("\tprotected void put(");
		
		// Create the dao name
		String daoName = CooDBCreDaos.getDaoName(table);
		// Get the table columns
		List<CooDBColumn> columns = table.getColumns();

		// Skip the primary key and start at column 1
		for(int i = 1; i < columns.size(); i++)
		{
			// Construct the put method head with parameter
			CooDBColumn col = columns.get(i);
			out.print(col.typeProperty().get().getPrimitive() + " " + col.nameProperty().get() 
				+ (i < columns.size() - 1 ? ", " : ") throws SQLException"));
		}
		out.println();
		out.println("\t{");
		
		// Initializes the dao object
		out.println("\t\t" + daoName + " dao = new "+ daoName + "();");
		out.println("\t\tdao.cre();");
		
		// Skip the primary key and start at column 1
		for(int i = 1; i < columns.size(); i++)
		{
			// Add the setting of the properties
			CooDBColumn col = columns.get(i);
			out.println("\t\tdao." + col.nameProperty().get() + 
				"Property().set(" + col.nameProperty().get() + ");");
		}

		// Add the dao insert and close method
		out.println("\t\tdao.insert();");
		out.println("\t}");
	}
	
	private String getDefault(CooDBColumn column)
	{
		String def = null;
		// Switch the column type 
		switch(column.typeProperty().get())
		{
			case BOOLEAN:
				def = "Boolean.FALSE";
				break;
			case DOUBLE:
				def = "0.0d";
				break;
			case INTEGER:
				def = "0";
				break;
			case VARCHAR:
				def = "\"\"";
				break;
			default:
			case TIMESTAMP:
				def = null;
				break;
		}
		return def;
	}
}