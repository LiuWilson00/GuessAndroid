package com.roy.guess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.roy.guess.databinding.ActivityMaterialBinding
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.ed_number
import kotlinx.android.synthetic.main.content_material.*
import kotlinx.android.synthetic.main.linear_main.*

class MaterialActivity : AppCompatActivity() {
    val secretNumber = SecretNumber()
    val TAG = MainActivity::class.java.simpleName
    val excellentLimitNumber = 3
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_material)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            replay()
        }
        updateCountNumber()
        val count: Int = getSharedPreferences("guess", Context.MODE_PRIVATE)
            .getInt("REC_COUNTER", -1)
        val nick: String? = getSharedPreferences("guess", Context.MODE_PRIVATE)
            .getString("REC_NICKNAME", null)
        Log.d(TAG, "data: $count/$nick")
        Log.d(TAG, "onCreate: " + secretNumber.secret)
    }


    private fun replay() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.replay_game))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                secretNumber.reset()
                updateCountNumber()
                markButtonEnabled(ok_button)

                ed_number.setText("")
            }
            .setNeutralButton(getString(R.string.cancel), null)
            .show()
    }

    //    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_material)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
    fun updateCountNumber() {
        counter.setText(secretNumber.count.toString())
    }

    fun okButtonClick(view: View) {
        val n: Int = ed_number.text.toString().toInt()
        val alertTitle = getString(R.string.dialog_title)
        val diff: Int = secretNumber.validate(n)

        Log.d(TAG, getString(R.string.number_lable) + n)

        var message = victoryOrDefeat(diff)


//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        updateCountNumber()
        AlertDialog.Builder(this)
            .setTitle(alertTitle)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, whitch ->
                toRecordScreen(diff)
            }.show()


    }

    fun toRecordScreen(diff: Int) {
        if (diff == 0) {
            val intent = Intent(this, RecordActivity::class.java)
            intent.putExtra("COUNTER", secretNumber.count)
            resultLauncher.launch(intent)

        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val nickname = result.data?.getStringExtra("NICK")
                replay()
                Log.d(TAG, "nickname: $nickname")
            }
        }


    fun victoryOrDefeat(diff: Int): String {
        var message = getString(R.string.yes_you_got_it)
        if (diff < 0) {
            message = getString(R.string.bigger)
        } else if (diff > 0) {
            message = getString(R.string.smaller)
        } else {
            message = winHandler(message)
        }
        return message
    }

    fun winHandler(msg: String): String {
        val answer = String.format(getString(R.string.number_is_count), secretNumber.count)
        val btnText = getString(R.string.yes_you_got_it)
        markButtonDisable(ok_button, "$btnText $answer")


        if (secretNumber.count < excellentLimitNumber) {
            val commend = getString(R.string.you_re_so_strong)
            return "$commend!$answer"
        }

        return msg
    }


    fun markButtonDisable(button: Button, text: String = "") {
        if (text !== "") {
            ok_button.setText(text)
        }
        button?.isEnabled = false
    }

    fun markButtonEnabled(button: Button) {
        ok_button.setText(getString(R.string.ok))
        button?.isEnabled = true
    }

}