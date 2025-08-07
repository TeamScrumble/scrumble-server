package com.project.scrumbleserver.global.transaction

import com.project.scrumbleserver.global.exception.ServerException
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Component
class Transaction(
    private val transactionManager: PlatformTransactionManager,
) {
    operator fun <T> invoke(action: () -> T): T {
        val transactionTemplate =
            TransactionTemplate(transactionManager).apply {
                isReadOnly = false
            }

        return transactionTemplate.execute {
            action()
        } ?: throw ServerException("transaction execute failed")
    }

    fun <T> readOnly(readOnlyAction: () -> T): T {
        val transactionTemplate =
            TransactionTemplate(transactionManager).apply {
                isReadOnly = true
            }

        return transactionTemplate.execute {
            readOnlyAction()
        } ?: throw ServerException("transaction execute failed")
    }
}
