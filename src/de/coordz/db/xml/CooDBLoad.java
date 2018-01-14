/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.xml;

import java.sql.SQLException;

import de.coordz.db.CooDB;

/**
 * Interface for db load functions.
 * @author tobias.ohm
 */
public interface CooDBLoad
{
	public void fromDB(CooDB database) throws SQLException;
}
