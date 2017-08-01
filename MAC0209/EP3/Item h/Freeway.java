/******************************************************************************
 * MAC0209 - EP3 - Cellular Automata Traffic Models
 *
 * Guilherme Costa Vieira            � NUSP 9790930
 * Joao Gabriel Basi                 � NUSP 9793801
 * Juliano Garcia de Oliveira        � NUSP 9277086
 * Pedro Pereira                     � NUSP 9778794
 * Raphael dos Reis Gusmao           � NUSP 9778561
 * Victor Chiaradia Gramuglia Araujo � NUSP 9793756
 ******************************************************************************/

package org.opensourcephysics.sip.ch14.traffic;
import java.awt.Graphics;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import org.opensourcephysics.display2d.*;
import org.opensourcephysics.controls.*;
import java.util.*;

// Freeway uses the Nagel-Schreckenberg model of single lane traffic
public class Freeway implements Drawable {
	
	public int[] v, x, xTemp;
	public LatticeFrame spaceTime;
	public double[] distribution;
	public int roadLength;
	public int numberOfCars;
	public int maximumVelocityCar;
	public int maximumVelocityTruck;
	public boolean[] isTruck;
	public double p;// Probability of reducing velocity
	private CellLattice road;
	public int lanes = 1;// Number of lanes
	public int[] lane, laneTemp;// Lane of the car i - right[0...lanes-1]left
	public double flow;
	public int steps, t;
	public int scrollTime = 100;// Number of time steps before scrolling space-time diagram
	public HistogramFrame hist1;
	public HistogramFrame hist2;
	private static int prob;
	
	public Freeway(){
		if (Freeway.prob < 1) {
			Freeway.prob = 1;
		}
	}
	
	// if probD = 1 : Uniform distribution
	// if probD = 2 : Normal distribution
	public static void setProbDistribution(int probD){
		if(probD != 1 && probD != 2) {
			throw new java.lang.IllegalArgumentException("We only accept UNIFORM or NORMAL. Get out freak!");
		}
		Freeway.prob = probD;
	}
	
	// Initializes arrays and starting configuration of cars
	public void initialize(LatticeFrame spaceTime, HistogramFrame hist1, HistogramFrame hist2) {
		this.spaceTime = spaceTime;
		this.hist1 = hist1;
		this.hist2 = hist2;
		this.x = new int[this.numberOfCars];
		this.lane = new int[this.numberOfCars];
		this.laneTemp = new int[this.numberOfCars];
		this.xTemp = new int[this.numberOfCars];// Used to allow parallel updating
		this.v = new int[this.numberOfCars];
		// Used to know if it is a car or a truck
		this.isTruck = new boolean[this.numberOfCars];
		spaceTime.resizeLattice(this.roadLength, 100);
		this.road = new CellLattice(this.roadLength, this.lanes);
		this.road.setBlock(0, 0, new byte[this.roadLength][this.lanes]);
		this.road.setIndexedColor(0, java.awt.Color.RED);
		this.road.setIndexedColor(1, java.awt.Color.GREEN);
		this.road.setIndexedColor(2, java.awt.Color.BLUE);
		spaceTime.setIndexedColor(0, java.awt.Color.RED);
		spaceTime.setIndexedColor(1, java.awt.Color.GREEN);
		spaceTime.setIndexedColor(2, java.awt.Color.BLUE);
		hist1.setDiscrete(false);
		hist2.setDiscrete(false);
		int d = this.roadLength/this.numberOfCars;
		this.lane[0] = 0;
		this.x[0] = 0;
		this.isTruck[0] = false;
		this.v[0] = this.maximumVelocityCar;
		this.road.setValue(0, this.lane[0], (byte)1);
		for (int i = 1; i < this.numberOfCars; i++) {
			// Starts at random lane
			this.isTruck[i] = probChooser(true);
			this.x[i] = this.x[i-1] + d;
			this.road.setValue(this.x[i], this.lane[i], (byte)1);
			if(probChooser(true)) {
				this.v[i] = 0;
			} else {
				this.v[i] = 1;
			}
			if (probChooser(true)) {
				this.isTruck[i] = true;
				this.v[i] = this.maximumVelocityTruck;
			}
			else {
				this.isTruck[i] = false;
				this.v[i] = this.maximumVelocityCar;
			}
		}
		this.flow = 0;
		this.steps = 0;
		this.t = 0;
	}
	
	// Returns true if a given event occurs, false otherwise
	// isFirst - true if it's the initialization, false otherwise
	// Return true if a given event occurs, false otherwise
	private boolean probChooser(boolean isFirst) {
		if (Freeway.prob == 1){// Uniform distribution
			if(isFirst) {
				return Math.random() < 0.5;
			}
			return Math.random() < this.p;
		} else {// Normal distribution
			double val = (new Random()).nextGaussian();
			if (val < -4) val = -4.0;
			else if (val > 4) val = 4.0;
			if (isFirst) {
				return val < 0;
			}
			return val < -4.0 + this.p*8;
		}
	}
	
