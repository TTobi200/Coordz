/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooSQLUtil.*;
import static de.util.CooXmlDomUtil.*;

import java.sql.SQLException;
import java.util.*;

import org.w3c.dom.*;

import de.coordz.db.CooDB;
import de.coordz.db.gen.dao.DaoGateway;
import de.coordz.db.gen.inf.InfLaser;
import de.coordz.db.xml.*;
import javafx.collections.*;

public class CooGateway extends DaoGateway implements CooDBXML, CooDBLoad
{
	/** {@link List} with all gateway {@link CooLaser} */
	protected ObservableList<CooLaser> laser;
	
	public CooGateway()
	{
		laser = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element gateway = addElement(doc, root,
			"Gateway");
		gateway.setAttribute("IP", ipProperty().get());
		gateway.setAttribute("MAC", macProperty().get());
		
		laser.forEach(l -> l.toXML(doc, gateway));
	}
	
	@Override
	public void fromXML(Element gateway)
	{
		if(Objects.nonNull(gateway))
		{
			ipProperty().set(gateway.getAttribute("IP"));
			macProperty().set(gateway.getAttribute("MAC"));
			
			// Load all laser
			addToList("Laser", gateway,
				CooLaser.class, this.laser);
		}
	}
	
	@Override
	public void fromDB(CooDB database) throws SQLException
	{
		// FORTEST Select the laser
		laser.setAll(loadList(database, InfLaser.TABLE_NAME, 
			InfLaser.GATEWAYID, CooLaser.class,
			gatewayIdProperty().get()));
	}
	
	@Override
	public void delete(CooDB database) throws SQLException
	{
		// Delete all referred data
		deleteAll(database, laser);

		// Delete the DAO
		super.delete(database);
	}
	
	/**
	 * Method to access Property
	 * @return {@link #laser}
	 */
	public ObservableList<CooLaser> getLaser()
	{
		return laser;
	}
}