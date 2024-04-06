package com.easyfitness.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.easyfitness.R
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class ProgramsPagerFragment : Fragment() {
    private var pagerAdapter: FragmentPagerItemAdapter? = null
    private lateinit var mViewPager: NonSwipeableViewPager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.program_pager, container, false)
        // Locate the viewpager in activity_main.xml
        mViewPager = view.findViewById(R.id.program_pager)
        if (mViewPager.adapter == null) {
            val args = this.arguments
            args!!.putLong("programID", -1)
            args.putLong("programProfile", -1)
            pagerAdapter = FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(context)
                .add(R.string.ExercisesInProgramLabel, ExercisesInProgramFragment::class.java)
                .add(R.string.ProgramsLabel, ProgramsFragment::class.java)
                .create())
            mViewPager.adapter = pagerAdapter
            val viewPagerTab: SmartTabLayout = view.findViewById(R.id.viewpagertab)
            viewPagerTab.setViewPager(mViewPager)
            viewPagerTab.setOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    val frag1 = pagerAdapter!!.getPage(position)
                    frag1?.onHiddenChanged(false) // Refresh data
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
        return view
    }

//    private val viewPagerAdapter: FragmentPagerItemAdapter?
//        get() = (requireView().findViewById<View>(R.id.program_pager) as NonSwipeableViewPager).adapter as FragmentPagerItemAdapter?

//    override fun onHiddenChanged(hidden: Boolean) {
//        if (!hidden) {
//            if (viewPagerAdapter != null) {
//                var frag1: Fragment?
//                for (i in 0..2) {
//                    frag1 = viewPagerAdapter!!.getPage(i)
//                    frag1?.onHiddenChanged(false) // Refresh data
//                }
//            }
//        }
//    }

    companion object {
        fun newInstance(name: String, id: Int): ProgramsPagerFragment {
            val f = ProgramsPagerFragment()
            val args = Bundle()
            args.putString("name", name)
            args.putInt("id", id)
            f.arguments = args
            return f
        }
    }
}
