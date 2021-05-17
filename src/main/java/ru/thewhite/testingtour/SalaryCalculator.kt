package ru.thewhite.testingtour

import java.time.LocalDate

/**
 * Калькулятор заработной платы
 *
 * @author Maxim Seredkin
 */
class SalaryCalculator(
        private val minMonthPay: Int,
        private val maxMonthPay: Int,
        private val workDayCost: Int,
        private val overtimeDayCost: Int,
        private val quarterBonusMultiplier: Int,
        private val bonusMonths: Array<Int>,
) {
    /**
     * Расчет заработной платы за текущий месяц
     *
     * Заработная плата = [Отработанные дни] * [Ставка] + [Переработки] * [Повышенная ставка]
     *
     * Если размер заработной платы за месяц меньше минимальной ставки за месяц, то должны заплатить по минимальной ставке.
     * Если размер заработной платы за месяц больше максимальной ставки за месяц, то должны заплатить по максимальной ставке.
     *
     * Если месяц, за который происходит оплата бонусный (с премией), то [Заработная плата] * [Бонусный коэффициент]
     *
     */
    fun currentMonthPay(workDays: Int, overtimeDays: Int): Int {

        if (workDays < 0) throw RuntimeException("Work day should be greater or equals 0");
        if (overtimeDays < 0) throw RuntimeException("Overtime day should be greater or equals 0");

        var currentMonthPay = workDays * workDayCost + overtimeDays * overtimeDayCost;

        if (currentMonthPay < minMonthPay) {
            currentMonthPay = minMonthPay;
        } else if (currentMonthPay > maxMonthPay) {
            currentMonthPay = maxMonthPay;
        }

        if (isLastMonthOfQuarter()) {
            currentMonthPay = currentMonthPay * quarterBonusMultiplier;
        }

        return currentMonthPay;
    }

    private fun isLastMonthOfQuarter(): Boolean {
        val currentMonthNumber = LocalDate.now().monthValue;

        return bonusMonths.contains(currentMonthNumber);
    }
}