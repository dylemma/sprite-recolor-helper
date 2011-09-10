package com.dylanh.itc.alg;

import java.util.List;

import org.eclipse.swt.graphics.RGB;

import com.dylanh.itc.util.RGBA;

public class BestColorMatchStrategy extends BestAlphaMatchStrategy {

	@Override
	public RGBA pickMatch(RGBA input, List<RGBA> candidates) {
		double minDist = Double.MAX_VALUE;
		RGBA best = input;
		RGB q = input.getRgb();
		for (RGBA candidate : candidates) {
			RGB p = candidate.getRgb();
			int dx = p.red - q.red;
			int dy = p.green - q.green;
			int dz = p.blue - q.blue;
			double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
			if (dist < minDist) {
				minDist = dist;
				best = candidate;
			}
		}

		return best;
	}

}
