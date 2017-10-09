package cs.chua.com.walmartproductlist.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.controller.product.ProductListAdapter;
import cs.chua.com.walmartproductlist.controller.product.ProductListFragment;
import cs.chua.com.walmartproductlist.controller.product.ProductSlideScreenActivity;
import cs.chua.com.walmartproductlist.model.remote.Product;

public class MainActivity extends AppCompatActivity
        implements ProductListAdapter.OnProductItemListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProductListFragment productListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProductList();
    }

    @Override
    public void onProductListAdapterClick(final int position) {
        // used in ProductListAdapter when items are clicked.  show the full detail screen
        if (productListFragment != null) {
            final Intent intent = new Intent(this, ProductSlideScreenActivity.class);
            final ArrayList<Product> productList = new ArrayList<>(productListFragment.getProductList());
            intent.putParcelableArrayListExtra(ProductSlideScreenActivity.ARGS_PRODUCTS, productList);
            intent.putExtra(ProductSlideScreenActivity.ARGS_DEFAULT_INDEX, position);
            startActivity(intent);
        }
    }

    private void showProductList() {
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentByTag(ProductListFragment.TAG);
        if (fragment == null) {
            Log.d(TAG, "create and show ProductListFragment");
            productListFragment = new ProductListFragment();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.product_list_framelayout, productListFragment, ProductListFragment.TAG);
            ft.commit();
        } else {
            // we don't refresh if fragment was previously already loaded
            // this is to deal with orientation changes or when the activity is recreated
            productListFragment = (ProductListFragment) fragment;
            if (productListFragment.isVisible()) {
                // TODO we can add logic to refresh products if data is old
                Log.d(TAG, "fragment is already showing");
            }
        }
    }
}