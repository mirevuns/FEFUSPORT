package com.example.fefusport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.model.User
import com.example.fefusport.ui.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ChangePasswordFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getCurrentUserId() ?: return
        val backButton: ImageButton = view.findViewById(R.id.backButton)
        val oldPasswordInput: TextInputEditText = view.findViewById(R.id.oldPasswordEditText)
        val newPasswordInput: TextInputEditText = view.findViewById(R.id.newPasswordEditText)
        val repeatPasswordInput: TextInputEditText = view.findViewById(R.id.repeatPasswordEditText)
        val oldLayout: TextInputLayout = view.findViewById(R.id.oldPasswordInputLayout)
        val newLayout: TextInputLayout = view.findViewById(R.id.newPasswordInputLayout)
        val repeatLayout: TextInputLayout = view.findViewById(R.id.repeatPasswordInputLayout)
        val applyButton: MaterialButton = view.findViewById(R.id.applyButton)

        backButton.setOnClickListener { parentFragmentManager.popBackStack() }

        userViewModel.observeUser(userId).observe(viewLifecycleOwner) { user ->
            currentUser = user
        }

        applyButton.setOnClickListener {
            oldLayout.error = null
            newLayout.error = null
            repeatLayout.error = null

            val oldPassword = oldPasswordInput.text?.toString().orEmpty()
            val newPassword = newPasswordInput.text?.toString().orEmpty()
            val repeatPassword = repeatPasswordInput.text?.toString().orEmpty()

            if (newPassword.isEmpty()) {
                newLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (newPassword != repeatPassword) {
                repeatLayout.error = getString(R.string.passwords_not_match)
                return@setOnClickListener
            }
            val user = currentUser ?: return@setOnClickListener
            if (user.password != oldPassword) {
                oldLayout.error = "Неверный текущий пароль"
            } else {
                val updated = user.copy(password = newPassword)
                userViewModel.updateUser(updated) { success ->
                    val message = if (success) "Пароль обновлён" else "Ошибка сохранения"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if (success) parentFragmentManager.popBackStack()
                }
            }
        }
    }
}