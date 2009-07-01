/* *********************************************************************** *
 * project: org.matsim.*
 * FacilitiesParserWriterTest.java
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

package org.matsim.core.facilities;

import org.matsim.api.basic.v01.Coord;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.api.facilities.ActivityFacilities;
import org.matsim.core.api.facilities.ActivityFacility;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.network.NodeImpl;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.world.World;
import org.matsim.world.ZoneLayer;

public class FacilityMoveTest extends MatsimTestCase {

	//////////////////////////////////////////////////////////////////////
	// member variables
	//////////////////////////////////////////////////////////////////////

	private static final Id ZONE_ID = new IdImpl("zone");
	
	private static final Coord[] COORDS = {
		new CoordImpl(1.0,1.0), new CoordImpl(0.5,1.0), new CoordImpl(0.5,0.5),
		new CoordImpl(0.0,0.0), new CoordImpl(-0.5,-0.5)
	};
	private static final String[] F_UP_ZONEID = { "z", "z", "z", "z00", null };
	private static final String[] F_DOWN_LINKID = { "l0011", "l0211", "l0211", "l0011", "l0011" };
	
	//////////////////////////////////////////////////////////////////////
	// private methods
	//////////////////////////////////////////////////////////////////////

	private final World buildWorld() {
		World world = new World();
		ZoneLayer zones = (ZoneLayer)world.createLayer(ZONE_ID,null);
		zones.createZone("z00","0.5","0.5","0.0","0.0","1.0","1.0","1","z00");
		zones.createZone("z01","1.5","0.5","1.0","0.0","2.0","1.0","1","z01");
		zones.createZone("z10","0.5","1.5","0.0","1.0","1.0","2.0","1","z10");
		zones.createZone("z11","1.5","1.5","1.0","1.0","2.0","2.0","1","z11");
		zones.createZone("z","1.0","1.0","0.5","0.5","1.5","1.5","1","z");
		return world;
	}
	
	private final ActivityFacilities buildFacilities(World world) {
		ActivityFacilities facilities = (ActivityFacilities)world.createLayer(ActivityFacilities.LAYER_TYPE,null);
		facilities.createFacility(new IdImpl("f11"), new CoordImpl(1, 1));
		return facilities;
	}
	
	private final NetworkLayer buildNetwork(World world) {
		NetworkLayer network = (NetworkLayer)world.createLayer(NetworkLayer.LAYER_TYPE,null);
		NodeImpl n00 = network.createNode(new IdImpl("n00"), new CoordImpl("0.0","0.0"));
		NodeImpl n02 = network.createNode(new IdImpl("n02"), new CoordImpl("0.0","2.0"));
		NodeImpl n11 = network.createNode(new IdImpl("n11"), new CoordImpl("1.0","1.0"));
		NodeImpl n20 = network.createNode(new IdImpl("n20"), new CoordImpl("2.0","0.0"));
		NodeImpl n22 = network.createNode(new IdImpl("n22"), new CoordImpl("2.0","2.0"));
		network.createLink(new IdImpl("l0011"),n00,n11,2,1,2000,1);
		network.createLink(new IdImpl("l0211"),n02,n11,2,1,2000,1);
		network.createLink(new IdImpl("l2011"),n20,n11,2,1,2000,1);
		network.createLink(new IdImpl("l2211"),n22,n11,2,1,2000,1);
		return network;
	}
	
	private final void validate(World world, int i) {
		ActivityFacility f = (ActivityFacility)world.getLayer(ActivityFacilities.LAYER_TYPE).getLocations().values().iterator().next();
		if (world.getLayer(ZONE_ID) != null) {
			if (f.getUpMapping().isEmpty()) { assertNull(F_UP_ZONEID[i]); }
			else { assertEquals(F_UP_ZONEID[i],f.getUpMapping().values().iterator().next().getId().toString()); }
		}
		else { assertEquals(0,f.getUpMapping().size()); }
		
		if (world.getLayer(NetworkLayer.LAYER_TYPE) != null) { assertEquals(F_DOWN_LINKID[i],f.getLink().getId().toString()); }
		else { assertEquals(0,f.getDownMapping().size()); }
	}
	
	//////////////////////////////////////////////////////////////////////
	// tests
	//////////////////////////////////////////////////////////////////////

	public void testMoveFacility1() {
		System.out.println("running testMoveFacility1()...");
		
		World world = new World();
		ActivityFacilities facilities = buildFacilities(world);
		world.complete();

		ActivityFacility f = facilities.getFacilities().values().iterator().next();
		for (int i=0; i<COORDS.length; i++) {
			f.moveTo(COORDS[i]);
			validate(world,i);
		}

		System.out.println("done.");
	}

	//////////////////////////////////////////////////////////////////////

	public void testMoveFacility2() {
		System.out.println("running testMoveFacility2()...");
		
		World world = buildWorld();
		ActivityFacilities facilities = buildFacilities(world);
		world.complete();
		
		ActivityFacility f = facilities.getFacilities().values().iterator().next();
		for (int i=0; i<COORDS.length; i++) {
			f.moveTo(COORDS[i]);
			validate(world,i);
		}

		System.out.println("done.");
	}

	//////////////////////////////////////////////////////////////////////

	public void testMoveFacility3() {
		System.out.println("running testMoveFacility3()...");
		
		World world = new World();
		ActivityFacilities facilities = buildFacilities(world);
		buildNetwork(world);
		world.complete();
		
		ActivityFacility f = facilities.getFacilities().values().iterator().next();
		for (int i=0; i<COORDS.length; i++) {
			f.moveTo(COORDS[i]);
			validate(world,i);
		}

		System.out.println("done.");
	}

	//////////////////////////////////////////////////////////////////////

	public void testMoveFacility4() {
		System.out.println("running testMoveFacility4()...");
		
		World world = buildWorld();
		ActivityFacilities facilities = buildFacilities(world);
		buildNetwork(world);
		world.complete();
		
		ActivityFacility f = facilities.getFacilities().values().iterator().next();
		for (int i=0; i<COORDS.length; i++) {
			f.moveTo(COORDS[i]);
			validate(world,i);
		}

		System.out.println("done.");
	}
}
