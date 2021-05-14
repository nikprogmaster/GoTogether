package com.kandyba.gotogether.presentation.fragment.prelogin

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel
import java.util.*

/**
 * Фрагмент с вводом информации о вновь зарегистрированном пользователе
 */
class AboutMeFragment : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var backButton: ImageView
    private lateinit var name: EditText
    private lateinit var sex: Spinner
    private lateinit var birthday: EditText
    private lateinit var policy: TextView

    private lateinit var viewModel: StartViewModel
    private lateinit var settings: SharedPreferences

    private var dateAndTime: Calendar = Calendar.getInstance()
    private var dateSetListener =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            dateAndTime.set(Calendar.YEAR, year)
            dateAndTime.set(Calendar.MONTH, monthOfYear)
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val result = "${dayOfMonth}.${monthOfYear + 1}.${year}"
            birthday.setText(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.about_me_fragment, container, false)
        backButton = root.findViewById(R.id.back_btn)
        continueButton = root.findViewById(R.id.continue_btn)
        name = root.findViewById(R.id.name_et)
        sex = root.findViewById(R.id.sex_et)
        birthday = root.findViewById(R.id.birthday_et)
        policy = root.findViewById(R.id.privacy_policy)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
        initObservers()
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        viewModel = ViewModelProvider(
            requireActivity(),
            component.getStartViewModelFactory()
        )[StartViewModel::class.java]
        settings = component.getSharedPreferences()
    }

    private fun initObservers() {
        viewModel.updateMainUserInfo.observe(requireActivity(), Observer {
            (requireActivity() as FragmentManager).openFragment(DescriptionFragment.newInstance())
        })
    }

    private fun initListeners() {
        continueButton.setOnClickListener {
            val request = createUserUpdateRequest()
            val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
            viewModel.updateMainUserInfo(token, request)
        }
        backButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
        birthday.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) setDate() }
        policy.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createUserUpdateRequest(): UserMainRequestBody {
        val name = if (name.text.toString() != EMPTY_STRING) {
            name.text.toString()
        } else null
        val birthDate = if (birthday.text.toString() != EMPTY_STRING) {
            dateAndTime.timeInMillis
        } else null
        val sex =
            if (sex.selectedItem.toString() == resources.getStringArray(R.array.sex)[0].toString()) MAN_SEX else WOMAN_SEX
        return UserMainRequestBody(name, birthDate, sex)
    }

    // отображаем диалоговое окно для выбора даты
    private fun setDate() {
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDestroyView() {
        viewModel.updateMainUserInfo.removeObservers(requireActivity())
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): AboutMeFragment {
            val args = Bundle()

            val fragment = AboutMeFragment()
            fragment.arguments = args
            return fragment
        }

        private const val MAN_SEX = 0
        private const val WOMAN_SEX = 1
    }
}