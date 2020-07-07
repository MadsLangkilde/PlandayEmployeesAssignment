package com.planday.employeesassignment.data.repositories

import androidx.databinding.ObservableField
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.EmployeesApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.*
import com.planday.employeesassignment.services.network.NetworkAvailabilityObserver
import io.reactivex.Maybe
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.Response


class EmployeesRepository : androidx.databinding.Observable.OnPropertyChangedCallback(), NetworkAvailabilityObserver {

    companion object {
        val instance: EmployeesRepository = EmployeesRepository()
    }

    private lateinit var apiAuthenticationRepository:   ApiAuthenticationRepository
    private lateinit var employeesApi:                  EmployeesApi
    private lateinit var clientId:                      String
    private lateinit var refreshToken:                  String
    private lateinit var pagingDetails:                 Paging
    var limit:                                          Int                                 = 20
    var totalEmployeesLoaded:                           Long                                = 0
    var nextLoadedPageNumber:                           Int                                 = 0

    var employeesSubject:                               PublishSubject<List<BaseEmployee>>  = PublishSubject.create<List<BaseEmployee>>()
    var employeeUpdatedSubject:                         PublishSubject<BaseEmployee>        = PublishSubject.create<BaseEmployee>()
    private val networkAvailable:                       ObservableField<Boolean>            = ObservableField()

    fun init(apiAuthenticationRepository: ApiAuthenticationRepository, employeesApi: EmployeesApi, clientId: String, refreshToken: String) {
        this.apiAuthenticationRepository    = apiAuthenticationRepository
        this.employeesApi                   = employeesApi
        this.clientId                       = clientId
        this.refreshToken                   = refreshToken

        apiAuthenticationRepository.openApiAuthentication.addOnPropertyChangedCallback(this)
    }

    fun loadNextEmployeesChunk() {
        if (!::pagingDetails.isInitialized || pagingDetails.offset < nextLoadedPageNumber) {

            employeesApi.getEmployees(limit, nextLoadedPageNumber)
                .observeOn(Schedulers.io())
                .map { response ->
                    val employees = ArrayList<BaseEmployee>()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pagingDetails = it.paging
                            employees.addAll(it.data)
                        }
                    }
                    totalEmployeesLoaded += employees.size
                    employees
                }
                .subscribeOn(Schedulers.io())
                .subscribe(object : DisposableMaybeObserver<ArrayList<BaseEmployee>>() {
                    override fun onSuccess(baseEmployees: ArrayList<BaseEmployee>) {
                        val allPagesLoaded      = pagingDetails.total <= totalEmployeesLoaded
                        employeesSubject.onNext(baseEmployees)
                        if (allPagesLoaded)
                            employeesSubject.onComplete()
                        else
                            nextLoadedPageNumber++
                    }

                    override fun onComplete() {
                        dispose()
                    }

                    override fun onError(e: Throwable) {
                        dispose()
                    }
                })
        }
    }

    fun getEmployeeById(employeeId: Int): Maybe<Response<ResponseGetEmployeeById>> {
        return employeesApi.getEmployee(employeeId)
    }

    fun putEmployee(employee: Employee): Maybe<Response<ResponsePutEmployee>> {
        return employeesApi.putEmployee(employee)
    }

    override fun onPropertyChanged(sender: androidx.databinding.Observable?, propertyId: Int) {
        if (sender == apiAuthenticationRepository.openApiAuthentication)
            apiAuthenticationRepository.openApiAuthentication.get()?.accessToken?.let { employeesApi.accessToken = it }
    }

    override fun onNetworkAvailabilityChanged(isConnected: Boolean) {
        networkAvailable.set(isConnected)
    }
}