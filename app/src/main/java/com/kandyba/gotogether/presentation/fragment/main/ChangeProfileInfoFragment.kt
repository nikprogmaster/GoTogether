package com.kandyba.gotogether.presentation.fragment.main

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.FileUtils
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import com.kandyba.gotogether.models.presentation.getAge
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*


/**
 * @author Кандыба Никита
 * @since 18.04.2021
 */
class ChangeProfileInfoFragment : Fragment() {

    private lateinit var profileAvatar: CircleImageView
    private lateinit var userName: TextView
    private lateinit var userAge: TextView
    private lateinit var description: TextView
    private lateinit var logoutButton: Button
    private lateinit var profileLayout: ConstraintLayout
    private lateinit var changeFio: TextView
    private lateinit var changeAge: TextView
    private lateinit var changeAboutMeInfo: TextView
    private lateinit var showAll: TextView
    private lateinit var profileBackButton: ImageView

    private lateinit var viewModel: ProfileViewModel

    private lateinit var cancelAlertButton: Button
    private lateinit var confirmAlertButton: Button
    private lateinit var exitAlertDialog: AlertDialog

    private lateinit var changeFieldAlertDialog: AlertDialog
    private lateinit var changedField: TextView
    private lateinit var newValue: EditText
    private lateinit var saveButton: Button

    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    private var expanded = false
    var dateAndTime: Calendar = Calendar.getInstance()

    private fun setDate(v: View?) {
        DatePickerDialog(
            requireContext(),
            d,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    // установка обработчика выбора даты
    var d =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime.set(Calendar.YEAR, year)
            dateAndTime.set(Calendar.MONTH, monthOfYear)
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val result = "${dayOfMonth}.${monthOfYear + 1}.${year}"
            newValue.setText(result)

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.change_user_profile, container, false)
        initViews(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
        initObservers()
        viewModel.loadUserInfo(
            settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
            settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
            false,
            false
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_AVATAR) {
            val selectedImage: Uri? = data?.data
            profileAvatar.setImageURI(selectedImage)

            val file = FileUtils.getFile(requireContext(), selectedImage)
            val fileBody: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "avatar",
                file.name,
                fileBody
            )
            viewModel.uploadUserAvatar(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                filePart
            )

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun littleEndianConversion(bytes: ByteArray): Int {
        var result = 0
        for (i in bytes.indices) {
            result = result or (bytes[i].toInt() shl 8 * i)
        }
        return result
    }

    private fun initViews(root: View) {
        profileAvatar = root.findViewById(R.id.profile_avatar)
        userName = root.findViewById(R.id.user_name)
        userAge = root.findViewById(R.id.user_age_value)
        description = root.findViewById(R.id.user_description)
        logoutButton = root.findViewById(R.id.logout)
        changeFio = root.findViewById(R.id.change_fio_button)
        changeAge = root.findViewById(R.id.change_age_button)
        changeAboutMeInfo = root.findViewById(R.id.change_about_me)
        showAll = root.findViewById(R.id.show_all)
        profileBackButton = root.findViewById(R.id.profile_back_btn)

        //init alerts
        profileLayout = root.findViewById(R.id.change_user_profile_layout)
        val alert = layoutInflater.inflate(R.layout.exit_dialog_layout, profileLayout, false)
        cancelAlertButton = alert.findViewById(R.id.cancel_btn)
        confirmAlertButton = alert.findViewById(R.id.logout_btn)
        val builder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setView(alert)
        exitAlertDialog = builder.create()

        val changeFieldAlert =
            layoutInflater.inflate(R.layout.change_user_info_alert, profileLayout, false)
        changedField = changeFieldAlert.findViewById(R.id.changed_field)
        newValue = changeFieldAlert.findViewById(R.id.new_value)
        saveButton = changeFieldAlert.findViewById(R.id.save_button)
        val changeBuider = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setView(changeFieldAlert)
        changeFieldAlertDialog = changeBuider.create()
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        val factory = component.getProfileViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)
            .get(ProfileViewModel::class.java)
        val mainFactory = component.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)
            .get(MainViewModel::class.java)
        mainViewModel.showToolbar(false)
        settings = component.getSharedPreferences()
    }

