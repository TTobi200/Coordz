/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.awt.event.ActionEvent;

import de.coordz.db.CooDBDao;
import javafx.beans.property.SimpleObjectProperty;

public class  CooTableDataEvent <T extends CooDBDao> extends ActionEvent
{
	/** Default serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public enum Action{ADD, EDIT, DELETE}
	
	private SimpleObjectProperty<T> dao;
	private SimpleObjectProperty<Action> action;

	public CooTableDataEvent(T dao, Action action)
	{
		super(dao, 0, null);
		this.dao = new SimpleObjectProperty<>(dao);
		this.action = new SimpleObjectProperty<>(action);
	}
	
	public SimpleObjectProperty<T> daoProperty()
	{
		return dao;
	}
	
	public SimpleObjectProperty<Action> actionProperty()
	{
		return action;
	}
}