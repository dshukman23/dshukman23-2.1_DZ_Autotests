package ru.netology

enum class CardType {
    MIR,
    MASTERCARD,
    VISA,
    MAESTRO,
    VK_PAY
}

class TransferService {
    // лимиты
    val dailyLimit = 150_000    //  Текущий ежедневный лимит
    val monthlyLimit = 600_000  // Текущий ежемесячный лимит
    val maxOneLimitVkPay = 15_000    //  Текущий ежедневный лимит VK_Pay
    val monthlyLimitVkPay = 40_000  // Текущий ежемесячный лимит VK_Pay

    // Расчет комиссии
    fun calculateCommission(
        cardType: CardType = CardType.MIR,
        monthlyAmount: Int = 0,
        transferAmount: Int
    ): Int {
        // Проверка на превышение лимитов
        when (cardType) {
            CardType.MIR, CardType.MASTERCARD, CardType.VISA, CardType.MAESTRO -> {
                if (transferAmount > dailyLimit) {
                    throw IllegalArgumentException("Превышен дневной лимит в 150 000 руб.")
                }
                if (monthlyLimit < transferAmount + monthlyAmount) {
                    throw IllegalArgumentException("Превышен ежемесячный лимит в 600 000 руб.")
                }
            }
            CardType.VK_PAY -> {
                if (transferAmount > maxOneLimitVkPay) {
                    throw IllegalArgumentException("Превышен разовый лимит в 15 000 руб по карте VK Pay.")
                }
                if (monthlyLimitVkPay < transferAmount + monthlyAmount) {
                    throw IllegalArgumentException("Превышен ежемесячный лимит в 40 000 руб по карте VK Pay.")
                }
            }
        }
        return when (cardType) {
            CardType.MIR, CardType.VISA -> calculateVisaEndMirCommission(transferAmount)
            CardType.MASTERCARD, CardType.MAESTRO -> calculateMastercardEndMaestroCommission(transferAmount)
            CardType.VK_PAY -> 0
        }
    }
}

fun calculateVisaEndMirCommission(amount: Int): Int {
    val commission = (amount * 0.0075).toInt()
    return maxOf(commission, 35)
}

fun calculateMastercardEndMaestroCommission( transferAmount: Int): Int {
    return (if (transferAmount >= 300.0 && transferAmount <= 75000.0) {
        0.0  // Без комиссии в рамках акции
    } else {
        transferAmount * 0.006 + 20
    }).toInt()
}


fun main() {
    val service = TransferService()

    // Пример 1: Перевод 150000 с Mastercard впервые за месяц
    println(service.calculateCommission(CardType.MASTERCARD, 0, 100_000)) // 470

    // Пример 2: Перевод 100000 с Visa
    println(service.calculateCommission(CardType.VISA, 0, 100_000)) // 750

    // Пример 3: Перевод 50000 с Мир
    println(service.calculateCommission(CardType.MIR, 0, 50_000)) // 0

    // Пример 4: Превышение дневного лимита
    try {
        service.calculateCommission(CardType.VISA, 0, 200_000)
    } catch (e: IllegalArgumentException) {
        println(e.message) // Превышен дневной лимит в 150 000 руб.
    }
    // Пример 5: Перевод 10000 с VK Pay разовый
    println(service.calculateCommission(CardType.VK_PAY, 0, 10_000)) // 0

    // Пример 6: Превышение лимита VK_PAY
    try {
        service.calculateCommission(CardType.VK_PAY, 0, 20_000)
    } catch (e: IllegalArgumentException) {
        println(e.message) // Превышен разовый лимит в 15 000 руб.
    }

}