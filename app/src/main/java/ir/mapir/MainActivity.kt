package ir.mapir

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var controller: IMapController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mapView = findViewById(R.id.map)
        controller = mapView.controller
        setMapViewConfig()


    }
    private fun setMapViewConfig() {
        mapView.overlayManager.tilesOverlay.loadingBackgroundColor = resources.getColor(R.color.white)
        mapView.overlayManager.tilesOverlay.loadingLineColor = resources.getColor(R.color.white)
        mapView.setTileSource(
            MapIrTileProvider(
                "Map.ir",
                0,
                22,
                256,
                ".png",
                copyright = "© your copyRight")
        )
        mapView.setMultiTouchControls(true)
        mapView.maxZoomLevel = 19.toDouble()
        mapView.minZoomLevel = 12.toDouble()
        controller.setZoom(15.toDouble())
        val startPoint = GeoPoint(34.639942, 50.875942)
        controller.setCenter(startPoint)
        mapView.isTilesScaledToDpi = true
        val mCopyrightOverlay = CopyrightOverlay(this)
        mCopyrightOverlay.setTextColor(resources.getColor(R.color.black))
        mCopyrightOverlay.setOffset(20, 40)
        mapView.overlays.add(mCopyrightOverlay)
    }

}
