package dk.lean_development.parking.parking_lot

import android.view.View
import org.jetbrains.anko.*

class ParkingLotListView(val parkingLotAdapter: ParkingLotListAdapter) : AnkoComponent<ParkingLotListActivity> {
    override fun createView(ui: AnkoContext<ParkingLotListActivity>): View = with(ui) {
        verticalLayout {
            lparams(width  = matchParent, height = matchParent)
            listView {
                adapter = parkingLotAdapter
            }
        }
    }
}