package io.github.erfangc.portfolios

import io.github.erfangc.portfolios.models.ImportPortfolioRequest
import io.github.erfangc.portfolios.models.ImportPortfolioResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/apis/portfolios/_import")
class PortfolioImportController(private val portfolioImportService: PortfolioImportService) {
    @PostMapping
    fun resolvePortfolio(@RequestBody req: ImportPortfolioRequest): ImportPortfolioResponse {
        return portfolioImportService.importPortfolio(req)
    }
}