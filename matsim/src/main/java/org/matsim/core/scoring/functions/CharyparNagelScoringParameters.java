/* *********************************************************************** *
 * project: org.matsim.*
 * CharyparNagelScoringParameters.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
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

package org.matsim.core.scoring.functions;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.matsim.core.api.internal.MatsimParameters;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.pt.PtConstants;

public class CharyparNagelScoringParameters implements MatsimParameters {
	public final Map<String, ActivityUtilityParameters> utilParams ;
	public final double marginalUtilityOfWaiting_s;
	public final double marginalUtilityOfLateArrival_s;
	public final double marginalUtilityOfEarlyDeparture_s;
	public final double marginalUtilityOfTraveling_s;
	public final double marginalUtilityOfTravelingPT_s; // public transport
	public final double marginalUtilityOfTravelingBike_s;
	public final double marginalUtilityOfTravelingWalk_s;
	public final double marginalUtilityOfTravelingOther_s;
	public final double marginalUtilityOfPerforming_s;
	
	public final double constantCar ;
	public final double constantWalk ;
	public final double constantBike ;
	public final double constantPt ;
	public final double constantOther ;

	@Deprecated
	public final double marginalUtilityOfDistanceCar_m;
	@Deprecated
	public final double marginalUtilityOfDistancePt_m;
	@Deprecated
	public final double marginalUtilityOfDistanceOther_m;

	public final double marginalUtilityOfDistanceWalk_m;

	public final double monetaryDistanceCostRateCar;
	public final double monetaryDistanceCostRatePt;
	public final double marginalUtilityOfMoney;

	public final double abortedPlanScore;

	/** True if at least one of marginal utilities for performing, waiting, being late or leaving early is not equal to 0. */
	public final boolean scoreActs;

	public CharyparNagelScoringParameters(final PlanCalcScoreConfigGroup config) {
		marginalUtilityOfWaiting_s = config.getMarginalUtlOfWaiting_utils_hr() / 3600.0;
		marginalUtilityOfLateArrival_s = config.getLateArrival_utils_hr() / 3600.0;
		marginalUtilityOfEarlyDeparture_s = config.getEarlyDeparture_utils_hr() / 3600.0;
		marginalUtilityOfTraveling_s = config.getTraveling_utils_hr() / 3600.0;
		marginalUtilityOfTravelingPT_s = config.getTravelingPt_utils_hr() / 3600.0;
		marginalUtilityOfTravelingBike_s = config.getTravelingBike_utils_hr() / 3600.0;
		marginalUtilityOfTravelingWalk_s = config.getTravelingWalk_utils_hr() / 3600.0;
		marginalUtilityOfTravelingOther_s = config.getTravelingOther_utils_hr() / 3600.0;
		marginalUtilityOfPerforming_s = config.getPerforming_utils_hr() / 3600.0;
		
		constantCar = config.getConstantCar() ;
		constantBike = config.getConstantBike() ;
		constantWalk = config.getConstantWalk() ;
		constantPt = config.getConstantPt() ;
		constantOther = config.getConstantOther() ;

		marginalUtilityOfDistanceCar_m = config.getMonetaryDistanceCostRateCar() * config.getMarginalUtilityOfMoney() ;
		marginalUtilityOfDistancePt_m = config.getMonetaryDistanceCostRatePt() * config.getMarginalUtilityOfMoney() ;

		marginalUtilityOfDistanceWalk_m = config.getMarginalUtlOfDistanceWalk();
		marginalUtilityOfDistanceOther_m = config.getMarginalUtlOfDistanceOther();

		monetaryDistanceCostRateCar = config.getMonetaryDistanceCostRateCar() ;
		monetaryDistanceCostRatePt = config.getMonetaryDistanceCostRatePt();
		marginalUtilityOfMoney = config.getMarginalUtilityOfMoney() ;

		abortedPlanScore = Math.min(
				Math.min(marginalUtilityOfLateArrival_s, marginalUtilityOfEarlyDeparture_s),
				Math.min(marginalUtilityOfTraveling_s, marginalUtilityOfWaiting_s)) * 3600.0 * 24.0; // SCENARIO_DURATION
		// TODO 24 has to be replaced by a variable like scenario_dur (see also other places below)
		// This rather complicated definition has to do with the fact that exp(some_large_number) relatively quickly becomes Inf.
		// In consequence, the abortedPlanScore needs to be more strongly negative than anything else, but not much more.  
		// kai, feb'12

		scoreActs = marginalUtilityOfPerforming_s != 0 || marginalUtilityOfWaiting_s != 0 ||
				marginalUtilityOfLateArrival_s != 0 || marginalUtilityOfEarlyDeparture_s != 0;

		SortedMap<String,ActivityUtilityParameters> tmpUtlParams = new TreeMap<String, ActivityUtilityParameters>() ;
		for (ActivityParams params : config.getActivityParams()) {
			
			ActivityUtilityParameters.Factory factory = new ActivityUtilityParameters.Factory(params) ;
			
			// the following was introduced in nov'12.  Also see setupTransitSimulation in Controler.  kai, nov'12
			if (params.getType().equals(PtConstants.TRANSIT_ACTIVITY_TYPE)) {
				factory.setScoreAtAll(false) ;
			}
			tmpUtlParams.put(params.getType(), factory.create() ) ;

		}
		utilParams = Collections.unmodifiableMap(tmpUtlParams );

	}

}