    private fun initListeners() {
        showAll.setOnClickListener {
            if (expanded) {
                showAll.text = resources.getText(R.string.show_all)
                description.maxLines = 5
            } else {
                description.maxLines = 10000
                showAll.text = resources.getText(R.string.roll_up)
            }
            expanded = !expanded
        }
        changeFio.setOnClickListener {
            newValue.setText(EMPTY_STRING)
            changedField.setText(R.string.name)
            changeFieldAlertDialog.show()
        }
        changeAge.setOnClickListener {
            newValue.setText(EMPTY_STRING)
            changedField.setText(R.string.birthday)
            newValue.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    setDate(newValue)
                }
            }
            changeFieldAlertDialog.show()
        }
        changeAboutMeInfo.setOnClickListener {
            newValue.setText(EMPTY_STRING)
            changedField.setText(R.string.about_me)
            changeFieldAlertDialog.show()
        }
        profileBackButton.setOnClickListener {
            mainViewModel.closeFragment()
        }
        profileAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_AVATAR
            )
        }
        saveButton.setOnClickListener {
            if (newValue.text.toString() != EMPTY_STRING) {
                val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
                when (changedField.text.toString()) {
                    resources.getString(R.string.name) -> {
                        val request = UserMainRequestBody(newValue.text.toString(), null, null)
                        viewModel.updateMainUserInfo(token, request)
                    }
                    resources.getString(R.string.birthday) -> {
                        val request = UserMainRequestBody(
                            null,
                            dateAndTime.timeInMillis,
                            null
                        )
                        viewModel.updateMainUserInfo(token, request)
                    }
                    resources.getString(R.string.about_me) -> {
                        val request = UserInfoRequestBody(newValue.text.toString())
                        viewModel.updateAdditionalUserInfo(token, request)
                    }
                }
                changeFieldAlertDialog.dismiss()
            }
        }
        logoutButton.setOnClickListener {
            cancelAlertButton.setOnClickListener { exitAlertDialog.dismiss() }
            confirmAlertButton.setOnClickListener {
                viewModel.logout(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
                exitAlertDialog.dismiss()
                exitAlertDialog.onDetachedFromWindow()
            }
            exitAlertDialog.show()
        }
    }

    private fun saveUserInfoToCache(userInfo: UserInfoModel) {
        Cache.instance.setUserInfo(userInfo)
    }

    private fun initObservers() {
        viewModel.userInfo.observe(requireActivity(), Observer { userInfo ->
            setViews(userInfo)
        })
        viewModel.updateAdditionalUserInfo.observe(requireActivity(), Observer {
            saveUserInfoToCache(it)
            setViews(it)
        })
        viewModel.updateMainUserInfo.observe(requireActivity(), Observer {
            saveUserInfoToCache(it)
            setViews(it)
        })
        viewModel.userImageUploaded.observe(requireActivity(), Observer {
            Log.i("Всё хорошо", "dfgfd")
        })
    }

    private fun setViews(userInfo: UserInfoModel) {
        userName.text = userInfo.firstName
        description.text =
            if (userInfo.info != EMPTY_STRING) userInfo.info else ABOUT_ME_PLACEHOLDER
        Picasso.get()
            .load(userInfo.avatar)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .into(profileAvatar)
        userAge.text = userInfo.birthDate?.toLong()?.let { getAge(it) }
        if (description.lineCount < FIRST_VISIBLE_LINES_NUMBER) {
            showAll.visibility = View.GONE
        }
    }

    companion object {
        private const val FIRST_VISIBLE_LINES_NUMBER = 5
        private const val MILLISECOND_DIVISOR = 1000000
        private const val ABOUT_ME_PLACEHOLDER = "Вы пока ничего не рассказали другим о себе"
        private const val PICK_IMAGE_AVATAR = 1

        fun newInstance(): ChangeProfileInfoFragment {
            val args = Bundle()

            val fragment = ChangeProfileInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}