	// Does one time step
	public void step() {
		for (int i = 0; i < this.numberOfCars; i++) {
			this.xTemp[i] = this.x[i];
			this.laneTemp[i] = this.lane[i];
		}
		for (int i = 0; i < this.numberOfCars; i++) {
			// 1 - Increase the velocity of the car by one unit if possible
			if (this.isTruck[i]) {
				if (this.v[i] < this.maximumVelocityTruck) {
					this.v[i]++; // Accelerate truck
				}
			}
			else {
				if (this.v[i] < this.maximumVelocityCar) {
					this.v[i]++; // Accelerate car
				}
			}
			// 2 - Reduce the velocity to prevent crashes or just change lane
			int d = 0;
			for (int j = this.xTemp[i]+1; j < this.roadLength; j++) {
				if (this.road.getValue(j, this.lane[i]) == (byte)1) {
					d = j - this.xTemp[i];
					break;
				}
			}
			if (d == 0) {
				for (int j = 0; j < this.xTemp[i]; j++) {
					if (this.road.getValue(j, this.lane[i]) == (byte)1) {
						d = this.xTemp[i] - j;
						break;
					}
				}
			}
			if (d == 0) {
				d = this.roadLength;
			}
			if (this.isTruck[i]) {
				if (this.v[i] >= d) {
					// More than 1 lane && there is a lane at left && v < max && there is a free space to the left
					if (this.lanes > 1 && this.lane[i]+1 < this.lanes && this.v[i] < this.maximumVelocityTruck
					 && this.road.getValue(this.xTemp[i], this.lane[i]+1) == (byte)0) {
						this.laneTemp[i]++;// Go to the left
					} else {// Otherwise
						this.v[i] = d-1;// Slow down due to cars in front
					}
				}
			}
			else {
				if (this.v[i] >= d) {
					// More than 1 lane && there is a lane at left && v < max && there is a free space to the left
					if (this.lanes > 1 && this.lane[i]+1 < this.lanes && this.v[i] < this.maximumVelocityCar
					 && this.road.getValue(this.xTemp[i], this.lane[i]+1) == (byte)0) {
						this.laneTemp[i]++;// Go to the left
					} else {// Otherwise
						this.v[i] = d-1;// Slow down due to cars in front
					}
				}
			}
			// 3 - Reduce the velocity with probability p
			if (this.v[i] > 0 && probChooser(false)) {
				this.v[i]--;
			}
			// 4 - Change lane again
			// More than 1 lane && there is a lane at right && v < max && there is a free space to the right
			if (this.lanes > 1 && this.laneTemp[i] > 0 && this.road.getValue(this.xTemp[i], this.laneTemp[i]-1) == (byte)0) {
				// If there is a car immediately behind it
				if (this.xTemp[i] > 0) {
					if (this.road.getValue(this.xTemp[i]-1, this.laneTemp[i]) == (byte)1) {
						this.laneTemp[i]--;// Go to the right
					}
				} else {// First Position
					if (this.road.getValue(this.roadLength-1, this.laneTemp[i]) == (byte)1) {
						this.laneTemp[i]--;// Go to the right
					}
				}
			}
			// 5 - Update the position of the car
			this.x[i] = (this.xTemp[i] + this.v[i])%this.roadLength;
			this.flow += this.v[i];
		}
		// Update lanes
		for (int i = 0; i < this.numberOfCars; i++) {
			this.lane[i] = this.laneTemp[i];
		}
		// Update road
		this.road.setBlock(0, 0, new byte[this.roadLength][this.lanes]);
		for (int i = 0; i < this.numberOfCars; i++) {
			if(this.isTruck[i]) {
				this.road.setValue(this.x[i], this.lane[i], (byte)2);
			}
			else {
				this.road.setValue(this.x[i], this.lane[i], (byte)1);
			}
		}
		this.steps++;
		computeSpaceTimeDiagram();
	}
	
	public void computeSpaceTimeDiagram() {
		this.t++;
		if (this.t < this.scrollTime) {
			for (int i = 0; i < this.numberOfCars; i++) {
				if (this.isTruck[i]) {
					this.spaceTime.setValue(this.x[i], this.t, 2);
				}
				else {
					this.spaceTime.setValue(this.x[i], this.t, 1);
				}
			}
		} else {// Scroll diagram
			for (int y = 0; y < this.scrollTime-1; y++) {
				for (int i = 0; i < this.roadLength; i++) {
					this.spaceTime.setValue(i, y, this.spaceTime.getValue(i, y+1));
				}
			}
			for (int i = 0; i < this.roadLength; i++) {
				this.spaceTime.setValue(i, this.scrollTime-1, 0);// Zero last row
			}
			for (int i = 0; i < this.numberOfCars; i++) {
				this.spaceTime.setValue(this.x[i], this.scrollTime-1, 1);// Add new row
			}
		}
		for (int i = 0; i < this.numberOfCars; i++) {
			this.hist1.append(this.v[i]);
		}
		for (int i = 1; i < this.numberOfCars; i++) {
			if (this.x[i] < this.x[i-1]) {
				this.hist2.append(this.x[i] + this.roadLength - this.x[i-1]);
			} else {
				this.hist2.append(this.x[i] - this.x[i-1]);
			}
		}
	}
	
	// Draws freeway
	public void draw(DrawingPanel panel, Graphics g) {
		if (this.x == null) return;
		this.road.draw(panel, g);
		g.drawString("Number of Steps = " + this.steps, 10, 20);
		g.drawString("Flow = " + ControlUtils.f3(this.flow/(this.roadLength*this.steps)), 10, 40);
		g.drawString("Density = " + ControlUtils.f3(((double)this.numberOfCars)/(this.roadLength)), 10, 60);
	}
}
