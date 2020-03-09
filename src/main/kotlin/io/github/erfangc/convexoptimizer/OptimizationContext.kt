package io.github.erfangc.convexoptimizer

import ilog.concert.IloNumVar
import ilog.cplex.IloCplex
import io.github.erfangc.assets.Asset
import io.github.erfangc.marketvalueanalysis.MarketValueAnalysis

data class OptimizationContext(
        val cplex: IloCplex,
        val request: OptimizePortfolioRequest,
        val assets: Map<String, Asset>,
        val assetIds: List<String>,
        val assetVars: Map<String, IloNumVar>,
        val portfolioDefinitions: List<PortfolioDefinition>,
        val positionVars: List<PositionVar>,
        val expectedReturns: Map<String, Double>,
        val marketValueAnalyses: Map<String, MarketValueAnalysis>,
        val aggregateNav: Double
)