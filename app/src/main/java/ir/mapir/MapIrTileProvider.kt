package ir.mapir

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MapIrTileProvider(
    aName: String?,
    aZoomMinLevel: Int,
    aZoomMaxLevel: Int,
    aTileSizePixels: Int,
    aImageFilenameEnding: String?,
    aBaseUrl: Array<out String>? = null,
    var copyright:String
) : OnlineTileSourceBase(
    aName,
    aZoomMinLevel,
    aZoomMaxLevel,
    aTileSizePixels,
    aImageFilenameEnding,
    aBaseUrl
) {
    private val MINX = 0
    private val MAXX = 1
    private val MINY = 2
    private val MAXY = 3
    private val TILE_ORIGIN = doubleArrayOf(-20037508.34789244, 20037508.34789244)
    var OSGEO_WMS = "https://map.ir/shiveh?" +
            "service=WMS" +
            "&version=1.1.0" +
            "&EXCEPTIONS=application/vnd.ogc.se_inimage" +
            "&request=GetMap" +
            "&layers=Shiveh:Shiveh" +
            "&width=256" +
            "&height=256" +
            "&srs=EPSG:3857" +
            "&format=image/png" +
            "&bbox=%f,%f,%f,%f" +
            "&x-api-key="

    private val apiKey =
        "your token"

    override fun getTileURLString(pMapTileIndex: Long): String {
        val x = MapTileIndex.getX(pMapTileIndex)
        val y = MapTileIndex.getY(pMapTileIndex)
        val z = MapTileIndex.getZoom(pMapTileIndex)


        val bbox: Array<Double?> = getBoundingBox(x, y, z)
        val s = String.format(
            Locale.US, OSGEO_WMS,
            bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY]
        ) + apiKey
        val url: URL
        url = try {
            URL(s)
        } catch (e: MalformedURLException) {
            throw AssertionError(e)
        }
        return url.toString()
    }

    private fun getBoundingBox(x: Int, y: Int, zoom: Int): Array<Double?> {
        val tileSize = 20037508.34789244 * 2 / Math.pow(
            2.0,
            zoom.toDouble()
        )
        val bbox = arrayOfNulls<Double>(4)
        bbox[MINX] = TILE_ORIGIN.get(0) + x * tileSize
        bbox[MINY] = TILE_ORIGIN.get(1) - (y + 1) * tileSize
        bbox[MAXX] = TILE_ORIGIN.get(0) + (x + 1) * tileSize
        bbox[MAXY] = TILE_ORIGIN.get(1) - y * tileSize
        return bbox
    }

    override fun getCopyrightNotice(): String? {
        return copyright
    }
}