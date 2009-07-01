/* *********************************************************************** *
 * project: org.matsim.*
 * NetworkFactoryTest.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.core.network;

import org.matsim.api.basic.v01.TransportMode;
import org.matsim.core.api.population.GenericRoute;
import org.matsim.core.api.population.Route;
import org.matsim.core.population.routes.AbstractRoute;
import org.matsim.core.population.routes.NodeNetworkRoute;
import org.matsim.core.population.routes.RouteFactory;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author mrieser
 */
public class NetworkFactoryTest extends MatsimTestCase {

	public void testSetRouteFactory() {
		NetworkFactory factory = new NetworkFactory();

		// test default
		Route carRoute = factory.createRoute(TransportMode.car, null, null);
		assertTrue(carRoute instanceof NodeNetworkRoute);

		Route route = factory.createRoute(TransportMode.pt, null, null);
		assertTrue(route instanceof GenericRoute);

		// overwrite car-mode
		factory.setRouteFactory(TransportMode.car, new CarRouteMockFactory());
		// add pt-mode
		factory.setRouteFactory(TransportMode.pt, new PtRouteMockFactory());

		// test car-mode
		carRoute = factory.createRoute(TransportMode.car, null, null);
		assertTrue(carRoute instanceof CarRouteMock);

		// add pt-mode
		Route ptRoute = factory.createRoute(TransportMode.pt, null, null);
		assertTrue(ptRoute instanceof PtRouteMock);

		// remove pt-mode
		factory.setRouteFactory(TransportMode.pt, null);

		// test pt again
		route = factory.createRoute(TransportMode.pt, null, null);
		assertTrue(route instanceof GenericRoute);

	}
	
	/*package*/ static class CarRouteMock extends AbstractRoute {
		CarRouteMock(LinkImpl startLink, LinkImpl endLink){
			super(startLink, endLink);
		}
	}

	/*package*/ static class PtRouteMock extends AbstractRoute {
		PtRouteMock(LinkImpl startLink, LinkImpl endLink){
			super(startLink, endLink);
		}
	}
	
	/*package*/ static class CarRouteMockFactory implements RouteFactory {
		public Route createRoute(LinkImpl startLink, LinkImpl endLink) {
			return new CarRouteMock(startLink, endLink);
		}

	}

	/*package*/ static class PtRouteMockFactory implements RouteFactory {
		public Route createRoute(LinkImpl startLink, LinkImpl endLink) {
			return new PtRouteMock(startLink, endLink);
		}

	}
}
