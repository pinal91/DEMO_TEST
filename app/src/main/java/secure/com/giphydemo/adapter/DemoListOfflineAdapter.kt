package secure.com.giphydemo.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.data_list_home.view.*
import secure.com.giphydemo.R
import secure.com.giphydemo.offlinestorage.OfflineDataModel
import java.util.*


class DemoListOfflineAdapter(private val dataList: ArrayList<OfflineDataModel>, private val context: Context?)
    : androidx.recyclerview.widget.RecyclerView.Adapter<DemoListOfflineAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (dataList[position]?.image.equals("null")||dataList[position]?.image.isNullOrEmpty()){
            holder.itemView.imgLawfirm.visibility=View.GONE
        }

        if (dataList[position]?.image.equals("")||dataList[position]?.image!!.isNullOrEmpty()){

            holder.itemView.imgLawfirm.visibility=View.GONE
        }else {
            Picasso.with(context).load(dataList[position]?.image)
                .into(holder.itemView.imgLawfirm, object : Callback {
                    override fun onSuccess() {

                        holder.itemView.imgLawfirm.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        holder.itemView.imgLawfirm.visibility = View.GONE
                    }

                })
        }


        if (dataList[position]?.title.equals("null")||dataList[position]?.title.isNullOrEmpty()){
            holder.itemView.txtTitle.visibility=View.GONE
        }
        if (dataList[position]?.description.equals("null")||dataList[position]?.description.isNullOrEmpty()){
            holder.itemView.txtDesc.visibility=View.GONE
        }

        if ( holder.itemView.txtTitle.visibility==View.VISIBLE || holder.itemView.txtTitle.visibility==View.VISIBLE
            || holder.itemView.txtTitle.visibility==View.VISIBLE){
            holder.itemView.imgNext.visibility=View.VISIBLE
        }else{
            holder.itemView.imgNext.visibility=View.GONE
        }

        holder.itemView.txtTitle.text=dataList[position]?.title

        holder.itemView.txtDesc.text=dataList[position]?.description




    }


    override fun getItemCount(): Int = dataList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_list_home, parent, false)

        return ViewHolder(view, parent.context)
    }

    class ViewHolder(view: View, val activity: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {


    }
}