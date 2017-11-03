package cs.chua.com.walmartproductlist.view;

import java.util.List;

import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 11/2/17.
 */

public interface ProductScreenView {
    void addLoadingView();
    void removeLoadingView();
    void updateProductListView(final List<Product> productList);
    void onError(final String error);
}
