package com.example.stateflowsample

import com.example.stateflowsample.detail.DetailModel
import com.example.stateflowsample.list.ListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object DataService {
    suspend fun getList(): ServiceResult<List<ListModel>> = withContext(Dispatchers.IO) {
        delay(2000)
        listOf(true, true, false).random().let {
            if (it) {
                ServiceResult.Success(
                    data = listOf(
                        ListModel("1", "Apple"),
                        ListModel("2", "Samsung"),
                        ListModel("3", "Google"),
                    )
                )
            } else {
                ServiceResult.Failure(
                    message = "Failed",
                    code = 400
                )
            }
        }
    }

    suspend fun getDetail(id: String): ServiceResult<DetailModel> = withContext(Dispatchers.IO) {
        delay(2000)
        listOf(true, true, false).random().let {
            if (it) {
                ServiceResult.Success(
                    data = DetailModel(
                        description = "Item$id description"
                    )
                )
            } else {
                ServiceResult.Failure(
                    message = "Failed",
                    code = 400
                )
            }
        }
    }

    suspend fun submitForm(): ServiceResult<Unit> = withContext(Dispatchers.IO) {
        delay(2000)
        listOf(true, false).random().let {
            if (it) {
                ServiceResult.Success(Unit)
            } else {
                val code = listOf(400, 409).random()
                ServiceResult.Failure(
                    message = if (code == 400) "Failed" else "Taken",
                    code = code
                )
            }
        }
    }
}