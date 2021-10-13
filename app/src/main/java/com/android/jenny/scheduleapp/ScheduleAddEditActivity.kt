package com.android.jenny.scheduleapp

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.jenny.scheduleapp.databinding.ActivityScheduleAddEditBinding
import com.android.jenny.scheduleapp.model.Schedule
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener


class ScheduleAddEditActivity: AppCompatActivity() {
    companion object {
        private const val TAG = "ScheduleAddEditActivity"
        private val SCHEDULE_DAYS = mutableListOf("Sun","Mon","Tue","Wed","Thur","Fri","Sat")
        private val SCHEDULE_DAYS_ORDER: HashMap<String, Int> = hashMapOf(
            "Sun" to 0,
            "Mon" to 1,
            "Tue" to 2,
            "Wed" to 3,
            "Thur" to 4,
            "Fri" to 5,
            "Sat" to 6
        )
        private const val SCHEDULE_TIMES = "00:00"
    }
    private lateinit var binding: ActivityScheduleAddEditBinding
    private lateinit var textViewScheduleOnOff: TextView
    private lateinit var useEachButton: ImageButton
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button

    private lateinit var sunButton: Button
    private lateinit var monButton: Button
    private lateinit var tuesButton: Button
    private lateinit var wedButton: Button
    private lateinit var thursButton: Button
    private lateinit var friButton: Button
    private lateinit var satButton: Button

//    private lateinit var timeCancelButton: Button
//    private lateinit var timeOkButton: Button

    private lateinit var key: String
    private var position: Int? = null
    private var dayList = ArrayList<String>()
    private var btnArray = ArrayList<Button>()

    private var useEachOnOff = "y"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate - start")
        super.onCreate(savedInstanceState)

