package com.example.fefusport

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fefusport.ui.welcomes.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)

        val saveButton: Button = view.findViewById(R.id.saveButton)
        val changePasswordButton: Button = view.findViewById(R.id.changePasswordButton)
        val logoutButton: MaterialButton = view.findViewById(R.id.logoutButton)
        val loginEditText: TextInputEditText = view.findViewById(R.id.loginEditText)
        val nameEditText: TextInputEditText = view.findViewById(R.id.nameEditText)


        saveButton.setOnClickListener {
            val newLogin = loginEditText.text.toString()
            val newName = nameEditText.text.toString()
            Toast.makeText(requireContext(), "Данные профиля сохранены", Toast.LENGTH_SHORT).show()
        }

        changePasswordButton.setOnClickListener {
            requireParentFragment().findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        logoutButton.setOnClickListener {

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
