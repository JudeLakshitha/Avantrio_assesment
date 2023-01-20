package com.judelakshitha.avantriotest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.models.UserLog
import java.text.SimpleDateFormat
import java.util.*

class UserLogsAdapter(var context: Context, itemList: ArrayList<UserLog>) : BaseAdapter() {


    var itemList = itemList
    var formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    var dateToStringFormatter = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
    var timeFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    var newTimeFormatter = SimpleDateFormat("HH:mm a", Locale.ENGLISH)

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val vh: ViewHolder


        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_log, parent, false)
            vh = ViewHolder(view)
            view.tag = vh


        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        if (itemList.size > 0) {
            val itemObj = itemList[position]
            val dateString = itemObj.dateString
            val timeString = itemObj.timeString
            val alertView = itemObj.alertView
            val longitude = itemObj.longitude
            val latitude = itemObj.latitude

            val date = formatter.parse(dateString)
            val dateStringOut = date?.let { dateToStringFormatter.format(it) }

            if (dateString != "") {
                vh.tvDate.text = dateStringOut
            } else {
                vh.tvDate.text = "null"
            }

            val time = timeFormatter.parse(timeString)
            val timeStringOut = time?.let { newTimeFormatter.format(it) }

            if (timeStringOut != "") {
                vh.tvTime.text = timeStringOut
            } else {
                vh.tvTime.text = "null"
            }

            if (!alertView) {
                vh.tvAlertView.text = "off"
            } else {
                vh.tvAlertView.text = "on"
            }

        }

        return view!!
    }

    private class ViewHolder(view: View?) {

        val tvDate: TextView
        val tvTime: TextView
        val tvAlertView: TextView
        val ivMap: ImageView

        init {
            this.tvDate = view?.findViewById(R.id.tv_date) as TextView
            this.tvTime = view.findViewById(R.id.tv_time) as TextView
            this.tvAlertView = view.findViewById(R.id.tv_alert_view) as TextView
            this.ivMap = view.findViewById(R.id.iv_map) as ImageView
        }
    }
}