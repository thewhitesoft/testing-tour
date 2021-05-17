package ru.thewhite.testingtour

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/**
 * @author Maxim Seredkin
 */
internal class SalaryCalculatorTest {
    lateinit var salaryCalculator: SalaryCalculator;

    @BeforeEach
    internal fun setUp() {
        salaryCalculator()
    }

    private fun salaryCalculator(
            minMonthPay: Int = 15000,
            maxMonthPay: Int = 45000,
            workDayCost: Int = 1000,
            overtimeDayCost: Int = 1500,
            quarterBonusMultiplier: Int = 2,
            bonusMonths: Array<Int> = arrayOf(),
            date: String = "2021-05-17T00:00:00.000Z",
    ) {
        this.salaryCalculator = SalaryCalculator(
                minMonthPay = minMonthPay,
                maxMonthPay = maxMonthPay,
                workDayCost = workDayCost,
                overtimeDayCost = overtimeDayCost,
                quarterBonusMultiplier = quarterBonusMultiplier,
                bonusMonths = bonusMonths,
                clock = Clock.fixed(Instant.parse(date), ZoneId.systemDefault())
        )
    }

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
     * Расчет заработной платы за текущий месяц без премии
     *
     * [lastMonthOfQuarter:false]
     * ([workDays:15] * [workDayCost:1000] + [overtimeDays:2] * [overtimeDayCost:1500]) * [quarterBonusMultiplier:2] = 36000
     */
    @Test
    fun currentMonthPayWithBonus() {
        // Arrange
        salaryCalculator(bonusMonths = arrayOf(5))

        val workDays = 15
        val overtimeDays = 2

        // Act
        val result = salaryCalculator.currentMonthPay(workDays, overtimeDays)

        // Assert
        Assertions.assertEquals(36000, result)
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