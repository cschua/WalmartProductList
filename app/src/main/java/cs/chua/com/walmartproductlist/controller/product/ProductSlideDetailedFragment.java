package cs.chua.com.walmartproductlist.controller.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.controller.ViewHolderPopulator;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.view.product.ProductDetailViewHolder;

/**
 * Created by christopherchua on 10/5/17.
 */

public class ProductSlideDetailedFragment extends Fragment {
    public static final String ARGS_PRODUCT = "argproduct";
    private ProductDetailViewHolder viewHolder;

    public ProductSlideDetailedFragment() {}

    public static ProductSlideDetailedFragment newInstance(final Product product) {
        final ProductSlideDetailedFragment fragment = new ProductSlideDetailedFragment();
        final Bundle args = new Bundle();
        args.putParcelable(ARGS_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.product_detailed_layout, container, false);
        viewHolder = new ProductDetailViewHolder(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            final Product product = bundle.getParcelable(ARGS_PRODUCT);
            ViewHolderPopulator.populateProductDetail(product, viewHolder.getProductViews());
        }
    }
}
