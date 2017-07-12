package library.jdp.jdplib.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by jamesdeperio on 6/25/2017.
 */

abstract class BaseFragment : Fragment() {
    private var unbinder: Unbinder? = null
    var rootView: View? = null
    protected abstract fun initContentView(): Any
    protected abstract fun initComponents()
    protected abstract fun initServices()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(Integer.parseInt(initContentView().toString()), container, false)
        unbinder = ButterKnife.bind(this, rootView!!)
        initComponents()
        initServices()
        return rootView
    }
    override fun onDestroyView() {
        unbinder!!.unbind()
        unbinder=null
        super.onDestroyView()
    }


}
