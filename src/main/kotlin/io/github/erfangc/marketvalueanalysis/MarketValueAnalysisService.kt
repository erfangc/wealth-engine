package io.github.erfangc.marketvalueanalysis

import io.github.erfangc.assets.models.Asset
import io.github.erfangc.assets.AssetService
import io.github.erfangc.portfolios.models.Position
import io.github.erfangc.common.PortfolioUtils.assetIds
import io.github.erfangc.marketvalueanalysis.models.*
import org.springframework.stereotype.Service

@Service
class MarketValueAnalysisService(private val assetService: AssetService) {

    private fun getMarketValue(asset: Asset?, position: Position): Double {
        return if (position.assetId == "USD") {
            position.quantity
        } else {
            (asset?.price ?: 0.0) * position.quantity
        }
    }

    /**
     * Market value analysis computes weight / market value of each position
     * as well as NAV of the portfolio and allocations
     */
    fun marketValueAnalysis(req: MarketValueAnalysisRequest): MarketValueAnalysisResponse {

        val portfolios = req.portfolios

        val assets = assetService
                .getAssets(assetIds(portfolios))
                .associateBy { it.id }

        // for every portfolio, sum up the market value of all the positions
        val netAssetValues = portfolios.map { portfolio ->
            portfolio.id to portfolio.positions.fold(0.0) { acc, position ->
                acc + getMarketValue(position = position, asset = assets[position.assetId])
            }
        }.toMap()

        val netAssetValue = netAssetValues.values.sum()

        val marketValues = portfolios.map { portfolio ->
            portfolio.id to portfolio.positions.map { position ->
                val asset = assets[position.assetId]
                val marketValue = getMarketValue(asset, position)
                position.id to marketValue
            }.toMap()
        }.toMap()

        // compute a map of positions to their weight in their respective portfolios
        val weights = portfolios.map { portfolio ->
            portfolio.id to portfolio.positions.map { position ->
                val nav = netAssetValues[portfolio.id] ?: 0.0
                position.id to nav.let {
                    (marketValues[portfolio.id]?.get(position.id) ?: 0.0) / nav
                }
            }.toMap()
        }.toMap()

        val weightsToAllInvestments = portfolios.map { portfolio ->
            val portfolioId = portfolio.id
            portfolioId to portfolio.positions.map { position ->
                val positionId = position.id
                val marketValue = marketValues[portfolioId]?.get(positionId) ?: 0.0
                positionId to marketValue / netAssetValue
            }.toMap()
        }.toMap()

        // create asset Allocation
        val buckets = portfolios
                .flatMap { portfolio ->
                    val portfolioId = portfolio.id
                    portfolio
                            .positions
                            .map { position ->
                                val positionId = position.id
                                val assetId = position.assetId
                                val asset = assets[assetId]
                                val assetClass = asset?.assetClass ?: "Other"
                                val weight = (weightsToAllInvestments[portfolioId]?.get(positionId) ?: 0.0)
                                assetClass to weight
                            }
                }
                .groupBy { it.first }
                .map { (assetClass, weights) ->
                    Bucket(name = assetClass, weight = weights.sumByDouble { it.second })
                }

        val allocations = Allocations(
                assetAllocation = Allocation(buckets = buckets)
        )

        return MarketValueAnalysisResponse(
                MarketValueAnalysis(
                        netAssetValue = netAssetValue,
                        netAssetValues = netAssetValues,
                        weights = weights,
                        weightsToAllInvestments = weightsToAllInvestments,
                        allocations = allocations,
                        marketValue = marketValues
                ),
                assets
        )
    }

}
