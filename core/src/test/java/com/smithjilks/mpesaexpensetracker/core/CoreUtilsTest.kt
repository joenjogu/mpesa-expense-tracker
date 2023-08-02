package com.smithjilks.mpesaexpensetracker.core

import com.smithjilks.mpesaexpensetracker.core.constants.AppConstants
import com.smithjilks.mpesaexpensetracker.core.model.Record
import com.smithjilks.mpesaexpensetracker.core.utils.CoreUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class CoreUtilsTest {
    @Test
    fun testFormatAmount() {
        val amount = 100.0
        assertTrue("Ksh 100.00" == CoreUtils.formatAmount(amount))
        assertFalse("Ksh 100.000" == CoreUtils.formatAmount(amount))
        assertFalse("Ksh 100.0" == CoreUtils.formatAmount(amount))
        assertFalse("Ksh 100" == CoreUtils.formatAmount(amount))
    }

    @Test
    fun testFormatDateTime() {
        assertTrue("Fri, May 12 2023" == CoreUtils.formatTimestamp(1683898085000))
        assertTrue("04:28 pm" == CoreUtils.formatTime(1683898085000))

    }

    @Test
    fun testDaysAgo() {
        val MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24

        assertTrue("Fri, May 12 2023" == CoreUtils.daysAgo(1683898085000))
        assertTrue("Today" == CoreUtils.daysAgo(Date().time))
        assertFalse("Yesterday" == CoreUtils.daysAgo(Date().time))
        assertTrue("Yesterday" == CoreUtils.daysAgo((Date().time - MILLIS_IN_A_DAY)))
        assertFalse("Today" == CoreUtils.daysAgo((Date().time - MILLIS_IN_A_DAY)))

    }

    @Test
    fun testIsPhoneNumberValid() {
        assertTrue(CoreUtils.isValidPhoneNumber("0712345678"))
        assertTrue(CoreUtils.isValidPhoneNumber("0112345678"))
        assertFalse(CoreUtils.isValidPhoneNumber("254712345678"))
        assertFalse(CoreUtils.isValidPhoneNumber("254712345678"))
        assertFalse(CoreUtils.isValidPhoneNumber("+2540712345678"))
        assertFalse(CoreUtils.isValidPhoneNumber("071235678c"))
        assertFalse(CoreUtils.isValidPhoneNumber("0712345678g"))
        assertFalse(CoreUtils.isValidPhoneNumber("71234568"))
        assertFalse(CoreUtils.isValidPhoneNumber("7123456897"))
    }

    @Test
    fun testConvertDateAndTimeToTimestamp() {
        assertTrue(1684587780000 == CoreUtils.convertDateAndTimeToTimestamp("20/5/23", " 4:03 PM"))
        assertTrue(0L == CoreUtils.convertDateAndTimeToTimestamp(null, null))

    }


    @Test
    fun testCreateRecordFromMpesaMessage() {
        val withdrawalRecord = Record(
            0,
            transactionRef = "REK4ZY2YCC",
            category = AppConstants.DEFAULT_CATEGORY,
            amount = 600,
            transactionCost = 28,
            note = "Withdrawal",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("20/5/23", "4:32 PM"),
            account = "",
            payee = "353403 - New Nairobi-Naivasha First communication Nairobi-Mai Mahiu stage ",
            recordType = AppConstants.EXPENSE,
            recordImageResourceId = null
        )

        val createdWithdrawalRecord = CoreUtils.createRecordFromMpesaMessage(
            "REK4ZY2YCC Confirmed.on 20/5/23 at 4:32 PMWithdraw Ksh600.00 " +
                    "from 353403 - New Nairobi-Naivasha First communication Nairobi-Mai Mahiu stage New M-PESA balance is Ksh8.91. " +
                    "Transaction cost, Ksh28.00. Amount you can transact within the day is 298,995.00. " +
                    "Withdraw your Fuliza limit at any M-Pesa agent Click https://bit.ly/3rhk97G or dial *334#"
        )

        val createdWithdrawalRecord2 = CoreUtils.createRecordFromMpesaMessage(
            "REK4ZY2YCC Confirmed.on 20/5/23 at 4:32 PM Ksh600.00 " +
                    "from 353403 - New Nairobi-Naivasha First communication Nairobi-Mai Mahiu stage New M-PESA balance is Ksh8.91. " +
                    "Transaction cost, Ksh28.00. Amount you can transact within the day is 298,995.00. " +
                    "Withdraw your Fuliza limit at any M-Pesa agent Click https://bit.ly/3rhk97G or dial *334#"
        )
        assertTrue(withdrawalRecord == createdWithdrawalRecord)
        assertTrue(withdrawalRecord == createdWithdrawalRecord2)


        val receivedRecord = Record(
            0,
            transactionRef = "REK5ZV0P9P",
            category = AppConstants.RECEIVED_MONEY,
            amount = 100,
            transactionCost = 0,
            note = "Received cash",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("20/5/23", "4:03 PM"),
            account = "",
            payee = "STANDARD CHARTERED BANK 329299",
            recordType = AppConstants.INCOME,
            recordImageResourceId = null
        )

        val createdReceivedRecord = CoreUtils.createRecordFromMpesaMessage(
            "REK5ZV0P9P Confirmed." +
                    "You have received Ksh100.00 from STANDARD CHARTERED BANK 329299 on 20/5/23 " +
                    "at 4:03 PM New M-PESA balance is Ksh16.91.  " +
                    "Separate personal and business funds through Pochi la Biashara on *334#."
        )

        val createdReceivedRecord2 = CoreUtils.createRecordFromMpesaMessage(
            "REK5ZV0P9P Confirmed." +
                    "You have  Ksh100.00 from STANDARD CHARTERED BANK 329299 on 20/5/23 " +
                    "at 4:03 PM New M-PESA balance is Ksh16.91.  " +
                    "Separate personal and business funds through Pochi la Biashara on *334#."
        )
        assertTrue(receivedRecord == createdReceivedRecord)
        assertTrue(null == createdReceivedRecord2)


        val buyGoodsRecord = Record(
            0,
            transactionRef = "REK61DLFRE",
            category = AppConstants.DEFAULT_CATEGORY,
            amount = 100,
            transactionCost = 0,
            note = "Buy Goods...",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("20/5/23", "6:39 PM"),
            account = "",
            payee = "Naivas Safari Centre Naivasha.",
            recordType = AppConstants.EXPENSE,
            recordImageResourceId = null
        )

        val createdBuyGoodsRecord = CoreUtils.createRecordFromMpesaMessage(
            "REK61DLFRE Confirmed. Ksh100.00 paid to Naivas Safari Centre Naivasha. on 20/5/23 at 6:39 PM." +
                    "New M-PESA balance is Ksh70.00. Transaction cost, Ksh0.00. " +
                    "Amount you can transact within the day is 297,728.00. " +
                    "Pay to POCHI LA BIASHARA FREE for amount below Kshs 100."
        )
        assertTrue(buyGoodsRecord == createdBuyGoodsRecord)


        val payBillRecord = Record(
            transactionRef = "REL83EC6RG",
            category = AppConstants.DEFAULT_CATEGORY,
            amount = 2500,
            transactionCost = 0,
            note = "Pay Bill...",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("21/5/23", "2:59 PM"),
            account = "",
            payee = "KPLC PREPAID for account 37228894004",
            recordType = AppConstants.EXPENSE,
            recordImageResourceId = null
        )

        val createdPayBillRecord = CoreUtils.createRecordFromMpesaMessage(
            "REL83EC6RG Confirmed. " +
                    "Ksh2,500.00 sent to KPLC PREPAID for account 37228894004 on 21/5/23 at 2:59 PM " +
                    "New M-PESA balance is Ksh90.91. Transaction cost, Ksh0.00." +
                    "Amount you can transact within the day is 122,899.00. " +
                    "Buy business stock with M-PESA GlobalPay virtual Visa card. " +
                    "Click on https://mpesaapp.page.link/GlobalPay"
        )
        assertTrue(payBillRecord == createdPayBillRecord)


        val sendMoneyRecord = Record(
            transactionRef = "REL93DKL7Z",
            category = AppConstants.DEFAULT_CATEGORY,
            amount = 1,
            transactionCost = 0,
            note = "Send Money...",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("21/5/22", "1:51 PM"),
            account = "",
            payee = "JOHN  DOE 0712345678",
            recordType = AppConstants.EXPENSE,
            recordImageResourceId = null
        )

        val createdSendMoneyRecord = CoreUtils.createRecordFromMpesaMessage(
            "REL93DKL7Z Confirmed. Ksh1.00 sent to JOHN  DOE 0712345678 on 21/5/22 at 1:51 PM. " +
                    "New M-PESA balance is Ksh0.00. Transaction cost, Ksh0.00. " +
                    "Amount you can transact within the day is 299,999.00. " +
                    "Use M-PESA app to send money conveniently mpesaapp.page.link/ggGV"
        )
        assertTrue(sendMoneyRecord == createdSendMoneyRecord)


        val buyAirtimeRecord = Record(
            transactionRef = "REK3YEJE2F",
            category = AppConstants.AIRTIME,
            amount = 300,
            transactionCost = 0,
            note = "Buy Airtime...",
            timestamp = CoreUtils.convertDateAndTimeToTimestamp("20/5/22", "2:26 AM"),
            account = "",
            payee = "Myself",
            recordType = AppConstants.EXPENSE,
            recordImageResourceId = null
        )

        val createdBuyAirtimeRecord = CoreUtils.createRecordFromMpesaMessage(
            "REK3YEJE2F confirmed.You bought Ksh300.00 of airtime on 20/5/22 at 2:26 AM." +
                    "New M-PESA balance is Ksh0.00. Transaction cost, Ksh0.00. " +
                    "Amount you can transact within the day is 129,400.00. " +
                    "Dial *234*0# to Opt in to FULIZA and check your limit."
        )
        assertTrue(buyAirtimeRecord == createdBuyAirtimeRecord)

    }

    @Test
    fun testConvertStringAmountToInt() {
        assertTrue(0 == CoreUtils.convertStringAmountToInt("0"))
        assertTrue(0 == CoreUtils.convertStringAmountToInt("0.0"))
        assertTrue(100 == CoreUtils.convertStringAmountToInt("100"))
        assertTrue(0 == CoreUtils.convertStringAmountToInt("Ksh 0.0"))
        assertTrue(1000 == CoreUtils.convertStringAmountToInt("1,000.00"))
        assertTrue(1000 == CoreUtils.convertStringAmountToInt("1 000.00"))
        assertTrue(1000 == CoreUtils.convertStringAmountToInt("1-000.00"))
        assertTrue(70000 == CoreUtils.convertStringAmountToInt("70,000.00"))
    }


}