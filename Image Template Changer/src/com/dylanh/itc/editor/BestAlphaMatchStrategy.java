package com.dylanh.itc.editor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.dylanh.itc.util.RGBA;

public abstract class BestAlphaMatchStrategy implements ColorMappingStrategy {
	private List<RGBA> getBestAlphaMatches(int targetAlpha, Collection<RGBA> domain) {
		List<RGBA> list = new LinkedList<RGBA>();
		int bestDifference = 255;
		for (RGBA next : domain) {
			int difference = Math.abs(next.getAlpha() - targetAlpha);
			if (difference < bestDifference) {
				list.clear();
				list.add(next);
				bestDifference = difference;
			} else if (difference == bestDifference) {
				list.add(next);
			}
		}
		return list;
	}

	@Override
	public ColorMapping createMapping(Collection<RGBA> keyDomain, Collection<RGBA> mapDomain) {
		ColorMapping cm = new ColorMapping();
		for (RGBA input : keyDomain) {
			int alpha = input.getAlpha();
			List<RGBA> candidates = getBestAlphaMatches(alpha, mapDomain);
			cm.put(input, pickMatch(input, candidates));
		}

		return null;
	}

	public abstract RGBA pickMatch(RGBA input, List<RGBA> candidates);
}