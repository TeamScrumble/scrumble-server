package com.project.scrumbleserver.global.transaction

import com.project.scrumbleserver.global.excception.ServerException
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
class Transaction(
    private val transactionTemplate: TransactionTemplate,
) {
    operator fun <T> invoke(action: () -> T): T {
        transactionTemplate.isReadOnly = false
        return transactionTemplate.execute {
            action()
        } ?: throw ServerException("transaction execute failed")
    }

    fun <T> readOnly(readOnlyAction: () -> T): T {
        transactionTemplate.isReadOnly = true
        return transactionTemplate.execute {
            readOnlyAction()
        } ?: throw ServerException("transaction execute failed")
    }
}