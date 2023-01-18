import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MarginItemDecoration(private val bottomDPSpace: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        val displayMetrics = DisplayMetrics()
//        val px =
//            bottomDPSpace * (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        outRect.bottom = bottomDPSpace
    }
}