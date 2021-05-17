package ru.thewhite.testingtour

import java.time.LocalDate

/**
 * Калькулятор заработной платы
 *
 * @author Maxim Seredkin
 */
class SalaryCalculator {
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
        val minMonthPay = 15000
        val maxMonthPay = 45000
        val workDayCost = 1000
        val overtimeDayCost = 1500
        val quarterBonusMultiplier = 2

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

        return currentMonthNumber == 3 ||
               currentMonthNumber == 6 ||
               currentMonthNumber == 9 ||
               currentMonthNumber == 12;
    }
}