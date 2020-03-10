package io.github.erfangc.assets

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class AssetService {

    private val assetLookup = jacksonObjectMapper()
            .readValue<Map<String, Asset>>(ClassPathResource("assets/assets.json").inputStream)
    private val cusipLookup = assetLookup.values.filter { it.cusip != null }.associateBy { it.cusip!! }
    private val tickerLookup = assetLookup.values.filter { it.ticker != null }.associateBy { it.ticker!! }

    fun getAssets(assetIds: List<String>): List<Asset> {
        return assetIds.map {
            assetLookup[it] ?: throw RuntimeException("cannot find assetId $it")
        }
    }

    fun getAssetByCUSIP(cusip: String): Asset? {
        return cusipLookup[cusip]
    }

    fun getAssetByTicker(ticker: String): Asset? {
        return tickerLookup[ticker]
    }

    fun getAssetsByPublicIdentifiers(publicIdentifiers: List<PublicIdentifier>): List<Asset> {
        TODO()
    }

}
