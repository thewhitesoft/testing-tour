package ru.thewhite.testingtour

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Maxim Seredkin
 */
internal class SalaryCalculatorTest {
    val salaryCalculator = SalaryCalculator()

    // region positives

    /**
     * Расчет заработной платы за текущий месяц без премии
     *
     * [lastMonthOfQuarter:false]
     * [workDays:20] * [workDayCost:1000] + [overtimeDays:2] * [overtimeDayCost:1500] = 23000
     */
    @Test
    fun currentMonthPayWithoutBonus() {
        // Arrange
        val workDays = 20
        val overtimeDays = 2

        // Act
        val result = salaryCalculator.currentMonthPay(workDays, overtimeDays)

        // Assert
        Assertions.assertEquals(23000, result)
    }

    // endregion positives
}