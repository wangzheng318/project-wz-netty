package designmodel.prototype;

import java.io.Serializable;

public class JinGuBang implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9053427207471549413L;
	// ¸ß¶È
	private float h;
	// Ö±¾¶
	private float d;

	public void big() {
		this.h *= 2;
		this.d *= 2;
	}

	public void small() {
		this.h /= 2;
		this.d /= 2;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}
}
