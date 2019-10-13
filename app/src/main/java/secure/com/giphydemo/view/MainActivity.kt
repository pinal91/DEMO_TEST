package secure.com.giphydemo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.leza.lawyers.apimanager.WebServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import secure.com.giphydemo.R
import secure.com.giphydemo.adapter.DemoListAdapter
import secure.com.giphydemo.adapter.DemoListOfflineAdapter
import secure.com.giphydemo.helper.Global
import secure.com.giphydemo.helper.NetworkUtil
import secure.com.giphydemo.model.DataItem
import secure.com.giphydemo.model.ResponseModel
import secure.com.giphydemo.offlinestorage.Constants
import secure.com.giphydemo.offlinestorage.DBHelper
import secure.com.giphydemo.offlinestorage.OfflineDataModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private var featureListAdapter: DemoListAdapter? = null
    private var offlineListAdapter: DemoListOfflineAdapter? = null

    private var isFromRefresh: Boolean? = false
    private lateinit var productsDBHelper: DBHelper
    private var arrListCartItemOffline = ArrayList<OfflineDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()

        if (NetworkUtil.getConnectivityStatus(this@MainActivity) != 0) {
            dataList()
        }else{
            offlineLoad()
        }
    }

    ///////////////////offline load list data///////////////////////////////////////////////////////
    private fun offlineLoad() {
        txtHead.text = Global.getStringFromSharedPref(this, Constants.PREFS_HEADER)
        arrListCartItemOffline = productsDBHelper.getAllCartProducts()
        if (arrListCartItemOffline != null && !arrListCartItemOffline.isEmpty()) {
            offlineListAdapter = DemoListOfflineAdapter(arrListCartItemOffline!!, this)
            recyFeaturedlist.adapter = offlineListAdapter

        }
    }

    ///////initialization///////////////////////////////////////////////////////////////////////////
    private fun initialize() {
        productsDBHelper = DBHelper(this)
        recyFeaturedlist.setLayoutManager(androidx.recyclerview.widget.LinearLayoutManager(this)
                as androidx.recyclerview.widget.RecyclerView.LayoutManager?)

        swipe_refresh_layout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            isFromRefresh = true
            swipe_refresh_layout.isRefreshing = true
            swipe_refresh_layout.postDelayed(Runnable {
                swipe_refresh_layout.isRefreshing = false

                if (NetworkUtil.getConnectivityStatus(this@MainActivity) != 0) {
                    dataList()
                }else{
                    offlineLoad()
                }
            }, 1000)
        })
    }

    ///////api calling//////////////////////////////////////////////////////////////////////////////
    private fun dataList() {

        if (!isFromRefresh!!)
        Global.showProgressDialog(this)
        //   Log.d("HOMNE", Global.apiService.home(WebServices.HomeWs))
        disposable =
            Global.apiService.videoList(WebServices.VideoWs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
    }

    //Here We can handle reponse////////////////////////////////////////////////////////////////////

    private fun handleResponse(homeList: ResponseModel) {
        txtHead.text = homeList?.title
        Global.saveStringInSharedPref(this!!, Constants.PREFS_HEADER, homeList?.title!!)

        if (homeList.rows?.size!!>0) {
            productsDBHelper.deleteTable("demo_list")

            for (x in 0 until homeList.rows.size){
                productsDBHelper.addProductToCart(OfflineDataModel(homeList.rows[x]?.title,
                    homeList.rows[x]?.description,
                    homeList.rows[x]?.imageHref)
                )
            }
            featureListAdapter = DemoListAdapter(homeList?.rows as ArrayList<DataItem?>, this)
            recyFeaturedlist.adapter = featureListAdapter
        }
        if (!isFromRefresh!!)
            Global.dismissProgressDialog()

    }

    //Here Error We will get////////////////////////////////////////////////////////////////////////

    fun handleError(error: Throwable) {
        if (!isFromRefresh!!)
        Global.dismissProgressDialog()
    }
}
