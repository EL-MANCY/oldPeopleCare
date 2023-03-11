package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import java.io.InputStream
import java.net.URL


class MedicineRecyclerView: RecyclerView.Adapter<MedicineRecyclerView.MedicineViewHolder>() {

    var  medicineList: List<AllMedicineRespone> = emptyList()
    fun setList(medicineItems: List<AllMedicineRespone>) {
        this.medicineList = medicineItems
        notifyDataSetChanged()
    }

    var onListItemClick:OnItemClickListener?=null



    inner class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var medicineImage: ImageView = itemView.findViewById(R.id.medicineImage)
        var time_txtView: TextView = itemView.findViewById(R.id.time_txtView)
        var medicineNameTxtView: TextView = itemView.findViewById(R.id.fullname_txtfield)
        var edit_icon: ImageView = itemView.findViewById(R.id.edit_icon)
        var mark_icon: ImageView = itemView.findViewById(R.id.mark_icon)
        var medicineDescriptionTxtView: TextView =
            itemView.findViewById(R.id.medicineDescriptionTxtView)
        var medicineProgressBar: ProgressBar = itemView.findViewById(R.id.medicineProgressBar)




        fun bind(medicineInfo: AllMedicineRespone) {
//            val imgUri: Uri = Uri.parse(medicineInfo.imgUrl)
//            medicineImage.setImageURI(imgUri)
             try {
                val i: InputStream = URL(medicineInfo.imgUrl).getContent() as InputStream
                val d=Drawable.createFromStream(i, "src name")
                 medicineImage.setImageDrawable(d)
            } catch (e: Exception) {

            }
            time_txtView.text = medicineInfo.time
            medicineNameTxtView.text = medicineInfo.name
            edit_icon.setImageResource(R.drawable.edit_icon)
            mark_icon.setImageResource(R.drawable.correct_mark)
            medicineDescriptionTxtView.text = medicineInfo.description
            medicineProgressBar.progress = 100

            edit_icon.setOnClickListener {
                onListItemClick?.onItemClick(medicineInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.medicine_list_item, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        var item: AllMedicineRespone = medicineList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return medicineList.size

    }
}