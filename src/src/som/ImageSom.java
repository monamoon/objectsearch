package som;
import java.util.*;
import net.sf.javaml.clustering.SOM;
import net.sf.javaml.clustering.SOM.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;


public class ImageSom {
	
	private Dataset dataset;
	private SOM som;
	private Vector<Instance> instances;
	private Vector<Vector<Double>> samples;
	
	public ImageSom(Vector<Vector<Double>> data) {

		setSamples(data);
		setupInstances();
		setupDataset();
		setupSOM();
	}
	
	public Dataset[] cluster(){
		return getSom().cluster(getDataset());
	}
	
	private void setupDataset(){
		setDataset(new DefaultDataset(getInstances()));	
	}
	
	
	private void setupInstances(){
		Vector<Instance> instances = new Vector<Instance>();
		for(Vector<Double> data : getSamples()){
			double[] arrayData = new double[data.size()];
			for(int i=0; i<(data.size()-1); i++)
				arrayData[i] = data.get(i);
			instances.add(new DenseInstance(arrayData, data.get(data.size()-1)));	
		}
		setInstances(instances);
	}
	
	private void setupSOM(){
		setSOM(new SOM(2, 
				2, 
				GridType.RECTANGLES, 
				40000, 
				0.01, 
				8, 
				LearningType.LINEAR, 
				NeighbourhoodFunction.GAUSSIAN, 
				new RBFKernelDistance()));
		
	}
	
	private void setSOM(SOM som){
		this.setSom(som);
	}

	private void setSamples(Vector<Vector<Double>> samples) {
		this.samples = samples;
		
	}

	public Vector<Vector<Double>> getSamples(){
		return this.samples;
	}
	
	private void setInstances(Vector<Instance> instances) {
		this.instances = instances;
		
	}
	
	public Vector<Instance> getInstances(){
		return this.instances;
	}

	public void setSom(SOM som) {
		this.som = som;
	}

	public SOM getSom() {
		return som;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Dataset getDataset() {
		return dataset;
	}
}
