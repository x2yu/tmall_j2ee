package tmall.comparator;

import java.util.Comparator;

import tmall.bean.Product;

//综合比较器 把 销量x评价 高的放前面

public class ProductAllComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		// TODO 自动生成的方法存根
		return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
	}
	
	
}
