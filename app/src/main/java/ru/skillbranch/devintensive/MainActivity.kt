package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender
import ru.skillbranch.devintensive.models.Bender.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var  benderObj: Bender

    val bundleKeyStatus: String = "STATUS"
    val bundleKeyQuestion: String = "QUESTION"
    val bundleKeyEditText: String = "EDITTEXT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // legacy
        // benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        // EditText - allow soft keyboard button
        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                sendBtn.performClick()
            }
            false
        }

        // restore saved values
        val status = savedInstanceState?.getString(bundleKeyStatus) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(bundleKeyQuestion) ?: Bender.Question.NAME.name
        val text = savedInstanceState?.getString(bundleKeyEditText) ?: ""

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))
        messageEt.setText(text)

        Log.d("M_MainActivity", "onCreate $status $question '$text'")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()

        sendBtn.setOnClickListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    private fun isAnswerValid(): Boolean {
        return benderObj.question.validate(messageEt.text.toString())
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send)
            if (isAnswerValid())
                sendAnswer()
            else
                displayMessageIfNotValid()
    }

    private fun displayMessageIfNotValid() {
        val errorMessage = when(benderObj.question){
            Question.NAME -> "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> "Материал не должен содержать цифр"
            Question.BDAY -> "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
            else -> "На этом все, вопросов больше нет"
        }
        textTxt.text = errorMessage + "\n" + benderObj.question.question
        messageEt.setText("")
    }

    private fun sendAnswer() {
        val (phase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
        messageEt.setText("")
        val(r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phase
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(bundleKeyStatus, benderObj.status.name)
        outState?.putString(bundleKeyQuestion, benderObj.question.name)
        val editText = messageEt.text.toString()
        outState?.putString(bundleKeyEditText, editText)
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name} '$editText'")
    }
}
