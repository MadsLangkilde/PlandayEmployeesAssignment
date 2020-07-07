package com.planday.employeesassignment.app.components.framework

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlin.random.Random

abstract class PDBaseActivity : AppCompatActivity() {

    abstract fun getFragmentContainer(): Int

    fun pushFragment(fragment: PDFragment) {
        if (fragment !is Fragment)
            return

        val currentFragment:        Fragment?               = supportFragmentManager.findFragmentById(getFragmentContainer())
        val fragmentTransaction:    FragmentTransaction     = supportFragmentManager.beginTransaction()
        val generatedFragmentTag:   String                  = generateFragmentTag(fragment)
    
        fragmentTransaction.setCustomAnimations(fragment.enterAnimation(), fragment.exitAnimation(), fragment.popEnterAnimation(), fragment.popExitAnimation())
        fragmentTransaction.add(getFragmentContainer(), fragment, generatedFragmentTag)
        fragmentTransaction.addToBackStack(generatedFragmentTag)

        if (currentFragment != null)
            fragmentTransaction.hide(currentFragment)

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun popFragment() {
        if (supportFragmentManager.backStackEntryCount <= 1)
            return

        val currentFragment:        PDFragment?             = getTopFragment()
        val previousFragment:       PDFragment?             = getPreviousFragment()

        val fragmentTransaction:    FragmentTransaction?    = supportFragmentManager.beginTransaction()

        if (currentFragment !is Fragment || previousFragment !is Fragment)
            return

        if (previousFragment.isHidden)
            fragmentTransaction?.show(previousFragment)

        fragmentTransaction?.remove(currentFragment)
        supportFragmentManager.popBackStack()
        fragmentTransaction?.commitAllowingStateLoss()
    }

    fun popBackToFragmentWithTag(popBackToTag: String) {
        val topFragmentIndex:   Int         = supportFragmentManager.backStackEntryCount - 1
        var i:                  Int         = topFragmentIndex
        while (i >= 0) {
            val foundFragment:  PDFragment? = getFragmentAtIndex(i)
            if (foundFragment is Fragment) {
                if (i >= 1) {
                    if (foundFragment.getFragmentTag() == popBackToTag)
                        break
                    else
                        supportFragmentManager.popBackStack()
                } else
                    break
            }
            i--
        }
    }

    private fun generateFragmentTag(fragment: PDFragment): String {
        var tag: String = fragment.getFragmentTag()
        val tagIsOccupied: Boolean = supportFragmentManager.findFragmentByTag(tag) != null

        while (tagIsOccupied) {
            val randomTagSuffix: Int = Random.nextInt(99999 - 1) + 1
            val newTag: String = "$tag-$randomTagSuffix"
            if (supportFragmentManager.findFragmentByTag(newTag) == null) {
                tag = newTag
                break
            }
        }
        return tag
    }

    private fun getTopFragment(): PDFragment? {
        var index: Int = supportFragmentManager.backStackEntryCount - 1
        if (index >= 0) {
            while (index >= 0) {
                val foundTopFragment: PDFragment? = getFragmentAtIndex(index)
                if (foundTopFragment != null && foundTopFragment is Fragment) {
                    return foundTopFragment
                }
                index--
            }
        }

        return null
    }

    fun getPreviousFragment(): PDFragment? {
        var index: Int = supportFragmentManager.backStackEntryCount - 2
        if (index >= 0) {
            while (index >= 0) {
                val foundTopFragment: PDFragment? = getFragmentAtIndex(index)
                if (foundTopFragment != null && foundTopFragment is Fragment) {
                    return foundTopFragment
                }
                index--
            }
        }
        return null
    }

    private fun getFragmentAtIndex(index: Int): PDFragment? {
        val bacstackCount: Int = supportFragmentManager.backStackEntryCount
        if (bacstackCount <= index || index < 0) {
            return null
        }
        val backstackEntry: FragmentManager.BackStackEntry? = supportFragmentManager.getBackStackEntryAt(index)
        val tag: String? = backstackEntry?.name
        return supportFragmentManager.findFragmentByTag(tag) as PDFragment?
    }
}