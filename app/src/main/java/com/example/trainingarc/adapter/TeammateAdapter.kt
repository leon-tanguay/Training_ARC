import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingarc.R
import com.example.trainingarc.model.Teammate

class TeammateAdapter(private val teammates: List<Teammate>) :
    RecyclerView.Adapter<TeammateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val pointsText: TextView = view.findViewById(R.id.pointsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teammate, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = teammates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teammate = teammates[position]
        holder.nameText.text = teammate.name
        holder.pointsText.text = teammate.points.toString()
    }
}
