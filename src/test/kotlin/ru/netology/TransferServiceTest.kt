package ru.netology

import org.junit.Assert.*

class TransferServiceTest {
    @org.junit.Test
    fun calculateCommission() {
        val service = TransferService() // создаем экземпляр

        assertThrows(IllegalArgumentException::class.java) {
            service.calculateCommission(CardType.VISA, 0, 150001)
        }

        assertThrows(IllegalArgumentException::class.java) {
            service.calculateCommission(CardType.MASTERCARD, 0, 600001)
        }

        assertThrows(IllegalArgumentException::class.java) {
            service.calculateCommission(CardType.VK_PAY, 0, 15001)
        }

        assertThrows(IllegalArgumentException::class.java) {
            service.calculateCommission(CardType.VK_PAY, 0, 40001)
        }


        // Для Mastercard без комиссии
        assertEquals(0, service.calculateCommission(CardType.MASTERCARD, 0, 50000))

        // Для Visa с минимальной комиссией
        assertEquals(35, service.calculateCommission(CardType.VISA, 0, 4667))

        // Для Mastercard с комиссией
        assertEquals(620, service.calculateCommission(CardType.MASTERCARD, 0, 100000))

        // Для VK Pay всегда 0
        assertEquals(0, service.calculateCommission(CardType.VK_PAY, 0, 10000))

        val cardType = CardType.VISA
        val monthlyAmount = 0
        val transferAmount = 100_000

        val result = service.calculateCommission(cardType, monthlyAmount, transferAmount)

        assertEquals(750, result)
    }

}