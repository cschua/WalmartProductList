package cs.chua.com.walmartproductlist.view.product;

import android.view.View;

import cs.chua.com.walmartproductlist.view.BaseRecyclerViewHolder;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductItemViewHolder extends BaseRecyclerViewHolder {
    final ProductItemViews productViews;

    public ProductItemViewHolder(final View itemView) {
        super(itemView);
        productViews = new ProductItemViews(itemView);
    }

    public ProductItemViews getProductViews() {
        return productViews;
    }
}
