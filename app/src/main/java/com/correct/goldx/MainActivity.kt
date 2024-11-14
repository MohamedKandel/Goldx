package com.correct.goldx

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.correct.goldx.databinding.ActivityMainBinding
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.changeTintColor
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.show
import com.correct.goldx.ui.SplashFragment
import com.correct.goldx.ui.auth.AskingFragment
import com.correct.goldx.ui.auth.CompleteProfileFragment
import com.correct.goldx.ui.auth.ForgetPasswordFragment
import com.correct.goldx.ui.auth.LoginFragment
import com.correct.goldx.ui.auth.NewPasswordFragment
import com.correct.goldx.ui.auth.PasswordChangedFragment
import com.correct.goldx.ui.auth.RegisterFragment
import com.correct.goldx.ui.auth.UploadPhotoFragment
import com.correct.goldx.ui.auth.VerificationFragment
import com.correct.goldx.ui.categories.MainCategoriesFragment
import com.correct.goldx.ui.categories.ViewProductFragment
import com.correct.goldx.ui.contact.ContactUsFragment
import com.correct.goldx.ui.ringsizer.HaveRingFragment
import com.correct.goldx.ui.ringsizer.InstructionsFragment
import com.correct.goldx.ui.ringsizer.NotHaveInstructionsFragment
import com.correct.goldx.ui.ringsizer.RingSizerFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var appUpdateType = AppUpdateType.IMMEDIATE
    private lateinit var appUpdateManager: AppUpdateManager
    private val hiddenFragmentArray = listOf(
        SplashFragment::class,
        AskingFragment::class,
        RegisterFragment::class,
        LoginFragment::class,
        CompleteProfileFragment::class,
        ForgetPasswordFragment::class,
        NewPasswordFragment::class,
        PasswordChangedFragment::class,
        VerificationFragment::class,
        ContactUsFragment::class,
        UploadPhotoFragment::class,
        ViewProductFragment::class,
        InstructionsFragment::class,
        HaveRingFragment::class,
        RingSizerFragment::class,
        NotHaveInstructionsFragment::class
    )

    private val ringSizerArray = listOf(
        InstructionsFragment::class,
        HaveRingFragment::class,
        RingSizerFragment::class,
        NotHaveInstructionsFragment::class
    )


    override fun onResume() {
        super.onResume()
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    arl,
                    AppUpdateOptions.newBuilder(appUpdateType).build()
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setupEdgeToEdgeWithCustomization()
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (appUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdateListener)
        }

        checkForUpdate()
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        setContentView(binding.root)

        binding.homeLayout.setOnClickListener {
            selectItem(R.id.home_layout)
        }

        binding.categoryLayout.setOnClickListener {
            selectItem(R.id.category_layout)
        }

        binding.cartLayout.setOnClickListener {
            selectItem(R.id.cart_layout)
        }

        binding.profileLayout.setOnClickListener {
            selectItem(R.id.profile_layout)
        }
    }

    private fun selectItem(itemID: Int) {
        when(itemID) {
            R.id.home_layout -> {
                // control line visibility
                binding.homeLine.show()
                binding.categoryLine.hide()
                binding.cartLine.hide()
                binding.profileLine.hide()

                //change icon colors
                binding.homeIcon.changeTintColor(resources.getColor(R.color.gold,this.theme))
                binding.categoryIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.cartIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.profileIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))

                // change text colors
                binding.txtHome.setTextColor(resources.getColor(R.color.gold,this.theme))
                binding.txtCart.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCategory.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtProfile.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
            }

            R.id.category_layout -> {
                // control line visibility
                binding.homeLine.hide()
                binding.categoryLine.show()
                binding.cartLine.hide()
                binding.profileLine.hide()

                //change icon colors
                binding.homeIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.categoryIcon.changeTintColor(resources.getColor(R.color.gold,this.theme))
                binding.cartIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.profileIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))

                // change text colors
                binding.txtHome.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCategory.setTextColor(resources.getColor(R.color.gold,this.theme))
                binding.txtCart.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtProfile.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
            }

            R.id.cart_layout -> {
                // control line visibility
                binding.homeLine.hide()
                binding.categoryLine.hide()
                binding.cartLine.show()
                binding.profileLine.hide()

                //change icon colors
                binding.homeIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.categoryIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.cartIcon.changeTintColor(resources.getColor(R.color.gold,this.theme))
                binding.profileIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))

                // change text colors
                binding.txtHome.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCategory.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCart.setTextColor(resources.getColor(R.color.gold,this.theme))
                binding.txtProfile.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
            }

            R.id.profile_layout -> {
                // control line visibility
                binding.homeLine.hide()
                binding.categoryLine.hide()
                binding.cartLine.hide()
                binding.profileLine.show()

                //change icon colors
                binding.homeIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.categoryIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.cartIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.profileIcon.changeTintColor(resources.getColor(R.color.gold,this.theme))

                // change text colors
                binding.txtHome.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCategory.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCart.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtProfile.setTextColor(resources.getColor(R.color.gold,this.theme))
            }

            else -> {
                // control line visibility
                binding.homeLine.show()
                binding.categoryLine.hide()
                binding.cartLine.hide()
                binding.profileLine.hide()

                //change icon colors
                binding.homeIcon.changeTintColor(resources.getColor(R.color.gold,this.theme))
                binding.categoryIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.cartIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.profileIcon.changeTintColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))

                // change text colors
                binding.txtHome.setTextColor(resources.getColor(R.color.gold,this.theme))
                binding.txtCart.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtCategory.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
                binding.txtProfile.setTextColor(resources.getColor(R.color.unselected_icon_color_light,this.theme))
            }
        }
    }

    private fun viewInsetsListenerNavBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            // make screen starts at bottom of system bar
            //val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // make screen ends at top of navigation bar
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(navBar.left, navBar.top, navBar.right, navBar.bottom)
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun viewInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            // make screen starts at bottom of system bar
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // make screen ends at top of navigation bar
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(navBar.left, navBar.top, navBar.right, navBar.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (appUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdateListener)
        }
    }

    private val installStateUpdateListener = InstallStateUpdatedListener {
        if (it.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                this, "Restart required to install update", Toast.LENGTH_SHORT
            ).show()
            lifecycleScope.launch {
                delay(5000)
                appUpdateManager.completeUpdate()
            }
        }
    }

    private val arl =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val resultCode = result.resultCode
            when {
                resultCode == Activity.RESULT_OK -> {
                    Log.v("MyActivity", "Update flow completed!")
                }

                resultCode == Activity.RESULT_CANCELED -> {
                    Log.v("MyActivity", "User cancelled Update flow!")
                    // display dialog to request update
                    val dialog = Dialog(applicationContext)
                    dialog.setContentView(R.layout.request_update_dialog)
                    dialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setCancelable(true)
                    dialog.setCanceledOnTouchOutside(false)
                    val btn_ok = dialog.findViewById<TextView>(R.id.btn_ok)
                    val btn_cancel = dialog.findViewById<TextView>(R.id.btn_cancel)

                    btn_ok.setOnClickListener {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                        dialog.dismiss()
                        dialog.cancel()
                    }

                    btn_cancel.setOnClickListener {
                        dialog.dismiss()
                        dialog.cancel()
                    }

                    dialog.show()
                }

                else -> {
                    Log.v("MyActivity", "Update flow failed with resultCode:$resultCode")
                }
            }
        }

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener {
            val isUpdateAllowed = when (appUpdateType) {
                AppUpdateType.IMMEDIATE -> it.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> it.isFlexibleUpdateAllowed
                else -> false
            }
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && isUpdateAllowed
            ) {
                // request update
                /* update with custom dialog
                val dialog = Dialog(applicationContext)
                dialog.setContentView(R.layout.request_update_dialog)
                dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(false)
                val btn_ok = dialog.findViewById<TextView>(R.id.btn_ok)
                val btn_cancel = dialog.findViewById<TextView>(R.id.btn_cancel)

                btn_ok.setOnClickListener {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                    dialog.dismiss()
                    dialog.cancel()
                }

                btn_cancel.setOnClickListener {
                    dialog.dismiss()
                    dialog.cancel()
                }

                dialog.show()*/
                /*update with google flow*/
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    arl,
                    AppUpdateOptions.newBuilder(appUpdateType).build()
                )
            } else {
                // there is no update found
                Log.v("Update mohamed", "No update available ${it.availableVersionCode()}")
            }
        }
    }

    override fun onFragmentChangeListener(fragment: Fragment) {
        if (fragment::class in hiddenFragmentArray) {
            Log.i("Fragment mohamed", "True")
            if (fragment::class in ringSizerArray) {
                viewInsetsListener()
            } else {
                viewInsetsListenerNavBar()
            }
            binding.btmBar.hide()
            binding.marqueeLayout.hide()
        } else {
            Log.i("Fragment mohamed", "False")
            viewInsetsListener()
            binding.btmBar.show()
            binding.marqueeLayout.show()
        }
    }

}