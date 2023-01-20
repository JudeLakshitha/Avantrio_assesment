package com.judelakshitha.avantriotest.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.models.User
import kotlin.random.Random


class UserAdapter(var context: Context, itemList: ArrayList<User>) : BaseAdapter() {

    var itemList = itemList

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
            view = inflater.inflate(R.layout.item_user, parent, false)
            vh = ViewHolder(view)
            view.tag = vh

        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        if (itemList.size > 0) {
            val itemObj = itemList[position]
            val id = itemObj.id
            val name = itemObj.name
            val letter = itemObj.letter

            if (name !== "") {
                vh.tvName.text = name
            } else {
                vh.tvName.text = "unknown"
            }

            vh.tvLetter.text = letter
            val r = java.util.Random()
            val red: Int = r.nextInt(255 - 0 + 1) + 0
            val green: Int = r.nextInt(255 - 0 + 1) + 0
            val blue: Int = r.nextInt(255 - 0 + 1) + 0

            val draw = context.resources.getDrawable(
                R.drawable.profile_circle_shape,
                context!!.theme
            ) as GradientDrawable;
            draw.setColor(Color.rgb(red, green, blue))
            vh.tvLetter.background = draw
        }

        vh.ivSubMenu.setOnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.ThemePopUpMenu)
            val popupMenu = PopupMenu(wrapper, vh.ivSubMenu)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.show()
        }

        return view!!

    }

    private class ViewHolder(view: View?) {

        val tvName: TextView
        val tvLetter: TextView

        //val profileImage: ImageView
        val ivSubMenu: ImageView

        init {
            this.tvName = view?.findViewById(R.id.tv_name) as TextView
            this.tvLetter = view?.findViewById(R.id.tv_letter) as TextView
            //this.profileImage=view.findViewById(R.id.iv_profile) as ImageView
            this.ivSubMenu = view.findViewById(R.id.iv_sub_menu) as ImageView
        }
    }
}