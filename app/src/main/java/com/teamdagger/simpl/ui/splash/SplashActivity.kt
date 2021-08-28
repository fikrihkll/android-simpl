package com.teamdagger.simpl.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.ui.getting_started.GettingStartedActivity
import com.teamdagger.simpl.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() , CoroutineScope{

    private lateinit var job: Job

    override val coroutineContext : CoroutineContext
    get() = Dispatchers.Main + job


    //Dao for checking data empty
    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        job = Job()

        launch {
            val userCache = userDao.getUser(1)
            delay(3000)
            if(userCache == null)
                launchActivity(false)
            else
                launchActivity(true)

        }
    }

    private fun launchActivity(doesExist:Boolean){
        if(doesExist){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }else{
            var intent = Intent(this,GettingStartedActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}