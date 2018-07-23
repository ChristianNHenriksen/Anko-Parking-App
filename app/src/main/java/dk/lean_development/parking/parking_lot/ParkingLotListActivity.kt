package dk.lean_development.parking.parking_lot

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.uiThread

class ParkingLotListActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ParkingLotListAdapter(listOf())
        ParkingLotListView(adapter).setContentView(this)

        val parkingLotService = ParkingLotService()

        doAsync {
            val parkingLots = parkingLotService.getParkingLots()
            adapter.parkingLots = parkingLots
            uiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
}