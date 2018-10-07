package designmodel.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class QiTianDaSheng extends Monkey implements Cloneable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4153276240846562202L;
	private JinGuBang jinGuBang;

	public QiTianDaSheng(JinGuBang jinGuBang) {
		this.jinGuBang = jinGuBang;
	}

	public JinGuBang getJinGuBang() {
		return jinGuBang;
	}

	public void setJinGuBang(JinGuBang jinGuBang) {
		this.jinGuBang = jinGuBang;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public Object deepClone() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}

}
