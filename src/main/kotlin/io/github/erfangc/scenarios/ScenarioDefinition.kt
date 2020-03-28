package io.github.erfangc.scenarios

data class ScenarioDefinition(
        val id: String,
        val name: String,
        val description: String? = null,
        val scenarioShocks: List<ScenarioShock>
)

