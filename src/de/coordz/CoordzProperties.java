/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz;

import java.io.File;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.ObservableMap;

import com.sun.javafx.collections.ObservableMapWrapper;

public abstract class CoordzProperties extends
				ObservableMapWrapper<String, Property<?>> implements
				ObservableMap<String, Property<?>>
{
	public static final String XML_FILE_START = "<?xml";
	public static final String XML_ROOT_ELEMENT = "Coordz-Settings";

	public CoordzProperties()
	{
		this(new HashMap<>());
	}

	public CoordzProperties(Map<String, Property<?>> map)
	{
		super(map);
	}

	@SuppressWarnings("unchecked")
	public <P extends Property<?>> P getProperty(String key)
		throws ClassCastException
	{
		return (P)get(key);
	}

	@SuppressWarnings("unchecked")
	public <P extends Property<?>> P getProperty(String key, Object def)
		throws ClassCastException
	{
		if(!containsKey(key))
		{
			putObject(key, def);
		}

		return (P)get(key);
	}

	public boolean getBoolean(String key) throws ClassCastException
	{
		return getBooleanProperty(key).get();
	}

	public boolean getBoolean(String key, boolean def)
		throws ClassCastException
	{
		return getBooleanProperty(key, def).get();
	}

	public long getLong(String key) throws ClassCastException
	{
		return getLongProperty(key).get();
	}

	public long getLong(String key, long def) throws ClassCastException
	{
		return getLongProperty(key, def).get();
	}

	public int getInt(String key) throws ClassCastException
	{
		return getIntProperty(key).get();
	}

	public int getInt(String key, int def) throws ClassCastException
	{
		return getIntProperty(key, def).get();
	}

	public float getFloat(String key) throws ClassCastException
	{
		return getFloatProperty(key).get();
	}

	public float getFloat(String key, float def) throws ClassCastException
	{
		return getFloatProperty(key, def).get();
	}

	public double getDouble(String key) throws ClassCastException
	{
		return getDoubleProperty(key).get();
	}

	public double getDouble(String key, double def) throws ClassCastException
	{
		return getDoubleProperty(key, def).get();
	}

	public String getString(String key) throws ClassCastException
	{
		return getStringProperty(key).get();
	}

	public String getString(String key, String def) throws ClassCastException
	{
		return getStringProperty(key, def).get();
	}

	public <O> O getObject(String key) throws ClassCastException
	{
		return this.<O> getObjectProperty(key).get();
	}

	public <O> O getObject(String key, Object def) throws ClassCastException
	{
		return this.<O> getObjectProperty(key, def).get();
	}

	public BooleanProperty getBooleanProperty(String key)
		throws ClassCastException
	{
		return this.<BooleanProperty> getProperty(key);
	}

	public BooleanProperty getBooleanProperty(String key, boolean def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.BOOL)
		{
			putBoolean(key, def);
		}
		return getBooleanProperty(key);
	}

	public LongProperty getLongProperty(String key) throws ClassCastException
	{
		return this.<LongProperty> getProperty(key);
	}

	public LongProperty getLongProperty(String key, long def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.LONG)
		{
			putLong(key, def);
		}
		return getLongProperty(key);
	}

	public IntegerProperty getIntProperty(String key) throws ClassCastException
	{
		return this.<IntegerProperty> getProperty(key);
	}

	public IntegerProperty getIntProperty(String key, int def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.INT)
		{
			putInt(key, def);
		}
		return getIntProperty(key);
	}

	public FloatProperty getFloatProperty(String key) throws ClassCastException
	{
		return this.<FloatProperty> getProperty(key);
	}

	public FloatProperty getFloatProperty(String key, float def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.FLOAT)
		{
			putFloat(key, def);
		}
		return getFloatProperty(key);
	}

	public DoubleProperty getDoubleProperty(String key)
		throws ClassCastException
	{
		return this.<DoubleProperty> getProperty(key);
	}

	public DoubleProperty getDoubleProperty(String key, double def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.DOUBLE)
		{
			putDouble(key, def);
		}
		return getDoubleProperty(key);
	}

	public StringProperty getStringProperty(String key)
		throws ClassCastException
	{
		return this.<StringProperty> getProperty(key);
	}

	public StringProperty getStringProperty(String key, String def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.STRING)
		{
			putString(key, def);
		}
		return getStringProperty(key);
	}

	public <O> ObjectProperty<O> getObjectProperty(String key)
		throws ClassCastException
	{
		return this.<ObjectProperty<O>> getProperty(key);
	}

	public <O> ObjectProperty<O> getObjectProperty(String key, Object def)
		throws ClassCastException
	{
		if(getType(key) != CoordzPropertyType.OBJECT)
		{
			putObject(key, def);
		}
		return getObjectProperty(key);
	}

	public Property<?> putBoolean(String key, boolean value)
	{
		if(getType(key) == CoordzPropertyType.BOOL)
		{
			getBooleanProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleBooleanProperty(value));
		}
	}

	public Property<?> putLong(String key, long value)
	{
		if(getType(key) == CoordzPropertyType.LONG)
		{
			getLongProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleLongProperty(value));
		}
	}

	public Property<?> putInt(String key, int value)
	{
		if(getType(key) == CoordzPropertyType.INT)
		{
			getIntProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleIntegerProperty(value));
		}
	}

	public Property<?> putFloat(String key, float value)
	{
		if(getType(key) == CoordzPropertyType.FLOAT)
		{
			getFloatProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleFloatProperty(value));
		}
	}

	public Property<?> putDouble(String key, double value)
	{
		if(getType(key) == CoordzPropertyType.DOUBLE)
		{
			getDoubleProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleDoubleProperty(value));
		}
	}

	public Property<?> putString(String key, String value)
	{
		if(getType(key) == CoordzPropertyType.STRING)
		{
			getStringProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleStringProperty(value));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Property<?> putObject(String key, Object value)
	{
		if(getType(key) == CoordzPropertyType.OBJECT)
		{
			getObjectProperty(key).set(value);
			return get(key);
		}
		else
		{
			return put(key, new SimpleObjectProperty(value));
		}
	}

	public CoordzPropertyType getType(String key)
	{
		Property<?> prop = get(key);

		if(Objects.isNull(prop))
		{
			return CoordzPropertyType.NULL;
		}

		if(prop instanceof BooleanProperty)
		{
			return CoordzPropertyType.BOOL;
		}
		else if(prop instanceof LongProperty)
		{
			return CoordzPropertyType.LONG;
		}
		else if(prop instanceof IntegerProperty)
		{
			return CoordzPropertyType.INT;
		}
		else if(prop instanceof FloatProperty)
		{
			return CoordzPropertyType.FLOAT;
		}
		else if(prop instanceof DoubleProperty)
		{
			return CoordzPropertyType.DOUBLE;
		}
		else if(prop instanceof StringProperty)
		{
			return CoordzPropertyType.STRING;
		}

		return CoordzPropertyType.OBJECT;
	}

	public static enum CoordzPropertyType
	{
		BOOL, LONG, INT, FLOAT, DOUBLE, STRING, OBJECT, NULL;
	}

	public abstract void load(File file);

	public abstract boolean save(File file);
}
