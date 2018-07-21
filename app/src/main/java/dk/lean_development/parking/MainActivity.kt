package dk.lean_development.parking

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import org.jetbrains.anko.*
import org.json.JSONObject

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ParkingLotListAdapter(listOf())
        MainView(adapter).setContentView(this)

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

class ParkingLotService() {
    fun getParkingLots() : List<ParkingLot> {
        val (request, response, result) = "http://portal.opendata.dk/api/3/action/datastore_search?resource_id=2a82a145-0195-4081-a13c-b0e587e9b89c"
                .httpGet()
                .responseJson()

        val json = result.component1()?.obj() ?: return listOf()
        return parseParkingLots(json)
    }

    private fun parseParkingLots(json: JSONObject): List<ParkingLot> {
        val records = json.getJSONObject("result").getJSONArray("records")

        val parkingLots = mutableListOf<ParkingLot>()
        for (index in 0..records.length() - 1) {
            val record = records.getJSONObject(index)
            parkingLots.add(ParkingLot(
                    record.getInt("_id"),
                    record.getString("garageCode"),
                    record.getInt("totalSpaces"),
                    record.getInt("vehicleCount")))
        }

        return parkingLots
    }
}

class MainView(val parkingLotAdapter: ParkingLotListAdapter) : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        verticalLayout {
            lparams(width  = matchParent, height = matchParent)
            listView {
                adapter = parkingLotAdapter
            }
        }
    }
}

class ParkingLotListAdapter(var parkingLots: List<ParkingLot>) : BaseAdapter() {
    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val context = viewGroup?.context ?: return View(null)
        val parkingLot = parkingLots[index];
        val color = color(parkingLot)

        return with(context) {
            verticalLayout {
                backgroundColorResource = color
                textView(parkingLot.name) {
                    textSize = 20f
                    topPadding = 20
                    rightPadding = 50
                    leftPadding = 50
                    bottomPadding = 5
                }.lparams(width  = matchParent, height = wrapContent)
                textView("${parkingLot.totalSpace - parkingLot.usedSpace} ledige pladser") {
                    typeface = Typeface.DEFAULT_BOLD
                    leftPadding = 50
                    leftPadding = 50
                    bottomPadding = 20
                }.lparams(width  = matchParent, height = wrapContent)
            }
        }
    }

    private fun color(parkingLot: ParkingLot): Int {
        val usedSpacePercentage = parkingLot.usedSpace.toDouble() / parkingLot.totalSpace.toDouble() * 100
        return when(usedSpacePercentage) {
            in 0..40 -> R.color.green
            in 41..80 -> R.color.yellow
            in 81..100 -> R.color.red
            else -> R.color.white
        }
    }

    override fun getItem(index: Int): ParkingLot {
        return parkingLots[index]
    }

    override fun getItemId(index: Int): Long {
        return parkingLots[index].id.toLong()
    }

    override fun getCount(): Int {
        return parkingLots.count()
    }
}

class ParkingLot(val id: Int, val name: String, val totalSpace: Int, val usedSpace: Int)