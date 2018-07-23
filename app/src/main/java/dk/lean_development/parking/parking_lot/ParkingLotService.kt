package dk.lean_development.parking.parking_lot

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import org.json.JSONObject

class ParkingLotService() {
    val parkingLotNames = mapOf(
            listOf(1) to "Nørreport",
            listOf(2) to "Scandinavian Center",
            listOf(3) to "Bruun's Galleri",
            listOf(4) to "Magasin",
            listOf(5) to "Kalkværksvej",
            listOf(6) to "Salling",
            listOf(8) to "Navitas",
            listOf(9) to "Busgadehuset",
            listOf(10, 11) to "Dokk1"
    )

    fun getParkingLots() : List<ParkingLot> {
        val (request, response, result) = "http://portal.opendata.dk/api/3/action/datastore_search?resource_id=2a82a145-0195-4081-a13c-b0e587e9b89c"
                .httpGet()
                .responseJson()

        val json = result.component1()?.obj() ?: return listOf()
        return parseParkingLots(json)
    }

    private fun parseParkingLots(json: JSONObject): List<ParkingLot> {
        val records = json.getJSONObject("result").getJSONArray("records")

        val parkingLots = parkingLotNames.entries.map { (indexes, name) ->
            val ids = mutableListOf<Int>()
            var totalSpace = 0
            var usedSpace = 0

            for (index in 0..records.length() - 1) {
                val record = records.getJSONObject(index)
                if (!indexes.contains(record.getInt("_id")))
                    continue

                ids.add(index)
                totalSpace += record.getInt("totalSpaces")
                usedSpace += record.getInt("vehicleCount")
            }

            return@map ParkingLot(name, totalSpace, usedSpace)
        }

        return parkingLots
    }
}