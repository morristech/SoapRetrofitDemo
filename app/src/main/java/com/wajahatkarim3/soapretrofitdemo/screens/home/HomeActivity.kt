package com.wajahatkarim3.soapretrofitdemo.screens.home

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.wajahatkarim3.soapretrofitdemo.R
import com.wajahatkarim3.soapretrofitdemo.base.BaseActivity
import com.wajahatkarim3.soapretrofitdemo.custom.CurrentDateDecorator
import com.wajahatkarim3.soapretrofitdemo.custom.EventDecortor
import com.wajahatkarim3.soapretrofitdemo.custom.TodayDecorator
import com.wajahatkarim3.soapretrofitdemo.databinding.ActivityHomeBinding
import com.wajahatkarim3.soapretrofitdemo.utils.toCalendarDay
import com.wajahatkarim3.soapretrofitdemo.utils.toJodaDate
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class HomeActivity : BaseActivity(), HomeContract.View {

    lateinit var bi: ActivityHomeBinding
    lateinit var presenter: HomePresenter
    lateinit var repository: HomeContract.Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = DataBindingUtil.setContentView(this, R.layout.activity_home)

        repository = HomeRepository(retrofit, this)
        presenter = HomePresenter(this, repository)
        bi.view = this
        bi.presenter = presenter
        presenter.initScreen()
    }

    override fun setupViews() {
        // Month Year Headings
        setMonthYearHeading(DateTime())

        // Calendar
        bi.calendarView.topbarVisible = false
        bi.calendarView.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS
        bi.calendarView.addDecorator(CurrentDateDecorator(this))
        bi.calendarView.setDateTextAppearance(R.style.TodayTextAppearance)

        // Today
        bi.txtDateSelected.text = DateTime().toString("EEEE, MMMM dd")

        // Calendar Listeners
        bi.calendarView.setOnMonthChangedListener { widget, date ->
            var dd = DateTime(date.date)
            dd = dd.plusMonths(1)
            setMonthYearHeading(dd)
        }
        bi.calendarView.setOnDateChangedListener { widget, date, selected ->
            var datetime = date.toJodaDate()
            bi.txtDateSelected.text = datetime.toString("EEEE, MMMM dd")
        }

    }

    override fun showLoading(message: String) {
        showProgress(message)
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun setMonthYearHeading(date: DateTime) {
        bi.txtMonth.text = date.toString("MMMM")
        bi.txtYear.text = date.toString("YYYY")
        bi.txtDateSelected.text = date.toString("MMMM yyyy")

    }

    override fun updateDates(list: ArrayList<CalendarDay>) {
        hideLoading()
        bi.calendarView.addDecorator(EventDecortor(Color.RED, HashSet(list)))
        bi.calendarView.invalidateDecorators()
    }

}
