package library.jdp.jdppattern.base

import android.animation.Animator
import android.content.Intent
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
import library.jdp.jdppattern.R
import library.jdp.jdppattern.util.FragmentChange
import java.lang.ref.WeakReference



abstract class DrawerActivity : AppCompatActivity() {
    companion object {
        private var navigationButton: WeakReference<View>? = null
        private var actionbar: ActionBar? = null
        private var isDetailedPage = false

        fun backToHome() {
            YoYo.with(Techniques.RotateIn)
                    .duration(500)
                    .withListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationCancel(animator: Animator) {
                            isDetailedPage = false
                            actionbar!!.setHomeAsUpIndicator(initMenuIcon())
                        }
                        override fun onAnimationRepeat(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                            isDetailedPage = false
                            actionbar!!.setHomeAsUpIndicator(initMenuIcon())
                        }
                    })
                    .playOn(navigationButton!!.get())
        }


        fun setAsDetailFragment() {
            YoYo.with(Techniques.RotateIn).duration(500).withListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {
                    actionbar!!.setDisplayHomeAsUpEnabled(true)
                    actionbar!!.setHomeButtonEnabled(true)
                    actionbar!!.setHomeAsUpIndicator(initBackIcon())
                    isDetailedPage = true
                }
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    actionbar!!.setDisplayHomeAsUpEnabled(true)
                    actionbar!!.setHomeButtonEnabled(true)
                    actionbar!!.setHomeAsUpIndicator(initBackIcon())
                    isDetailedPage = true
                }
            }).playOn(navigationButton!!.get())

        }

        private fun  initMenuIcon(): Int {
            return R.drawable.ic_menu_black_24dp
        }
        private fun  initBackIcon(): Int {
            return R.drawable.ic_arrow_back_black_24dp
        }
    }

    private var isDrawerOpen = false
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
        FragmentChange.load(Integer.parseInt(initDrawerFragmentID().toString()),supportFragmentManager,drawerFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Integer.parseInt(initContentView().toString()))
        unbinder = ButterKnife.bind(this)
        setSupportActionBar(initSupportingActionBar())
        actionbar = supportActionBar
        val toggle =object : ActionBarDrawerToggle(
                this,
                initDrawerLayout(),
                initSupportingActionBar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                if (slideOffset == 0f ) {
                    // drawer closed
                    isDrawerOpen=false
                    YoYo.with(Techniques.RotateIn).duration(300).withListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            Companion.actionbar!!.setHomeAsUpIndicator(Companion.initMenuIcon())
                        }
                        override fun onAnimationCancel(animator: Animator) {
                            Companion.actionbar!!.setHomeAsUpIndicator(Companion.initMenuIcon())
                        }
                        override fun onAnimationRepeat(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                        }
                    }).playOn(navigationButton!!.get())
                }else if(1f==slideOffset) {
                    // started opening
                    isDrawerOpen=true
                    YoYo.with(Techniques.RotateIn).duration(300).withListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            Companion.actionbar!!.setHomeAsUpIndicator(Companion.initBackIcon())
                        }
                        override fun onAnimationCancel(animator: Animator) {
                            Companion.actionbar!!.setHomeAsUpIndicator(Companion.initBackIcon())
                        }
                        override fun onAnimationRepeat(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                        }
                    }).playOn(navigationButton!!.get())

                }
               }
        }

        initDrawerLayout().setDrawerListener(toggle)
        toggle.syncState()
        setDrawerLayout()
        initComponents()
        initServices()
        navigationButton = WeakReference<View>(initSupportingActionBar().getChildAt(1))
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setHomeButtonEnabled(true)
        actionbar!!.setHomeAsUpIndicator(initMenuIcon())
        navigationButton!!.get()!!.setBackgroundColor(Color.TRANSPARENT)
        navigationButton!!.get()!!.setOnClickListener {
            if(isDetailedPage){
                backToHome()
                onBackPressed()
            }else if(!isDrawerOpen) {
                initDrawerLayout().openDrawer(Gravity.START)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }


    override fun onBackPressed() {
        if (initDrawerLayout().isDrawerOpen(GravityCompat.START))
            initDrawerLayout().closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
    fun showDialog(intent: Intent){
        startActivity(intent)
    }
}