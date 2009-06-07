/* *********************************************************************** *
 * project: org.matsim.*
 * ReRoute.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.core.replanning.modules;

import org.matsim.core.api.network.Network;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.router.util.DijkstraFactory;
import org.matsim.core.router.util.TravelCost;
import org.matsim.core.router.util.TravelTime;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * Uses {@link org.matsim.core.router.Dijkstra} for calculating the routes of plans during Replanning.
 *
 * @author mrieser
 */
public class ReRouteDijkstra extends AbstractMultithreadedModule {

	TravelCost costCalculator = null;
	TravelTime timeCalculator = null;
	Network network = null;
	private PlansCalcRouteConfigGroup configGroup = null;
	
	protected ReRouteDijkstra(final Network network, final TravelCost costCalculator, final TravelTime timeCalculator) {
		this.network = network;
		this.costCalculator = costCalculator;
		this.timeCalculator = timeCalculator;
		// TODO balmermi: PLEASE DOUBLECHECK/TRIPPLECHECK THE USE OF PlansCalcRouteConfigGroup
		configGroup = new PlansCalcRouteConfigGroup();
	}
	
	public ReRouteDijkstra(PlansCalcRouteConfigGroup configGroup, final Network network, final TravelCost costCalculator, final TravelTime timeCalculator) {
		this(network, costCalculator, timeCalculator);
		this.configGroup = configGroup;
		// TODO balmermi: PLEASE DOUBLECHECK/TRIPPLECHECK THE USE OF PlansCalcRouteConfigGroup
		if (this.configGroup == null) { System.err.println(">>> ARE YOU SURE THAT THE PlansCalcRouteConfigGroup IS SET TO NULL??? <<<"); }
	}
	

	@Override
	public PlanAlgorithm getPlanAlgoInstance() {
		return new PlansCalcRoute(this.configGroup, this.network, this.costCalculator, this.timeCalculator, new DijkstraFactory());
	}

}
