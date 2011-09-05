package com.dylanh.itc.editor;

import java.util.Collection;

import com.dylanh.itc.util.RGBA;

public interface ColorMappingStrategy {
	public ColorMapping createMapping(Collection<RGBA> keyDomain, Collection<RGBA> mapDomain);
}
