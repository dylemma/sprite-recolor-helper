package com.dylanh.itc.alg;

import java.util.Collection;

import com.dylanh.itc.data.ColorMapping;
import com.dylanh.itc.util.RGBA;

public interface ColorMappingStrategy {
	public ColorMapping createMapping(Collection<RGBA> keyDomain, Collection<RGBA> mapDomain);
}
