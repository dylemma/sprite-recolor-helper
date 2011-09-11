package com.dylanh.itc.data;

import static com.dylanh.itc.util.Rand.rand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.dylanh.itc.alg.ColorMappingStrategy;
import com.dylanh.itc.util.RGBA;

public class ColorMapping
{
	// extends HashMap<RGBA, RGBA> {

	private static final long serialVersionUID = 8504032073542856018L;

	public static interface Listener
	{
		public void mappingChanged(ColorMapping mapRef, RGBA key, RGBA oldValue, RGBA newValue);
	}

	private final Set<Listener> listeners = new HashSet<Listener>();

	public void addListener(Listener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(Listener listener)
	{
		listeners.remove(listener);
	}

	public void fireMappingChanged(RGBA key, RGBA oldVal, RGBA newVal)
	{
		for (Listener listener : listeners)
		{
			listener.mappingChanged(this, key, oldVal, newVal);
		}
	}

	private final HashMap<RGBA, RGBA> innerMap = new HashMap<RGBA, RGBA>();

	public RGBA put(RGBA key, RGBA newValue)
	{
		RGBA oldValue = innerMap.put(key, newValue);
		fireMappingChanged(key, oldValue, newValue);
		return oldValue;
	}

	public RGBA get(RGBA key)
	{
		return innerMap.get(key);
	}

	public Set<Entry<RGBA, RGBA>> entrySet()
	{
		return innerMap.entrySet();
	}

	public static ColorMappingStrategy randomStrategy = new ColorMappingStrategy()
	{
		@Override
		public ColorMapping createMapping(java.util.Collection<RGBA> keyDomain, java.util.Collection<RGBA> mapDomain)
		{
			RGBA[] lefts = keyDomain.toArray(new RGBA[keyDomain.size()]);
			RGBA[] rights = mapDomain.toArray(new RGBA[mapDomain.size()]);
			ColorMapping cm = new ColorMapping();
			for (RGBA key : lefts)
			{
				RGBA value = rights[rand.nextInt(rights.length)];
				cm.put(key, value);
			}

			return cm;
		}
	};

}
