package com.smithjilks.mpesaexpensetracker.feature.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smithjilks.mpesaexpensetracker.core.model.Record
import com.smithjilks.mpesaexpensetracker.core.repository.AppDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AppDatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _recentRecordsList = MutableStateFlow<List<Record>>(emptyList())
    val recentRecordsList = _recentRecordsList.asStateFlow()

    var totalIncome by mutableStateOf(0.0)
        private set

    var totalExpenses by mutableStateOf(0.0)
        private set

    init {
        effect {
            repository.getRecentRecords().distinctUntilChanged().collect { listOfRecentRecords ->
                if (listOfRecentRecords.isNotEmpty()) {
                    _recentRecordsList.value = listOfRecentRecords
                } else {
                    emptyList<Record>()
                }
            }
        }

        effect {
            repository.getAllExpenseRecords().distinctUntilChanged()
                .collect { listOfExpenseRecords ->
                    if (listOfExpenseRecords.isNotEmpty()) {
                        listOfExpenseRecords.forEach {
                            totalExpenses += (it.amount.toDouble() + it.transactionCost.toDouble())
                        }
                    }
                }
        }

        effect {
            repository.getAllIncomeRecords().distinctUntilChanged().collect { listOfIncomeRecords ->
                if (listOfIncomeRecords.isNotEmpty()) {
                    listOfIncomeRecords.forEach {
                        totalIncome += (it.amount + it.transactionCost)
                    }
                }
            }
        }

    }

    fun insertRecords(records: List<Record>) = effect { repository.insertRecords(records) }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch { block() }    // 4
    }
}