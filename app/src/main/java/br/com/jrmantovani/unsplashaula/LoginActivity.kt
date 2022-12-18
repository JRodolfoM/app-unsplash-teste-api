package br.com.jrmantovani.unsplashaula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.jrmantovani.unsplashaula.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            logarUsuario()
        }
        binding.tvCadastre.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)

        }
    }


    private fun logarUsuario() {

        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        auth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener { authResult ->

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Erro ao logar", Toast.LENGTH_SHORT).show()
            Log.i("info_unsplash", "erro ao Logar")
        }

    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = auth.currentUser
        if (usuarioAtual != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


}