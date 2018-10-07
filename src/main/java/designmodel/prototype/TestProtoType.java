package designmodel.prototype;

public class TestProtoType {
	public static void main(String[] args) throws Exception {
		QiTianDaSheng qiTianDaSheng = new QiTianDaSheng(new JinGuBang());
		QiTianDaSheng copy = (QiTianDaSheng) qiTianDaSheng.clone();
		//QiTianDaSheng copy = (QiTianDaSheng) qiTianDaSheng.deepClone();
		
		System.out.println(qiTianDaSheng.getJinGuBang() == copy.getJinGuBang());
	}
}
