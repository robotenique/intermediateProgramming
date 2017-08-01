/******************************************************************************
 * MAC0209 - EP3 - Cellular Automata Traffic Models
 *
 * Guilherme Costa Vieira            – NUSP 9790930
 * Joao Gabriel Basi                 – NUSP 9793801
 * Juliano Garcia de Oliveira        – NUSP 9277086
 * Pedro Pereira                     – NUSP 9778794
 * Raphael dos Reis Gusmao           – NUSP 9778561
 * Victor Chiaradia Gramuglia Araujo – NUSP 9793756
 ******************************************************************************/

package org.opensourcephysics.sip.ch14.traffic;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.*;

// FreewayApp models traffic flow by showing  the movement of the cars and a space-time diagram.
// Time is on the vertical axis and space on the horizontal axis.
public class FreewayApp extends AbstractSimulation {
	
	Freeway freeway = new Freeway();
	DisplayFrame display = new DisplayFrame("Freeway");
	LatticeFrame spaceTime = new LatticeFrame("space", "time", "Space Time Diagram");
	HistogramFrame hist1 = new HistogramFrame("Vel", "", "Velocity Histogram");
	HistogramFrame hist2 = new HistogramFrame("Gap", "", "Gap Histogram");
	
	// Constructs the FreewayApp.
	public FreewayApp() {
		this.display.addDrawable(this.freeway);
	}
	
	// Initializes the animation using the values in the control.
	public void initialize() {
		this.freeway.numberOfCars = this.control.getInt("Number of cars");
		this.freeway.roadLength = this.control.getInt("Road length");
		this.freeway.lanes = this.control.getInt("Number of lanes");
		this.freeway.p = this.control.getDouble("Slow down probability");
		this.freeway.maximumVelocity = this.control.getInt("Maximum velocity");
		this.display.setPreferredMinMax(0, this.freeway.roadLength, -3, 4);
		Freeway.setProbDistribution(this.control.getInt("Prob (1 = Uniform, 2 = Normal)"));
		this.freeway.initialize(this.spaceTime, this.hist1, this.hist2);
	}
	
//	public void initialize2(int cars, int road) {
//		this.freeway.numberOfCars = cars;
//		this.freeway.roadLength = road;
//		this.freeway.p = 0.5;
//		this.freeway.maximumVelocity = 2;
//		this.display.setPreferredMinMax(0, this.freeway.roadLength, -3, 4);
//		Freeway.setProbDistribution(1);
//		this.freeway.initialize(this.spaceTime, this.hist1, this.hist2);
//	}
	
	// Does one iteration
	public void doStep() {
		this.freeway.step();
	}
	
	public double getFlow() {
		return this.freeway.flow/50000;
	}
	
	// Resets animation to a predefined state
	public void reset() {
		this.control.setValue("Number of cars", 50);
		this.control.setValue("Road length", 100);
		this.control.setValue("Number of lanes", 1);
		this.control.setValue("Slow down probability", 0.5);
		this.control.setValue("Maximum velocity", 2);
		this.control.setValue("Steps between plots", 1);
		this.control.setValue("Prob (1 = Uniform, 2 = Normal)", 1);
		enableStepsPerDisplay(true);
	}
	
	// Resets data without changing configuration
	public void resetAverages() {
		this.freeway.flow = 0;
		this.freeway.steps = 0;
	}
	
	// Starts Java application
	public static void main(String[] args) {
		SimulationControl control = SimulationControl.createApp(new FreewayApp());
		control.addButton("resetAverages", "resetAverages");
	}
	
	// Number of cars X flow main
	/*public static void main(String[] args) {
		FreewayApp freeway = new FreewayApp();
		PlotFrame plot = new PlotFrame("Number of cars", "Flow", "Flow per number of cars");
		plot.setVisible(true);
		for (int i = 1; i < 500; i++) {
			freeway.initialize2(i, 500);
			for (int j = 0; j < 1000; j++) {
				freeway.doStep();
			}
			plot.append(0, i, freeway.getFlow());
			plot.render();
		}
	}*/
	
	// Road size X flow main
	/*public static void main(String[] args) {
		FreewayApp freeway = new FreewayApp();
		PlotFrame plot = new PlotFrame("Road size", "Flow", "Flow X Road Size");
		plot.setVisible(true);
		for (int i = 41; i < 500; i++) {
			freeway.initialize2(40, i);
			for (int j = 0; j < 1000; j++) {
				freeway.doStep();
			}
			plot.append(0, i, freeway.getFlow());
			plot.render();
		}
	}*/
}
