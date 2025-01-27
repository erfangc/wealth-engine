package io.github.erfangc.proposals

import io.github.erfangc.proposals.models.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/apis")
class ProposalCrudController(private val proposalCrudService: ProposalCrudService) {
    @GetMapping("users/proposals")
    fun getProposalsForCurrentUser(): GetProposalsForCurrentUser {
        return proposalCrudService.getProposalsForCurrentUser()
    }

    @PostMapping("clients/{clientId}/proposals")
    fun saveProposal(@PathVariable clientId: String, @RequestBody req: SaveProposalRequest): SaveProposalResponse {
        return proposalCrudService.saveProposal(clientId, req)
    }

    @GetMapping("clients/{clientId}/proposals")
    fun getProposalsByClientId(@PathVariable clientId: String): GetProposalsByClientIdResponse {
        return proposalCrudService.getProposalsByClientId(clientId)
    }

    @GetMapping("clients/{clientId}/proposals/{proposalId}")
    fun getProposal(@PathVariable clientId: String, @PathVariable proposalId: String): GetProposalResponse {
        return proposalCrudService.getProposal(clientId, proposalId)
    }

    @DeleteMapping("clients/{clientId}/proposals/{proposalId}")
    fun deleteProposal(@PathVariable clientId: String, @PathVariable proposalId: String): DeleteProposalResponse {
        return proposalCrudService.deleteProposal(clientId, proposalId)
    }
}