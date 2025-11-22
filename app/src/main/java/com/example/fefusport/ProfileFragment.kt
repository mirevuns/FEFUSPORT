package com.example.fefusport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.model.User
import com.example.fefusport.ui.viewmodel.UserViewModel
import com.example.fefusport.ui.welcomes.MainActivity
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUser: User? = null
    private var avatarUri: String? = null

    private val pickAvatarLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            }
            avatarUri = it.toString()
            avatarImageView?.setImageURI(it)
        }
    }

    private var avatarImageView: ShapeableImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getCurrentUserId()
            ?: return redirectToWelcome()

        avatarImageView = view.findViewById(R.id.avatarImageView)
        val loginEditText: TextInputEditText = view.findViewById(R.id.loginEditText)
        val nameEditText: TextInputEditText = view.findViewById(R.id.nameEditText)
        val loginLayout: TextInputLayout = view.findViewById(R.id.loginInputLayout)
        val nameLayout: TextInputLayout = view.findViewById(R.id.nameInputLayout)
        val saveButton: Button = view.findViewById(R.id.saveButton)
        val changePasswordButton: Button = view.findViewById(R.id.changePasswordButton)
        val logoutButton: MaterialButton = view.findViewById(R.id.logoutButton)
        val changeAvatarButton: MaterialButton = view.findViewById(R.id.changeAvatarButton)

        // Устанавливаем белый цвет текста для кнопок
        changeAvatarButton.setTextColor(android.graphics.Color.WHITE)
        changePasswordButton.setTextColor(android.graphics.Color.WHITE)
        saveButton.setTextColor(android.graphics.Color.WHITE)
        
        // Устанавливаем ярко-красный фон для кнопки "Выйти"
        logoutButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.bright_red)
        logoutButton.setTextColor(android.graphics.Color.WHITE)

        changeAvatarButton.setOnClickListener {
            pickAvatarLauncher.launch("image/*")
        }

        userViewModel.observeUser(userId).observe(viewLifecycleOwner) { user ->
            currentUser = user
            if (user != null) {
                loginEditText.setText(user.login)
                nameEditText.setText(user.nickname)
                avatarUri = user.avatarUri
                if (user.avatarUri.isNullOrEmpty()) {
                    avatarImageView?.setImageResource(R.drawable.person)
                } else {
                    avatarImageView?.setImageURI(Uri.parse(user.avatarUri))
                }
            }
        }

        saveButton.setOnClickListener {
            loginLayout.error = null
            nameLayout.error = null

            val newLogin = loginEditText.text?.toString()?.trim().orEmpty()
            val newName = nameEditText.text?.toString()?.trim().orEmpty()

            if (newLogin.isEmpty()) {
                loginLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }
            if (newName.isEmpty()) {
                nameLayout.error = getString(R.string.field_required)
                return@setOnClickListener
            }

            val updatedUser = currentUser?.copy(
                login = newLogin,
                nickname = newName,
                avatarUri = avatarUri
            ) ?: return@setOnClickListener

            userViewModel.updateUser(updatedUser, currentUser) { success ->
                val message = if (success) {
                    getString(R.string.profile_updated)
                } else {
                    "Не удалось сохранить изменения"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        changePasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        logoutButton.setOnClickListener {
            sessionManager.clearSession()
            redirectToWelcome()
        }
    }

    private fun redirectToWelcome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
