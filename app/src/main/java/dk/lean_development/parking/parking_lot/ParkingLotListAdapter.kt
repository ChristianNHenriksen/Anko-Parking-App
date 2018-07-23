package dk.lean_development.parking.parking_lot

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import dk.lean_development.parking.R
import org.jetbrains.anko.*

class ParkingLotListAdapter(var parkingLots: List<ParkingLot>) : BaseAdapter() {
    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val context = viewGroup?.context ?: return View(null)
        val parkingLot = parkingLots[index];
        val icon = icon(parkingLot)

        return with(context) {
            relativeLayout {
                val imageView = imageView(icon) {
                    id = R.id.parking_lost_list_image_view
                }.lparams {
                    width = 100
                    leftMargin = 24
                    sameTop(R.id.parking_lost_list_name_text)
                    sameBottom(R.id.parking_lost_list_description_text)
                }

                val name = textView(parkingLot.name) {
                    id = R.id.parking_lost_list_name_text
                    textSize = 20f
                    maxLines = 1
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    topMargin = 24
                    leftMargin = 24
                    alignParentTop()
                    rightOf(imageView)
                    endOf(imageView)
                }

                textView("${parkingLot.totalSpace - parkingLot.usedSpace} ledige pladser") {
                    id = R.id.parking_lost_list_description_text
                    textSize = 14f
                    maxLines = 1
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    bottomMargin = 24
                    below(name)
                    alignStart(R.id.parking_lost_list_name_text)
                }
            }
        }
    }

    private fun icon(parkingLot: ParkingLot): Int {
        val usedSpacePercentage = parkingLot.usedSpace.toDouble() / parkingLot.totalSpace.toDouble() * 100
        return when(usedSpacePercentage) {
            in 0..40 -> R.drawable.green_circle
            in 41..80 -> R.drawable.yellow_circle
            in 81..100 -> R.drawable.red_circle
            else -> R.color.white
        }
    }

    override fun getItem(index: Int): ParkingLot {
        return parkingLots[index]
    }

    override fun getItemId(index: Int): Long {
        return when(parkingLots[index].name) {
            "Nørreport" -> 1
            "Scandinavian Center" -> 2
            "Bruun's Galleri" -> 3
            "Magasin" -> 4
            "Kalkværksvej" -> 5
            "Salling" -> 6
            "Navitas" -> 8
            "Busgadehuset" -> 9
            "Dokk1" -> 10
            else -> 0
        }

    }

    override fun getCount(): Int {
        return parkingLots.count()
    }
}