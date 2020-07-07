package com.planday.employeesassignment.app.components.framework

import androidx.fragment.app.Fragment
import com.planday.employeesassignment.R

abstract class PDBaseFragment : Fragment(), PDFragment{

    fun getParentActivity() : PDBaseActivity {
        return activity as PDBaseActivity
    }

    override fun pushFragment(fragment: PDFragment) {
        getParentActivity().pushFragment(fragment)
    }

    override fun popFragment() {
        getParentActivity().popFragment()
    }

     override fun enterAnimation(): Int {
         return R.anim.slide_in_from_right
     }

     override fun exitAnimation(): Int {
         return R.anim.slide_out_to_left
     }

     override fun popEnterAnimation(): Int {
         return R.anim.slide_in_from_left
     }

     override fun popExitAnimation(): Int {
         return R.anim.slide_out_to_right
     }

 }