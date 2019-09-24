package Base;

public class WeightSample {
	
	Core.Assignment e;
	double w;

	public WeightSample(Core.Assignment a, double w) {
		this.e = a;
		this.w = w;
	}

	public Core.Assignment getE() {
		return e;
	}

	public void setE(Core.Assignment e) {
		this.e = e;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	
}
