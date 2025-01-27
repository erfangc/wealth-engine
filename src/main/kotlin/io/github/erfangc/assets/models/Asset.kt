package io.github.erfangc.assets.models

import java.time.Instant

data class Asset(
        val id: String,
        val assetClass: String? = null,
        val yield: Double? = null,
        val type: String? = null,
        val category: String? = null,
        val name: String? = null,
        val description: String? = null,
        val ticker: String? = null,
        val cusip: String? = null,
        val sedol: String? = null,
        val isin: String? = null,
        val price: Double? = null,
        val allocations: Allocations? = null,
        val lastUpdated: String = Instant.now().toString(),
        val source: String = "yfinance"
)
