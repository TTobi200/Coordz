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

public class CooDBCreDaos
{
	public static final String PREFIX = "Dao";
	
	public void create(CooDBModel model) throws SAXException, 
		IOException, ParserConfigurationException, SQLException
	{
		// Create the DB DAOs
		createDBDaos(model);
	}

	private void createDBDaos(CooDBModel model)
	{
		CooLog.debug("Create the database acess objects");
		
		for(CooDBTable table : model.getTables())
		{
			// Generate the DAO name
			String daoName = PREFIX + table.nameProperty().get();
			File daoFile = new File("src/" + CooDBDao.class.getPackage()
				.getName().replace(".", "/") + "/gen/dao", daoName + ".java");
			// Create folders if they not exists
			daoFile.getParentFile().mkdirs();
			
			CooLog.debug("Creating DAO for <" + daoName + ">");
			
			try(PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(daoFile))))
			{
				// Add the package declaration
				out.println(CooDBDao.class.getPackage() + ".gen.dao;");
				out.println();
				
				// Define the used imports
				addNeededImports(out, table);
				out.println();
				
				out.println("public class " + daoName + " extends " + CooDBDao.class.getName());
				out.println("{");
				
					// Add the column variables
					addColumnVariables(out, daoName, table.getColumns());
					
					// Add the default constructor
					out.println();
					addConstructor(out, daoName, table);
					
					// Add the getters for propertys
					addGetters(out, table.getColumns());
				
				out.println("}");
				
				out.flush();
				out.close();
			}
			catch(IOException e)
			{
				CooLog.error("Error while creating DAO", e);
			}
		}
	}

	private void addNeededImports(PrintWriter out, CooDBTable table)
	{
		// Check if time stamp used in this dao
		boolean timestampUsed = table.getColumns().stream().filter(
			c -> c.typeProperty().get()	== CooDBValTypes.TIMESTAMP)
				.findAny().isPresent();
		
		// Check if Blob used in this dao
		boolean blobUsed = table.getColumns().stream().filter(
			c -> c.typeProperty().get()	== CooDBValTypes.BLOB)
				.findAny().isPresent();
		
		out.println("import javafx.beans.property.*;");
		out.println("import " + CooDBDao.class.getPackage().getName() + ".*;");
		out.println("import " + CooDBDao.class.getPackage().getName() + ".gen.inf.*;");
		
		if(timestampUsed)
		{
			out.println("import java.sql.Timestamp;");
		}
		if(blobUsed)
		{
			out.println("import java.sql.Blob;");
		}		
	}

	private void addConstructor(PrintWriter out, String daoName, CooDBTable table)
	{
		out.println("\tpublic " + daoName + "()");
		out.println("\t{");
			
			out.println("\t\ttableName = " + CooDBCreInfs.getColConst(table, "TABLE_NAME") +";");
			out.println("\t\ttablePKey = " + CooDBCreInfs.getColConst(table, 
				table.pKeyProperty().get()) + ";");
			out.println("\t\ttableFKey = " + (Objects.nonNull(table.fKeyProperty().get()) ? 
				CooDBCreInfs.getColConst(table, table.fKeyProperty().get()) : "null") + ";");
			out.println();
			
			for(CooDBColumn col : table.getColumns())
			{
				// Put the property to columns map
				out.println("\t\taddColumn(" + CooDBCreInfs.getColConst(table,
					// Add the column name
					col.nameProperty().get()) + ", " + 
						// Add the column DB value type
						CooDBValTypes.class.getSimpleName() + "." + col.typeProperty().get() + ", " +	
							// Add the property for this column
							col.nameProperty().get() + "Property = new Simple" 
								+ col.typeProperty().get().getProperty() + "()" +");");
			}
		
		out.println("\t}");
	}

	private void addGetters(PrintWriter out, List<CooDBColumn> columns)
	{
		for(CooDBColumn col : columns)
		{
			out.println();
			out.println("\tpublic " + col.typeProperty().get().getProperty() 
				+ " " + col.nameProperty().get() + "Property()");
			out.println("\t{");
				
				out.println("\t\treturn " + col.nameProperty().get() + "Property;");
			
			out.println("\t}");
		}
	}

	private void addColumnVariables(PrintWriter out,
		String daoName, List<CooDBColumn> columns)
	{
		for(CooDBColumn col : columns)
		{
			// Print the description
			out.println("\t/** {@link " + col.typeProperty().get().getProperty() +
				" } for {@link " + daoName + "} " + col.nameProperty().get() + " */");
			// Print the property variable
			out.print("\tprivate ");
			out.print(col.typeProperty().get().getProperty() + " ");
			out.print(col.nameProperty().get() + "Property;");
			out.println();
		}
	}
	
	public static String getDaoName(CooDBTable table)
	{
		return CooDBCreDaos.PREFIX + table.nameProperty().get();
	}
}