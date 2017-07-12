package library.jdp.jdplib.base

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import library.jdp.jdplib.util.FragmentUtil
import library.jdp.jdppattern.R

abstract class DrawerActivity : AppCompatActivity() {
    companion object {
        private var toggle: ActionBarDrawerToggle? = null
        private var navigationButton: View? = null
        private var actionbar: ActionBar? = null
        private var isDetailedPage = false

        fun backToHome() {
            YoYo.with(Techniques.RotateIn)
                    .duration(500)
                    .withListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                            isDetailedPage = false
                            actionbar!!.setHomeAsUpIndicator(initMenuIcon())
                        }
                    })
                    .playOn(navigationButton)
        }


        fun setAsDetailFragment() {
            YoYo.with(Techniques.RotateIn).duration(500).withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    actionbar!!.setDisplayHomeAsUpEnabled(true)
                    actionbar!!.setHomeButtonEnabled(true)
                    actionbar!!.setHomeAsUpIndicator(initBackIcon())
                    isDetailedPage = true
                }
            }).playOn(navigationButton)

        }

        private fun  initMenuIcon(): Int {
            return R.drawable.ic_menu_black_24dp
        }
        private fun  initBackIcon(): Int {
            return R.drawable.ic_arrow_back_black_24dp
        }
    }

    private var unbinder: Unbinder? = null
    protected abstract fun initContentView(): Any
    protected abstract fun initSupportingActionBar(): Toolbar
    protected abstract fun initDrawerLayout(): DrawerLayout
    protected abstract fun initDrawerFragmentID(): Any
    protected abstract fun initDrawerFragment(): DrawerFragment
    protected abstract fun initComponents()
    protected abstract fun initServices()

    private fun setDrawerLayout() {
        val drawerFragment: DrawerFragment? = initDrawerFragment()
        drawerFragment!!.drawerLayout = initDrawerLayout()
        FragmentUtil.loadFragment(Integer.parseInt(initDrawerFragmentID().toString()),supportFragmentManager,drawerFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Integer.parseInt(initContentView().toString()))
        unbinder = ButterKnife.bind(this)
        setSupportActionBar(initSupportingActionBar())
        actionbar = supportActionBar
        toggle = ActionBarDrawerToggle(
                this, initDrawerLayout(), initSupportingActionBar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        initDrawerLayout().setDrawerListener(toggle)
        toggle!!.syncState()
        setDrawerLayout()
        initComponents()
        initServices()
        navigationButton = initSupportingActionBar().getChildAt(1)
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeButtonEnabled(true)
        actionbar!!.setHomeAsUpIndicator(Companion.initMenuIcon())
        navigationButton!!.setBackgroundColor(Color.TRANSPARENT)
        navigationButton!!.setOnClickListener {
            if(isDetailedPage){
                backToHome()
                onBackPressed()
            }else{
                YoYo.with(Techniques.RotateIn).duration(200).withListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        initDrawerLayout().openDrawer(Gravity.START)
                    }
                }).playOn(navigationButton)

            }

        }
    }

    override fun onDestroy() {
        unbinder!!.unbind()
        super.onDestroy()
    }


    override fun onBackPressed() {
        if (initDrawerLayout().isDrawerOpen(GravityCompat.START))
            initDrawerLayout().closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }



}