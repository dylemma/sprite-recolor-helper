package com.dylanh.itc.editor;

import java.util.Collection;

import org.eclipse.swt.graphics.RGB;

public interface ColorMappingStrategy {
	public ColorMapping createMapping(Collection<RGB> keyDomain, Collection<RGB> mapDomain);
}
