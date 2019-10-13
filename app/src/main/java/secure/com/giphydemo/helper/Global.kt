package secure.com.giphydemo.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import secure.com.giphydemo.R
import secure.com.giphydemo.apimanager.RestClient

/**
 * Created by pinal-leza on 1/17/2018.
 */
object Global {
    private lateinit var progressDialog: Dialog


    /////////////////below function is required to initialize retrofit interface////////////////////
    val apiService by lazy {
        RestClient.create()
    }


    ///////////////////to chek null value///////////////////////////////////////////////////////////
    fun checkNull(strValue: String?): String? {
        return strValue ?: ""
    }

    /////////////////show progress dialog globally from any where in page///////////////////////////
    fun showProgressDialog(activity: Activity) {

        val v = activity.layoutInflater.inflate(R.layout.dialog_loading, null)
        progressDialog = Dialog(activity, R.style.MyTheme)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(v)
        //progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (progressDialog != null && !progressDialog.isShowing)
            progressDialog.show()
        progressDialog.setCancelable(true)
        progressDialog.setCanceledOnTouchOutside(true)
    }

    //Custom Toast for All
    fun showCustomToast(context: Context, strMsg: String) =
        Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun getStatusBarHeight(activity: Activity): Int {
        var result = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getStringFromSharedPref(context: Context, key: String): String {
        return SharedPreferencesManager.getString(context, key, "")!!
    }

    fun saveStringInSharedPref(context: Context, key: String, strInput: String) {
        SharedPreferencesManager.writeString(context, key, strInput)
    }

    ///////////////end progreess dialog/////////////////////////////////////////////////////////////
    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    //Common Method to load image using picasso/////////////////////////////////////////////////////
    fun imageLoad(context: Context, strUrl: String, imageView: ImageView) {
        Picasso.with(context).load(strUrl).into(imageView)
    }


}
