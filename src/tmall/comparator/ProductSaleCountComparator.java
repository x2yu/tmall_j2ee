package tmall.comparator;

import java.util.Comparator;

import tmall.bean.Product;
// 销量比较器 把销量高的放前面
public class ProductSaleCountComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		// TODO 自动生成的方法存根
		return p2.getSaleCount()-p1.getSaleCount();
	}

}