        performDataBinding()
        key = getIntentKey()
        when (key) {
            "addScheduleKey" -> {
                initScheduleData()
            }
            "editScheduleKey" -> {
                binding.textviewScheduleAddEditTitle.setText(R.string.edit)
                position = intent.getIntExtra("position", 1)
                val editScheduleData: Schedule = intent.getParcelableExtra(ScheduleMainActivity.EDIT_SCHEDULE_DATA)!!
                binding.buttonRemove.visibility = View.VISIBLE
                loadEditScheduleData(editScheduleData)
            }
        }
    }

    fun removeButtonClick() {
        Log.e(TAG, "removeBtnClick - start")
        showRemoveAlertDialog()
//        removeForScheduleListActivity()
    }

   //TODO: removeDialog
    private fun showRemoveAlertDialog() {
       val builder = AlertDialog.Builder(this)
       builder.setTitle(R.string.schedule_remove_title)
       builder.setMessage(R.string.schedule_remove_message)

       builder.setPositiveButton(R.string.ok) { _, _ ->
           removeForScheduleListActivity()
       }
       builder.setNegativeButton(R.string.cancel) { dialog, _ ->
           dialog.cancel()
       }
       builder.show()
    }

    private fun removeForScheduleListActivity() {
        Log.e(TAG, "removeForScheduleListActivity - start")
        val intent = Intent(this@ScheduleAddEditActivity, ScheduleMainActivity::class.java)
        intent.putExtra("key", "removeScheduleKey")
        intent.putExtra("position", position)
//        intent.putExtra(ScheduleListActivity.EDIT_SCHEDULE_DATA, getInputData())
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
    }

    fun saveButtonClick() {
        // false: "00:00" , true: settingTimes
        if (!checkTimes()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.schedule_error)
            builder.setMessage(R.string.schedule_times_error_message)

            builder.setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        } else if (dayList.size == 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.schedule_error)
            builder.setMessage(R.string.schedule_days_error_message)

            builder.setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        } else {
            saveForScheduleListActivity()
        }
    }

    private fun checkTimes(): Boolean {
        val startTime = startTimeButton.text.toString()
        val endTime = endTimeButton.text.toString()
        return !(startTime == SCHEDULE_TIMES && endTime == SCHEDULE_TIMES)
    }

    private fun saveForScheduleListActivity() {
        val intent = Intent(this@ScheduleAddEditActivity, ScheduleMainActivity::class.java)
        when(key) {
            "editScheduleKey" -> {
                intent.putExtra("key", "editScheduleKey")
                intent.putExtra("position", position)
                intent.putExtra(ScheduleMainActivity.EDIT_SCHEDULE_DATA, getResultEditScheduleData())
            }
            "addScheduleKey" -> {
                intent.putExtra("key", "addScheduleKey")
                intent.putExtra(ScheduleMainActivity.ADD_SCHEDULE_DATA, getResultEditScheduleData())
            }
        }
        setResult(RESULT_OK, intent)
        if (!isFinishing) finish()
        Log.e(TAG, "$key _data 전달 완료!")
    }

    private fun getResultEditScheduleData(): Schedule {
        return Schedule(
            textViewScheduleOnOff.text.toString(),
            useEachOnOff,
            sortScheduleDays(dayList),
            startTimeButton.text.toString(),
            endTimeButton.text.toString()
        )
    }

    private fun sortScheduleDays(days: MutableList<String>): String {
        Log.e(TAG, "sortScheduleDays_copy_start: $days")
        val comparator = Comparator {o1: String, o2: String ->
            return@Comparator SCHEDULE_DAYS_ORDER[o1]!! - SCHEDULE_DAYS_ORDER[o2]!!
        }
        val copy = mutableListOf<String>().apply { addAll(days) }
        Log.e(TAG,"sortScheduleDays_copy_before: $copy")
        copy.sortWith(comparator)
        Log.e(TAG, "sortScheduleDays_copy_after: $copy")
        return copy.distinct().joinToString(",")
    }

    fun settingScheduleNameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit_schedule_name)

        val input = EditText(this)
        input.hint = textViewScheduleOnOff.text
        input.inputType = InputType.TYPE_CLASS_TEXT

        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = LengthFilter(20)
        input.filters = filterArray

        builder.setView(input)

        builder.setPositiveButton(R.string.ok) { _, _ ->
            val editName = input.text.toString()
            textViewScheduleOnOff.text = editName
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    fun useEachButtonClick(view: View) {
        Log.e(TAG, "useEachButtonClick:${view.isSelected}")
        val useSelected = !useEachButton.isSelected
        Log.e(TAG, "useEachButtonClick: $useEachButton")
        if (useSelected) {
            setUseEachButtonTrue()
        } else {
            setUseEachButtonFalse()
        }
    }

    private fun setUseEachButtonTrue() {
        useEachButton.isSelected = true
        useEachOnOff = "y"
        setAllButtonEnabledTrue()
        Log.e("setUseEachButtonTrue", "true:${useEachButton.isSelected}" )
    }

    private fun setUseEachButtonFalse() {
        useEachButton.isSelected = false
        useEachOnOff = "n"
        setAllButtonEnabledFalse()
        Log.e("setUseEachButtonFalse", "false:${useEachButton.isSelected}" )
    }

    private fun setAllButtonEnabledTrue() {
        btnArray.forEach { it.isEnabled = true }

        binding.textviewStartTime.setTextColor(this.getColor(R.color.grey_900))
        binding.textviewEndTime.setTextColor(this.getColor(R.color.grey_900))
        binding.textviewTimeTerm.setTextColor(this.getColor(R.color.grey_900))
        startTimeButton.isEnabled = true
        endTimeButton.isEnabled = true
        startTimeButton.setTextColor(this.getColor(R.color.green_700))
        endTimeButton.setTextColor(this.getColor(R.color.green_700))

        binding.buttonRemove.isEnabled = true
        binding.buttonRemove.setTextColor(this.getColor(R.color.coral))
    }

    private fun setAllButtonEnabledFalse() {
        btnArray.forEach { it.isEnabled = false }

        binding.textviewStartTime.setTextColor(this.getColor(R.color.grey_700))
        binding.textviewEndTime.setTextColor(this.getColor(R.color.grey_700))
        binding.textviewTimeTerm.setTextColor(this.getColor(R.color.grey_700))
        startTimeButton.isEnabled = false
        endTimeButton.isEnabled = false
        startTimeButton.setTextColor(this.getColor(R.color.grey_700))
        endTimeButton.setTextColor(this.getColor(R.color.grey_700))

        binding.buttonRemove.isEnabled = false
        binding.buttonRemove.setTextColor(this.getColor(R.color.grey_700))
    }

    fun dayButtonClick(view: View) {
        Log.e(TAG,"dayBtnClick: ${view.isSelected}")
        val selectedButtonState = !view.isSelected
        Log.e("dayBtnClick_current_state", "$selectedButtonState")
        val selectedButtonText = getTextFromDayButton(view.id)
        val selectedButton = getButtonFromText(selectedButtonText)

        if (selectedButtonState) {
            selectedButton.isSelected = true
            dayList.add(selectedButtonText)
        } else {
            selectedButton.isSelected = false
            dayList.remove(selectedButtonText)
        }
    }

    private fun getTextFromDayButton(id: Int): String {
        lateinit var day: String
        when (id) {
            sunButton.id -> day = this.getString(R.string.sun_day)
            monButton.id -> day = this.getString(R.string.mon_day)
            tuesButton.id -> day = this.getString(R.string.tues_day)
            wedButton.id -> day = this.getString(R.string.wed_day)
            thursButton.id -> day = this.getString(R.string.thurs_day)
            friButton.id -> day = this.getString(R.string.fri_day)
            satButton.id -> day = this.getString(R.string.sat_day)
        }
        Log.e("getTextFromDayButton()","day:$day")
        return day
    }

    private fun getButtonFromText(day: String): Button {
        lateinit var button: Button
        when (day) {
            this.getString(R.string.sun_day) -> button = sunButton
            this.getString(R.string.mon_day) -> button = monButton
            this.getString(R.string.tues_day) -> button = tuesButton
            this.getString(R.string.wed_day) -> button = wedButton
            this.getString(R.string.thurs_day) -> button = thursButton
            this.getString(R.string.fri_day) -> button = friButton
            this.getString(R.string.sat_day) -> button = satButton
        }
        return button
    }

     private fun checkDaysButtonFromEditData(days: String) {
        val editDayArray = days.split(",").toMutableList()
        Log.e("checkDaysButtonFromEditData", "days_param:$days")
         for (x in editDayArray.indices) {
             val button = getButtonFromText(editDayArray[x])
             setDaysButtonState(button)
            dayList.add(editDayArray[x])
            Log.e("checkDaysButtonFromEditData", "dayList: $dayList")
        }
    }

    private fun setDaysButtonState(button: Button) {
        btnArray.find { it == button }?.apply { isSelected = true }
    }

    fun scheduleTimesButtonClick(view: View) {
//        showTimePickerDialog(view)
        showNumberPickerDialog(view)
    }

    fun showNumberPickerDialog(view2: View) {
        val interval = 10
        val hourList = (0..24).toList()
        val hourStrConvertList = hourList.map {it.toString()}
        Log.e("hourStrConvertList", "$hourStrConvertList")

        val dialog = AlertDialog.Builder(this).create()
        val dialogLayout: LayoutInflater = LayoutInflater.from(this)
        val dialogView: View = dialogLayout.inflate(R.layout.dialog_timepicker, null)
        val hour: NumberPicker = dialogView.findViewById(R.id.timepicker_hour)
        val minute: NumberPicker = dialogView.findViewById(R.id.timepicker_minute)
        val okButton: Button = dialogView.findViewById(R.id.timepicker_button_ok)
        val cancelButton: Button = dialogView.findViewById(R.id.timepicker_button_cancel)

        hour.run {
            minValue = 0
            maxValue = hourStrConvertList.size - 1
            displayedValues = hourStrConvertList.toTypedArray()
        }
        minute.run {
            minValue = 0
            maxValue = ((60 / interval) - 1)
            val minuteStrConvertList = ArrayList<String>()
            minuteStrConvertList.let {
                var i = 0
                while (i <60) {
                    it.add(i.toString())
                    i += interval
                }
            }
            Log.e("minuteStrConvertList", "$minuteStrConvertList")
            displayedValues = minuteStrConvertList.toTypedArray()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }
        okButton.setOnClickListener {
        val formatTime = String.format("%02d", hour.value).plus(":").plus(String.format("%02d", minute.value))
        Log.e("formatTime", "$formatTime")
//            dialog.dismiss()
//            dialog.cancel()
            Log.e("okbutton","1")
            Log.e("view2.id", "${view2.id}")
            Log.e("button_startTime", "${R.id.button_startTime}")
            when (view2.id) {
                startTimeButton.id -> {
                    startTimeButton.text = String.format("%02d", hour.value).plus(":").plus(String.format("%02d", minute.value))
                }
                endTimeButton.id -> {
                    endTimeButton.text = String.format("%02d", hour.value).plus(":").plus(String.format("%02d", minute.value))
                }
            }
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    private fun showTimePickerDialog(view: View) {
        val timepicker = CustomTimePickerDialog(this,
            { _, sHour, sMinute ->
                setScheduleTimes(view, sHour, sMinute) }, 0, 0, false)
        timepicker.setTitle(R.string.timeSetting)
        timepicker.show()
    }

    private fun setScheduleTimes(view: View, sHour: Int, sMinute: Int) {
        when (view.id) {
            R.id.button_startTime -> { startTimeButton.text = String.format("%02d:%02d", sHour, sMinute) }
            R.id.button_endTime -> { endTimeButton.text = String.format("%02d:%02d", sHour, sMinute) }
        }
    }

    private fun initScheduleData() {
        Log.e(TAG, "initScheduleData - start")
        setUseEachButtonTrue()
        btnArray.forEach { it.isSelected = true }
        dayList.addAll(SCHEDULE_DAYS)
        setAllButtonEnabledTrue()
    }

    private fun loadEditScheduleData(data: Schedule) {
        Log.e(TAG, "loadEditScheduleData - start")
        Log.e(TAG, "loadEditScheduleData: $data")
        textViewScheduleOnOff.text = data.name
        startTimeButton.text = data.start
        endTimeButton.text = data.end

        checkDaysButtonFromEditData(data.day)
        if (data.use == "y") {
            setUseEachButtonTrue()
        } else {
            setUseEachButtonFalse()
        }
//        setUseEachButton(checkUseEachButtonState(data.use))
//        checkDaysButtonFromEditData(data.day)
//        getDayButtonBeforeEdit(data.day)
    }

    private fun getIntentKey(): String {
        Log.e(TAG, "getIntentKey() -start")
        return intent.getStringExtra("key").toString()
    }

    private fun performDataBinding() {
        Log.e(TAG, "performDataBinding - start")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_add_edit)
        binding.activity = this@ScheduleAddEditActivity

        textViewScheduleOnOff = binding.textviewScheduleEachOnOff
        useEachButton = binding.buttonScheduleUseEach
        startTimeButton = binding.buttonStartTime
        endTimeButton = binding.buttonEndTime

//        timeCancelButton = findViewById(R.id.timepicker_button_cancel)
//        timeOkButton = findViewById(R.id.timepicker_button_ok)

        sunButton = binding.buttonSun
        monButton = binding.buttonMon
        tuesButton = binding.buttonTues
        wedButton = binding.buttonWed
        thursButton = binding.buttonThurs
        friButton = binding.buttonFri
        satButton = binding.buttonSat

        btnArray.add(sunButton)
        btnArray.add(monButton)
        btnArray.add(tuesButton)
        btnArray.add(wedButton)
        btnArray.add(thursButton)
        btnArray.add(friButton)
        btnArray.add(satButton)
    }


}