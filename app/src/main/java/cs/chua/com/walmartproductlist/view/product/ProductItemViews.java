package cs.chua.com.walmartproductlist.view.product;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cs.chua.com.walmartproductlist.R;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ProductItemViews {
    public final ImageView imageView;
    public final TextView priceTextView;
    public final TextView nameTextView;
    public final RatingBar ratingStars;
    public final TextView ratingCountTextView;
    public final TextView outOfStockTextView;

    public ProductItemViews(final View itemView) {
        imageView = itemView.findViewById(R.id.imageview);
        priceTextView = itemView.findViewById(R.id.price_textview);
        nameTextView = itemView.findViewById(R.id.name_textview);
        ratingStars = itemView.findViewById(R.id.rating_stars);
        ratingCountTextView = itemView.findViewById(R.id.rating_count);
        outOfStockTextView = itemView.findViewById(R.id.outofstock_textview);
    }
}
