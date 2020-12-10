package drm.ezenglish.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import drm.ezenglish.R

class TopicAdapter(context: Context, resourceId: Int, private val topics: List<String?>)
    : ArrayAdapter<String>(context, resourceId, topics) {

    override fun getCount() = topics.size

    override fun getItem(position: Int) = topics[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
        label.text = topics[position]
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = topics[position]
        return label
    }
}