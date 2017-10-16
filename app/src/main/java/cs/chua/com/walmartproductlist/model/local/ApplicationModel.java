package cs.chua.com.walmartproductlist.model.local;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 10/15/17.
 */

public class ApplicationModel {
    private volatile List<Product> productList;
    private volatile PaginationCount paginationCount;

    private ApplicationModel() {
        productList = new ArrayList<>();
        paginationCount = new PaginationCount(0);
    }

    private static class SingletonHelper {
        private static final ApplicationModel INSTANCE = new ApplicationModel();
    }

    public static ApplicationModel getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void addToProductList(final List<Product> productList) {
        this.productList.addAll(productList);
    }

    public void replaceProductList(final List<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(productList);
    }

    public void updatePaginationCount(final int totalProducts) {
        paginationCount = new PaginationCount(totalProducts);
    }

    public PaginationCount getPaginationCount() {
        return paginationCount;
    }
}
