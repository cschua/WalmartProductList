package cs.chua.com.walmartproductlist.controller.product;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.model.remote.Product;

/**
 * Created by christopherchua on 10/6/17.
 */

public class ProductSlideScreenAdapter extends FragmentStatePagerAdapter {
    private List<Product> productList;

    public ProductSlideScreenAdapter(final FragmentManager fm) {
        super(fm);
        productList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return ProductSlideDetailedFragment.newInstance(productList.get(position));
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    public void setProductList(final List<Product> productList) {
        this.productList = productList;
    }
}
