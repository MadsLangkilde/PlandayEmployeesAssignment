package com.planday.employeesassignment.app.pages.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.planday.employeesassignment.app.framework.PDBaseFragment

class EmployeesFragment : PDBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    /*

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel                               = ViewModelProviders.of(this).get(CustomersViewModel::class.java)
        viewModel.headlineFavorites             = getString(R.string.pages_customers_favorites_title)
        viewModel.headlineCustomersWithCatalogs = getString(R.string.pages_customers_catalogs_header)
        viewModel.headlineCustomersNoCatalogs   = getString(R.string.pages_customers_no_catalogs_header)
        viewModel.stringCatalogsPlural          = getString(R.string.shared_catalogs_plural)
        viewModel.allCategoriesString           = getString(R.string.pages_customers_filter_section_category_all)
        viewModel.isBigScreen                   = isBigScreen()
        viewModel.isLandscape                   = isLandscape()
        viewModel.screenWidth                   = ScreenUtil.getScreenWidth(context, true)
        val binding: LayoutCustomersBinding     = DataBindingUtil.inflate<LayoutCustomersBinding>(inflater, R.layout.layout_customers, container, false)
        binding.lifecycleOwner                  = this
        binding.customersViewModel              = viewModel

        viewModel.initToolbarConfig(title = getString(R.string.pages_customers_title), rightButtonListener = View.OnClickListener {
            viewModel.toggleSearchState()
        })

        return binding.root
    }
     */
    override fun getFragmentTag(): String {
        return "EmployeesFragment"
    }
}