package com.ityu.studymvvmandroid.testdata
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ityu.studymvvmandroid.MainActivity
import com.ityu.studymvvmandroid.R


class SingleInstanceActivity : AppCompatActivity() {

    private val TAG = "LaunchModeTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_instance)

        val taskIdTextView: TextView = findViewById(R.id.taskIdTextView)
        val infoTextView: TextView = findViewById(R.id.infoTextView)
        val buttonToMain: Button = findViewById(R.id.buttonToMain)
        val buttonToSelf: Button = findViewById(R.id.buttonToSelf)

        val info = "Activity: SingleInstanceActivity\nTaskId: $taskId\nInstance: ${this.hashCode()}"
        taskIdTextView.text = "Task ID: $taskId"
        infoTextView.text = info
        Log.d(TAG, info)

        buttonToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonToSelf.setOnClickListener {
            // 再次启动自己，测试是否会创建新实例
            startActivity(Intent(this, SingleInstanceActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "SingleInstanceActivity onNewIntent called. Instance: ${this.hashCode()}")
    }
}