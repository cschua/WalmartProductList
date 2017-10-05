package cs.chua.com.walmartproductlist.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cs.chua.com.walmartproductlist.R;

/**
 * Created by christopherchua on 10/3/17.
 */

public class ProductViewHolder extends BaseViewHolder {
    public final ImageView imageView;
    public final TextView priceTextView;
    public final TextView nameTextView;
    public final RatingBar ratingStars;
    public final TextView ratingCountTextView;
    public final TextView outOfStockTextView;

    public ProductViewHolder(final View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        priceTextView = itemView.findViewById(R.id.price_textview);
        nameTextView = itemView.findViewById(R.id.name_textview);
        ratingStars = itemView.findViewById(R.id.rating_stars);
        ratingCountTextView = itemView.findViewById(R.id.rating_count);
        outOfStockTextView = itemView.findViewById(R.id.outofstock_textview);
    }
}
