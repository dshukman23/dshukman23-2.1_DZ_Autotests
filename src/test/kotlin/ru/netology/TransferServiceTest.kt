package ru.netology

import org.junit.Assert.*

class TransferServiceTest {
    @org.junit.Test
    fun calculateCommission() {
        val service = TransferService() // создаем экземпляр
        val cardType = CardType.VISA
        val monthlyAmount = 0
        val transferAmount = 100_000

        val result = service.calculateCommission(cardType, monthlyAmount, transferAmount)

        assertEquals(750, result)
    }

}