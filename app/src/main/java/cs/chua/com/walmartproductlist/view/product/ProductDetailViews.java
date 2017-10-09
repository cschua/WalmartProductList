package cs.chua.com.walmartproductlist.view.product;

import android.view.View;
import android.widget.TextView;

import cs.chua.com.walmartproductlist.R;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ProductDetailViews extends ProductItemViews {
    public final TextView shortDescriptionTextView;
    public final TextView longDescriptionTextView;

    public ProductDetailViews(final View itemView) {
        super(itemView);
        shortDescriptionTextView = itemView.findViewById(R.id.short_description_textview);
        longDescriptionTextView = itemView.findViewById(R.id.long_description_textview);
    }
}
