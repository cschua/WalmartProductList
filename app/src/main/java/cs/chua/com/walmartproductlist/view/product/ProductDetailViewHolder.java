package cs.chua.com.walmartproductlist.view.product;

import android.view.View;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductDetailViewHolder {
    private ProductDetailViews productViews;

    public ProductDetailViewHolder(final View itemView) {
        productViews = new ProductDetailViews(itemView);
    }

    public ProductDetailViews getProductViews() {
        return productViews;
    }
}
