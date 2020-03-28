package io.github.erfangc.dataaccess

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class DatabaseInitializer(private val jdbcTemplate: JdbcTemplate) {

    private val log = LoggerFactory.getLogger(DatabaseInitializer::class.java)

    init {
        createTable("clients")
        createUsersTable()
        createPortfoliosTable()
        createProposalsTable()
    }

    private fun createPortfoliosTable() {
        log.info("Attempting to create the portfolios table if it does not exist")
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS portfolios (
                id varchar not null,
                clientId varchar,
                userId varchar not null,
                json json,
                PRIMARY KEY (id, userId)
            )
        """.trimIndent())
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idxClientId ON portfolios (clientId)
        """.trimIndent())
    }

    private fun createProposalsTable() {
        log.info("Attempting to create the proposals table if it does not exist")
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS proposals (
                id varchar not null,
                clientId varchar,
                userId varchar not null,
                json json,
                PRIMARY KEY (id, userId)
            )
        """.trimIndent())
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idxClientId ON proposals (clientId)
        """.trimIndent())
    }

    private fun createUsersTable() {
        log.info("Attempting to create the users table if it does not exist")
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id varchar not null,
                password varchar not null,
                json json,
                PRIMARY KEY (id)
            )
        """.trimIndent())
    }
    private fun createTable(name: String) {
        log.info("Attempting to create the $name table if it does not exist")
        //language=PostgreSQL
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS $name (
                id varchar not null,
                userId varchar not null,
                json json,
                PRIMARY KEY (id, userId)
            )
        """.trimIndent())
    }

}