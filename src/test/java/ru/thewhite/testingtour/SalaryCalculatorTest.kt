package ru.thewhite.testingtour

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Maxim Seredkin
 */
internal class SalaryCalculatorTest {
    val minMonthPay = 15000
    val maxMonthPay = 45000
    val workDayCost = 1000
    val overtimeDayCost = 1500
    val quarterBonusMultiplier = 2

    val salaryCalculator = SalaryCalculator(
            minMonthPay = minMonthPay,
            maxMonthPay = maxMonthPay,
            workDayCost = workDayCost,
            overtimeDayCost = overtimeDayCost,
            quarterBonusMultiplier = quarterBonusMultiplier,
    )

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

    /**
     * Расчет заработной платы за текущий месяц при большом количестве отгулов
     *
     * [lastMonthOfQuarter:false]
     * [workDays:5] * [workDayCost:1000] + [overtimeDays:2] * [overtimeDayCost:1500] = 8000
     */
    @Test
    fun currentMonthPayForLowWork() {
        // Arrange
        val workDays = 5
        val overtimeDays = 2

        // Act
        val result = salaryCalculator.currentMonthPay(workDays, overtimeDays)

        // Assert
        Assertions.assertEquals(15000, result)
    }

    /**
     * Расчет заработной платы за текущий месяц при загрузке 200%
     *
     * [lastMonthOfQuarter:false]
     * [workDays:0] * [workDayCost:1000] + [overtimeDays:31] * [overtimeDayCost:1500] = 46500
     */
    @Test
    fun currentMonthPayForHeavyWork() {
        // Arrange
        val workDays = 0
        val overtimeDays = 31

        // Act
        val result = salaryCalculator.currentMonthPay(workDays, overtimeDays)

        // Assert
        Assertions.assertEquals(45000, result)
    }

    // endregion positives

    // region negatives

    @Test
    fun currentMonthPayWithNegativeWorkDays() {
        // Arrange
        val workDays = -1
        val overtimeDays = 2

        // Act
        val result = Assertions.assertThrows(RuntimeException::class.java) {
            salaryCalculator.currentMonthPay(workDays, overtimeDays)
        }

        // Assert
        Assertions.assertEquals("Work day should be greater or equals 0", result.localizedMessage)
    }

    @Test
    fun currentMonthPayWithNegativeOvertimeDays() {
        // Arrange
        val workDays = 20
        val overtimeDays = -1

        // Act
        val result = Assertions.assertThrows(RuntimeException::class.java) {
            salaryCalculator.currentMonthPay(workDays, overtimeDays)
        }

        // Assert
        Assertions.assertEquals("Overtime day should be greater or equals 0", result.localizedMessage)
    }

    // endregion negatives
}