package cs.chua.com.walmartproductlist.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.controller.product.ProductBaseFragment;
import cs.chua.com.walmartproductlist.controller.product.ProductListFragment;
import cs.chua.com.walmartproductlist.controller.product.adapter.ProductListAdapter;
import cs.chua.com.walmartproductlist.model.local.PaginationCount;
import cs.chua.com.walmartproductlist.model.remote.Product;

public class MainActivity extends AppCompatActivity
        implements ProductListAdapter.OnProductItemListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProductListFragment productListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProductListScreen();
    }

    @Override
    public void onProductListAdapterClick(int position) {
        // used in ProductListAdapter when items are clicked.  show the full detail screen
        Log.d(TAG, "onProductListAdapterClick " + position);
        if (productListFragment != null) {
            final Intent intent = new Intent(this, ProductSlideScreenActivity.class);

            // we will limit pageviews to 30 items starting from the middle of the list if possible
            List<Product> productList = productListFragment.getProductList();
            final int leftIndex = Math.max(position - PaginationCount.TOTAL_ITEMS_PER_PAGE/2, 0);
            final int rightIndex = Math.min(leftIndex + PaginationCount.TOTAL_ITEMS_PER_PAGE, productList.size()-1);
            productList = productList.subList(leftIndex, rightIndex);
            position = Math.min(position, 15);

            intent.putParcelableArrayListExtra(ProductSlideScreenActivity.INTENT_EXTRA_PRODUCTS, new ArrayList<>(productList));
            intent.putExtra(ProductSlideScreenActivity.INTENT_EXTRA_DEFAULT_POSITION, position);
            startActivity(intent);
        }
    }

    private void showProductListScreen() {
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentByTag(ProductBaseFragment.TAG);
        if (fragment == null) {
            Log.d(TAG, "create and show ProductBaseFragment");
            productListFragment = new ProductListFragment();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.product_list_framelayout, productListFragment, ProductBaseFragment.TAG);
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