package tmall.comparator;

import java.util.Comparator;

import tmall.bean.Product;
//人气比较器 把评价数量多的放前面
public class ProductReviewComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		// TODO 自动生成的方法存根
		return p2.getReviewCount()-p1.getReviewCount();
	}

}
