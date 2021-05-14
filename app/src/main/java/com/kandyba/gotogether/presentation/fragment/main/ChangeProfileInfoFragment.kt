package com.kandyba.gotogether.presentation.fragment.main

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.*
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*


/**
 * Фрагмент изменения профиля пользователя
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
    private lateinit var cancelAlertButton: Button
    private lateinit var confirmAlertButton: Button
    private lateinit var exitAlertDialog: AlertDialog
    private lateinit var changeFieldAlertDialog: AlertDialog
    private lateinit var changedField: TextView
    private lateinit var newValue: EditText
    private lateinit var saveButton: Button

    private lateinit var settings: SharedPreferences
    private lateinit var mainViewModel: MainViewModel
    private var viewModel: ProfileViewModel? = null

    private var expanded = false
    private var dateAndTime: Calendar = Calendar.getInstance()

    private var dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        viewModel?.loadUserInfo(
            settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
            settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
            false,
            false
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_AVATAR) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                profileAvatar.setImageURI(selectedImage)
                viewModel?.uploadUserAvatar(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                    createImageFilePart(selectedImage)
                )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createImageFilePart(imageUri: Uri): MultipartBody.Part {
        val file = File(imageUri.path ?: EMPTY_STRING)
        val inputStream: InputStream? =
            requireActivity().contentResolver.openInputStream(imageUri)
        val original = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        val outputStream = ByteArrayOutputStream()
        val quality = calculateCompressQuality((original.width * original.height).toLong())
        original?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val fileBody: RequestBody =
            RequestBody.create(
                MEDIA_TYPE.toMediaTypeOrNull(),
                outputStream.toByteArray()
            )
        return MultipartBody.Part.createFormData(
            MULTIPART_NAME,
            file.name,
            fileBody
        )
    }

    private fun calculateCompressQuality(originalPixelCount: Long): Int {
        return if (SENDED_IMAGE_PIXELS_COUNT >= originalPixelCount) {
            ORIGINAL_IMAGE_QUALITY
        } else {
            ((SENDED_IMAGE_PIXELS_COUNT.toDouble() / originalPixelCount.toDouble()) * 100).toInt()
        }
    }

    private fun checkPermissions(): Boolean {
        val listPermissionsNeeded = mutableListOf<String>()
        for (p in permissions) {
            val result = ContextCompat.checkSelfPermission(requireActivity(), p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                READ_STORAGE_PERMISSION
            )
            return false
        }
        return true
    }

    fun startImagePickActivity() {
        val intent = Intent()
        intent.type = IMAGE_TYPE
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(intent, CHOOSER_TITLE),
            1
        )
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
        val changeBuilder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setView(changeFieldAlert)
        changeFieldAlertDialog = changeBuilder.create()
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
            newValue.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) setDate() }
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
            if (checkPermissions()) startImagePickActivity()
        }
        saveButton.setOnClickListener {
            if (newValue.text.toString() != EMPTY_STRING) {
                changeUserInfo(changedField.text.toString())
                changeFieldAlertDialog.dismiss()
            }
        }
        logoutButton.setOnClickListener {
            cancelAlertButton.setOnClickListener { exitAlertDialog.dismiss() }
            confirmAlertButton.setOnClickListener {
                viewModel?.logout(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
                exitAlertDialog.dismiss()
                exitAlertDialog.onDetachedFromWindow()
            }
            exitAlertDialog.show()
        }
    }

    private fun changeUserInfo(changedFieldText: String) {
        val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        when (changedFieldText) {
            resources.getString(R.string.name) -> {
                val request = UserMainRequestBody(newValue.text.toString(), null, null)
                viewModel?.updateMainUserInfo(token, request)
            }
            resources.getString(R.string.birthday) -> {
                val request = UserMainRequestBody(
                    null,
                    dateAndTime.timeInMillis,
                    null
                )
                viewModel?.updateMainUserInfo(token, request)
            }
            resources.getString(R.string.about_me) -> {
                val request = UserInfoRequestBody(newValue.text.toString())
                viewModel?.updateAdditionalUserInfo(token, request)
            }
        }
    }

    private fun saveUserInfoToCache(userInfo: UserInfoModel) {
        Cache.instance.setUserInfo(userInfo)
    }

    private fun initObservers() {
        viewModel?.userInfo?.observe(requireActivity(), Observer { userInfo ->
            setViews(userInfo)
        })
        viewModel?.updateAdditionalUserInfo?.observe(requireActivity(), Observer {
            saveUserInfoToCache(it)
            setViews(it)
        })
        viewModel?.updateMainUserInfo?.observe(requireActivity(), Observer {
            saveUserInfoToCache(it)
            setViews(it)
        })
        viewModel?.userImageUploaded?.observe(requireActivity(), Observer {
            viewModel?.loadUserInfo(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
                updateCache = true,
                anotherUser = false
            )
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.userInfo?.removeObservers(requireActivity())
        viewModel?.updateAdditionalUserInfo?.removeObservers(requireActivity())
        viewModel?.updateMainUserInfo?.removeObservers(requireActivity())
        viewModel?.userImageUploaded?.removeObservers(requireActivity())
    }

    private fun setViews(userInfo: UserInfoModel) {
        userName.text = userInfo.firstName
        description.text =
            if (userInfo.info != EMPTY_STRING) userInfo.info else resources.getString(R.string.about_me_placeholder)
        val placeholder =
            if (userInfo.sex == MAN_SEX) R.drawable.man_stub else R.drawable.woman_stub
        Picasso.get()
            .load(userInfo.avatar)
            .placeholder(placeholder)
            .error(placeholder)
            .into(profileAvatar)
        userAge.text = userInfo.birthDate?.toLong()?.let { getAge(it) }
        if (description.lineCount < FIRST_VISIBLE_LINES_NUMBER) {
            showAll.visibility = View.GONE
        }
    }

    private fun setDate() {
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    companion object {
        private const val FIRST_VISIBLE_LINES_NUMBER = 5
        private const val PICK_IMAGE_AVATAR = 1

        /** Дефолтное разрешение изображения 1024 x 768 */
        private const val SENDED_IMAGE_PIXELS_COUNT = 786342
        private const val ORIGINAL_IMAGE_QUALITY = 100
        private const val IMAGE_TYPE = "image/*"
        private const val CHOOSER_TITLE = "Выберите изображение"
        private const val MAN_SEX = "0"
        private const val MULTIPART_NAME = "avatar"
        private const val MEDIA_TYPE = "multipart/form-data"
        private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        fun newInstance(): ChangeProfileInfoFragment {
            return ChangeProfileInfoFragment()
        }
    }
}