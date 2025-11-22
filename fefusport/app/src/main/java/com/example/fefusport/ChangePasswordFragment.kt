package com.example.fefusport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        val backButton: ImageButton = view.findViewById(R.id.backButton)
        val oldPasswordEditText: TextInputEditText = view.findViewById(R.id.oldPasswordEditText)
        val newPasswordEditText: TextInputEditText = view.findViewById(R.id.newPasswordEditText)
        val repeatPasswordEditText: TextInputEditText = view.findViewById(R.id.repeatPasswordEditText)
        val applyButton: MaterialButton = view.findViewById(R.id.applyButton)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        applyButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()
            
            Toast.makeText(requireContext(), "Смена пароля пока не реализована", Toast.LENGTH_SHORT).show()
        }

        return view
    }
} 