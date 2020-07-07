package com.planday.employeesassignment.app.pages.editemployee.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.planday.employeesassignment.app.components.ActionLiveData
import com.planday.employeesassignment.app.components.bindingadapters.BindingAdaptersSelectionViews
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Employee
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponseGetEmployeeById
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponsePutEmployee
import com.planday.employeesassignment.data.repositories.EmployeesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class EditEmployeeViewModel : ViewModel(), BindingAdaptersSelectionViews.Companion.SpinnerSelectListener {


    val employeeRepository:         EmployeesRepository                     = EmployeesRepository.instance

    private var initiliazed:        Boolean                                 = false
    var employeeId:                 Int                                     = 0
    var email:                      ObservableField<String>                 = ObservableField()
    var firstName:                  ObservableField<String>                 = ObservableField()
    var lastName:                   ObservableField<String>                 = ObservableField()
    var firstNameError:             ObservableField<Boolean>                = ObservableField()
    var lastNameError:              ObservableField<Boolean>                = ObservableField()
    var errorOccurred:              ObservableField<Boolean>                = ObservableField()
    var submitInProgress:           ObservableField<Boolean>                = ObservableField()
    var gendersObservable:          ObservableField<ArrayList<String>>      = ObservableField(ArrayList())
    var selectedGenderValue:        ObservableField<String>                 = ObservableField("")
    var editSuccessListener:        ActionLiveData<Boolean>                 = ActionLiveData()
    var showKeyboardListener:       ActionLiveData<Boolean>                 = ActionLiveData()

    var employee:                   Employee?                               = null
    private var genderKeys:         ArrayList<String>                       = ArrayList()
    private var genderValues:       ArrayList<String>                       = ArrayList()

    init {
    }

    fun setGenderOptions(genderValues: ArrayList<String>) {
        this.gendersObservable.set(genderValues)
        this.genderValues   = genderValues
        this.genderKeys     = arrayListOf("Male", "Female")
    }

    fun init(employeeId: Int, firstName: String?, lastName: String?, email: String?) {
        if (initiliazed)
            return

        initiliazed = true
        this.employeeId = employeeId
        firstName?.let { this.firstName.set(it) }
        lastName?.let { this.lastName.set(it) }
        email?.let { this.employeeId = employeeId }

        fetchEmployee(employeeId)
    }

    private fun getGenderKeyFromValue(value: String): String? {
        val index = genderValues.indexOf(value) ?: return null
        return genderKeys.getOrNull(index)
    }

    private fun getGenderValueFromKey(genderKey: String): String? {
        val index = genderKeys.indexOf(genderKey)
        return genderValues.getOrNull(index)
    }

    private fun fetchEmployee(employeeId: Int) {
        employeeRepository.getEmployeeById(employeeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableMaybeObserver<Response<ResponseGetEmployeeById>>() {
                override fun onSuccess(response: Response<ResponseGetEmployeeById>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { employee ->
                            this@EditEmployeeViewModel.employee = employee
                            firstName.set(employee.firstName)
                            lastName.set(employee.lastName)
                            email.set(employee.email)
                            System.out.println("TESTTEST: employee.gender =" + employee.gender)
                            System.out.println("TESTTEST: getGenderValueFromKey(employee.gender) =" + getGenderValueFromKey(employee.gender))
                            selectedGenderValue.set(getGenderValueFromKey(employee.gender))
                        }
                    }
                }
                override fun onComplete() {
                    dispose()
                }

                override fun onError(e: Throwable) {
                    dispose()
                }
            })
    }


    fun onSubmitEmployeeChanges() {
        if (submitInProgress.get() == true)
            return

        showKeyboardListener.sendAction(false)

        employee?.let { employee ->
            submitInProgress.set(true)
            val firstName           = firstName.get() ?: ""
            val lastName            = lastName.get() ?: ""

            if (firstName.isEmpty() || lastName.isEmpty()) {
                firstNameError.set(firstName.isEmpty())
                lastNameError.set(lastName.isEmpty())
                errorOccurred.set(firstName.isEmpty() || lastName.isEmpty())
                submitInProgress.set(false)
            } else {
                employee.firstName  = firstName
                employee.lastName   = lastName

                System.out.println("TESTTEST: onSubmitEmployeeChanges employee.gender =" + employee.gender)
                putEmployee(employee)
            }
        }
    }

    private fun putEmployee(employee: Employee) {
        System.out.println("TESTTEST: putEmployee employee.gender =" + employee.gender)
        employeeRepository.putEmployee(employee)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableMaybeObserver<Response<ResponsePutEmployee>>() {
                override fun onSuccess(response: Response<ResponsePutEmployee>) {
                    System.out.println("TESTTEST: putEmployee response =" + response.code())
                    if (response.isSuccessful) {
                        employeeRepository.employeeUpdatedSubject.onNext(employee)
                        editSuccessListener.sendAction(true)
                    } else {
                        errorOccurred.set(true)
                    }
                    submitInProgress.set(false)
                }

                override fun onComplete() {
                    submitInProgress.set(false)
                    dispose()
                }

                override fun onError(e: Throwable) {
                    errorOccurred.set(true)
                    submitInProgress.set(false)
                    dispose()
                }
            })
    }

    override fun onSpinnerItemSelected(item: String) {
        System.out.println("TESTTEST: onSpinnerItemSelected item =" + item)
        getGenderKeyFromValue(item)?.let { employee?.gender = it }
        System.out.println("TESTTEST: getGenderKeyFromValue(item) item =" + getGenderKeyFromValue(item))
    }

}