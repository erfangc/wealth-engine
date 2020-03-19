package io.github.erfangc.assets.parser.universeproviders

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.erfangc.assets.parser.yfinance.YFinanceFundAssetParser
import io.github.erfangc.assets.parser.yfinance.YFinanceTimeSeriesDownloader
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ISharesTickerProvider(
        private val httpClient: HttpClient,
        private val objectMapper: ObjectMapper,
        private val yFinanceFundAssetParser: YFinanceFundAssetParser,
        private val yFinanceTimeSeriesDownloader: YFinanceTimeSeriesDownloader
) {
    private val log = LoggerFactory.getLogger(ISharesTickerProvider::class.java)

    fun run() {
        // this queries the iShares' websites screener
        val httpGet = HttpGet("https://www.ishares.com/us/product-screener/product-screener-v3.jsn?dcrPath=/templatedata/config/product-screener-v3/data/en/us-ishares/product-screener-ketto&siteEntryPassthrough=true")
        val content = httpClient
                .execute(httpGet)
                .entity
                .content
        val jsonNode = objectMapper.readTree(content)
        val columns = jsonNode.at("/data/tableData/columns")
        val data = jsonNode.at("/data/tableData/data")
        val tickerIdx = columns.indexOfFirst { it.at("/name").textValue() == "localExchangeTicker" }
        // every row in data represents an ETF
        data.forEachIndexed {
            idx, row ->
            try {
                val ticker = row.get(tickerIdx).textValue()
                log.info("Processing ticker $ticker")
                yFinanceFundAssetParser.parseTicker(ticker, true)
                yFinanceTimeSeriesDownloader.downloadHistoryForTicker(ticker, true)
                log.info("Finished processing ticker $ticker")
            } catch (e: Exception) {
                log.error("Unable to process row $idx", e)
            }
        }
    }

}