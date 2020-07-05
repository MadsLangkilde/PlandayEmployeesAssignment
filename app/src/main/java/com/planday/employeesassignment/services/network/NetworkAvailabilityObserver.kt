package com.planday.employeesassignment.services.network

interface NetworkAvailabilityObserver {
    fun onNetworkAvailabilityChanged(isConnected: Boolean)
}