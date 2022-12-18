package br.com.jrmantovani.unsplashaula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.jrmantovani.unsplashaula.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnCadastrar.setOnClickListener {
            cadastrarUsuario()
        }
    }


    private fun cadastrarUsuario() {

        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()
        auth.createUserWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener { authRresul ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }.addOnFailureListener { authRresul ->
            Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show()
        }
    }
}