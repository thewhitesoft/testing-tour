package ru.thewhite.testingtour

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.function.Consumer
import java.util.stream.Stream

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

    companion object {
        @JvmStatic
        fun currentMonthPay(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of("currentMonthPayWithoutBonus", 20, 2, 23000, Consumer<SalaryCalculatorTest> { }),
                    Arguments.of("currentMonthPayWithBonus", 15, 2, 36000, Consumer<SalaryCalculatorTest> { it.salaryCalculator(bonusMonths = arrayOf(5)) }),
                    Arguments.of("currentMonthPayForLowWork", 5, 2, 15000, Consumer<SalaryCalculatorTest> { }),
                    Arguments.of("currentMonthPayForHeavyWork", 0, 31, 45000, Consumer<SalaryCalculatorTest> { }),
            )
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    fun currentMonthPay(testName: String, workDays: Int, overtimeDays: Int, expectedResult: Int, arrangeFunc: Consumer<SalaryCalculatorTest>) {
        // Arrange
        arrangeFunc.accept(this)

        // Act
        val result = salaryCalculator.currentMonthPay(workDays, overtimeDays)

        // Assert
        Assertions.assertEquals(expectedResult, result)
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