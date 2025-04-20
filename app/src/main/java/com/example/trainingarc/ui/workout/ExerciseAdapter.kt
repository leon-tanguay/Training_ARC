import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.res.ResourcesCompat
import com.example.trainingarc.R


class ExerciseAdapter(
    private var exercises: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.label.text = exercises[position]

        val customFont = ResourcesCompat.getFont(holder.itemView.context, R.font.chakrapetchregular)
        holder.label.typeface = customFont

        holder.itemView.setOnClickListener {
            onClick(exercises[position])
        }
    }

    override fun getItemCount(): Int = exercises.size

    fun filterList(filtered: List<String>) {
        exercises = filtered
        notifyDataSetChanged()
    }
}
