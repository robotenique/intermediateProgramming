/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */
package org.opensourcephysics.sip.ch14.traffic;
import java.awt.Graphics;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display2d.*;
import org.opensourcephysics.controls.*;

/**
 * Freeway uses the Nagel-Schreckenberg model of single lane traffic
 *
 * @author Jan Tobochnik, Wolfgang Christiann, Harvey Gould
 * @version 1.0  revised 06/24/05
 */
public class Freeway implements Drawable {
  public int[] v, x, xtemp;
  public LatticeFrame spaceTime;
  public double[] distribution;
  public int roadLength;
  public int numberOfCars;
  public int maximumVelocity;
  public double p;             // probability of reducing velocity
  private CellLattice road;
  public int offRamp;
  public int onRamp;
  public boolean isInLimbo = false;
  public int carInLimbo;
  public double flow;
  public int steps, t;
  public int scrollTime = 100; // number of time steps before scrolling space-time diagram
  
  public void initialize(LatticeFrame spaceTime) {
	this.spaceTime = spaceTime;
    x = new int[numberOfCars];
    xtemp = new int[numberOfCars]; // used to allow parallel updating
    v = new int[numberOfCars];
    spaceTime.resizeLattice(roadLength, 100);
    road = new CellLattice(roadLength, 1);
    road.setIndexedColor(0, java.awt.Color.RED);
    road.setIndexedColor(1, java.awt.Color.GREEN);
    spaceTime.setIndexedColor(0, java.awt.Color.RED);
    spaceTime.setIndexedColor(1, java.awt.Color.GREEN);
    int d = roadLength/numberOfCars;
    
    x[0] = 0;
    v[0] = maximumVelocity;
    for(int i = 1;i<numberOfCars;i++) {
      x[i] = x[i-1]+d;
      
      if(Math.random()<0.5) {
        v[i] = 0;
      } else {
        v[i] = 1;
      }
    }
    flow = 0;
    steps = 0;
    t = 0;
  }
  
  /**
   * Does one time step
   */  
  public void step() { 
	
	  for(int i = 0;i<numberOfCars;i++) {
      xtemp[i] = x[i];
    }
	
	if(isInLimbo == false) {
		carInLimbo = (int) (Math.random()*(numberOfCars - 1));
		isInLimbo = true;
	}
	
    for(int i = 0;i<numberOfCars;i++) {
      if(v[i]<maximumVelocity && i != carInLimbo) {
        v[i]++;                                   // acceleration
      } else if (i == carInLimbo) {
    	  if (offRamp != onRamp) { // if they are equal, the problem wont simulate onramp and offramp
    		  if(xtemp[i] < offRamp) { //decrease velocity until it arrives to offramp
    			  if (v[i] > 1) {
    			  	v[i]--;
    		  	  }
    			  if (v[i] == 0) { //with probability p, it can turn the velocity to zero, we fix that here
    				  v[i] = 1;
    			  }
    		  }
    		  else if (xtemp[i] > offRamp && v[i] < maximumVelocity){ //increase the velocity if it already passed the offramp 
    			  v[i]++;
    		  }
    		  else if (xtemp[i] == offRamp) { //take the car out of the lane
    			  xtemp[i] = -1;
    		  }
    	  } else if(v[i] < maximumVelocity) { //increase the velocity
    		  v[i]++;
    	  }
      }
      
      if(x[(i+1)%numberOfCars] == -1) { //calculate the distance when the car in front is in limbo
     	 int d = xtemp[(i+2)%numberOfCars]-xtemp[i]; // distance between cars
         if(d<=0) {                                  // periodic boundary conditions, d = 0 correctly treats one car on road
           d += roadLength;
         }
         if(v[i]>=d) {
           v[i] = d-1; // slow down due to cars in front
         }  
      } 
      else if(x[i] != -1) {
    	int d = xtemp[(i+1)%numberOfCars]-xtemp[i]; // distance between cars
        if(d<=0) {                                  // periodic boundary conditions, d = 0 correctly treats one car on road
          d += roadLength;
        }
        if(v[i]>=d) {
          v[i] = d-1; // slow down due to cars in front
        }
      }
      
      if((v[i]>0)&&(Math.random()<p)) {
        v[i]--;     // randomization
      }
      
      if(x[i] != -1) {
        x[i] = (xtemp[i]+v[i])%roadLength;
      }
      
      else if(x[(i+1)%numberOfCars] > onRamp) {
    	  boolean flag = false;
    	  for(int j = 0; j < numberOfCars;j++) {
    		  if(xtemp[j] == onRamp) {
    			flag = true;
    			break;
    		  }
    	  }
    	  if(flag == false) {
    	    x[i] = onRamp;
    	    isInLimbo = false;
    	  }
      }
      flow += v[i];
    }
    steps++;
    computeSpaceTimeDiagram();
  }
  
  public void computeSpaceTimeDiagram() {
    t++;
    if(t<scrollTime) {
      for(int i = 0;i<numberOfCars;i++) {
        if(x[i] != -1) {
    	  spaceTime.setValue(x[i], t, 1);
        }  
      }
    } 
    else {                                       // scroll diagram
      for(int y = 0;y<scrollTime-1;y++) {
        for(int i = 0;i<roadLength;i++) {
          spaceTime.setValue(i, y, spaceTime.getValue(i, y+1));
         }
      }
      for(int i = 0;i<roadLength;i++) {
        spaceTime.setValue(i, scrollTime-1, 0);    // zero last row
      }
      for(int i = 0;i<numberOfCars;i++) {
    	if(x[i] != -1) {
    	  spaceTime.setValue(x[i], scrollTime-1, 1); // add new row
    	}
      }
    }
  }
  
  public void draw(DrawingPanel panel, Graphics g) {
	if(x==null) {
      return;
    }
    road.setBlock(0, 0, new byte[roadLength][1]);
    for(int i = 0;i<numberOfCars;i++) {
      if(x[i] != -1) {
    	road.setValue(x[i], 0, (byte) 1);
      }
    }
    road.draw(panel, g);
    g.drawString("Number of Steps = "+steps, 10, 20);
    g.drawString("Flow = "+ControlUtils.f3(flow/(roadLength*steps)), 10, 40);
    g.drawString("Density = "+ControlUtils.f3(((double) numberOfCars)/(roadLength)), 10, 60);
  }
}

/* 
 * Open Source Physics software is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.

 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be released
 * under the GNU GPL license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307 USA
 * or view the license online at http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */