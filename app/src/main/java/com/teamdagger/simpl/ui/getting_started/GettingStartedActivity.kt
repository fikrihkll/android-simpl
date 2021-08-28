package com.teamdagger.simpl.ui.getting_started

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.UserModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.data.room.bank.BankCacheMapper
import com.teamdagger.simpl.data.room.dao.BalanceDao
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.data.room.user.UserCacheMapper
import com.teamdagger.simpl.data.room.user.UserCacherEntity
import com.teamdagger.simpl.data.room.wallet.WalletCacheMapper
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheEntity
import com.teamdagger.simpl.databinding.ActivityGettingStartedBinding
import com.teamdagger.simpl.ui.getting_started.fragments.*
import com.teamdagger.simpl.ui.home.MainActivity
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class GettingStartedActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityGettingStartedBinding

    //User Data Variables
    private var userData: UserModel? = null
    private var bankData: MutableList<BankModel>? = mutableListOf()
    private var walletData: MutableList<WalletModel>? = mutableListOf()

    //viewpager adapter
    private lateinit var adapter: SliderPagerAdapter

    //Coroutines
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //Fragments for ViewPager
    private lateinit var profileFragment: ProfileFragment
    private lateinit var bankFragment: BankFragment
    private lateinit var walletFragment: WalletFragment

    //DAO
    @Inject
    lateinit var balanceDao: BalanceDao

    @Inject
    lateinit var userDao: UserDao

    //Mapper
    @Inject
    lateinit var walletCacheMapper: WalletCacheMapper

    @Inject
    lateinit var bankCacheMapper: BankCacheMapper

    @Inject
    lateinit var userCacheMapper: UserCacheMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGettingStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()
        prepareFragment()
        retrieveDataFromFragment()

        binding.tvStepStart.text = "Step 1/3"

        prepareViewPager()

        binding.btnBackStart.setOnClickListener {
            binding.vpGettingStarted.setCurrentItem(binding.vpGettingStarted.currentItem - 1, true)
        }

    }

    private fun prepareViewPager() {
        adapter = SliderPagerAdapter(this, listOf(profileFragment, bankFragment, walletFragment))

        binding.vpGettingStarted.adapter = adapter
        binding.vpGettingStarted.setPageTransformer(DepthPageTransformer())
        binding.vpGettingStarted.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvStepStart.text = "Step ${position + 1}/3"
                if (position == 0)
                    binding.btnBackStart.visibility = View.INVISIBLE
                else
                    binding.btnBackStart.visibility = View.VISIBLE
            }
        })

        //Move it to the second page to make it not lag, when the user first time to move it
        binding.vpGettingStarted.setCurrentItem(1, true)
        binding.vpGettingStarted.setCurrentItem(0, true)
    }

    private fun retrieveDataFromFragment() {
        profileFragment.sendData {
            userData = it
        }

        bankFragment.sendData {
            bankData?.clear()
            bankData?.addAll(it)
        }

        walletFragment.sendData {
            walletData?.clear()
            walletData?.addAll(it)
        }
    }

    //Prepare fragments for ViewPager
    private fun prepareFragment() {
        binding.vpGettingStarted.isUserInputEnabled = false

        profileFragment = ProfileFragment()
        bankFragment = BankFragment()
        walletFragment = WalletFragment()

        profileFragment.changePage {
            binding.vpGettingStarted.setCurrentItem(binding.vpGettingStarted.currentItem + 1, true)
        }
        bankFragment.changePage {
            binding.vpGettingStarted.setCurrentItem(binding.vpGettingStarted.currentItem + 1, true)
        }
        walletFragment.changePage {
            launch {
                insertData()

                var intent = Intent(this@GettingStartedActivity, MainActivity::class.java)
                startActivity(intent)
                this@GettingStartedActivity.finish()
            }

        }
    }

    suspend fun insertData() {
        Log.w("SYKL", "${userData} - ${bankData} - ${walletData}")
        var userCache: UserCacherEntity? = null
        if (userData != null) {
            userDao.addNewUser(userCacheMapper.modelToEntity(userData!!))
            userCache = userDao.getUser(1)
        }

        if (bankData != null) {
            for(i in bankData!!){
                var rawBank = i
                rawBank.userId = userCache!!.id
                balanceDao.addBank(bankCacheMapper.modelToEntity(rawBank))
            }
        }
        if (walletData != null) {
            for(i in walletData!!){
                var rawWallet = i
                rawWallet.userId = userCache!!.id
                balanceDao.addWallet(walletCacheMapper.modelToEntity(rawWallet))
            }

        }

        //Add Emergency Safe
        var ret = balanceDao.addSafe(
            WantToBuyCacheEntity(
                0,
                "EMERGENCY",
                "Your emergency safe",
                0,
                9999999999999999,
                "",
                UtilFun.timestamp(),
                null,
                userCache!!.id,
                false
            )
        )
        Log.w("SYKL","RET-${ret}")


    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}