package br.com.jrmantovani.unsplashaula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jrmantovani.unsplashaula.adapter.FotosAdapter
import br.com.jrmantovani.unsplashaula.api.RetrofitService
import br.com.jrmantovani.unsplashaula.api.UnsplashAPI
import br.com.jrmantovani.unsplashaula.databinding.ActivityMainBinding
import br.com.jrmantovani.unsplashaula.model.RespostaFotos
import br.com.jrmantovani.unsplashaula.model.RespostaPesquisaFotos
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var totalPaginas = 1000
    private var paginaAtual = 1
    private var textoPesquisa = ""
    private var job: Job? = null
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val unsplashAPI by lazy {
        RetrofitService.getAPI(UnsplashAPI::class.java)
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private var adapter: FotosAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapter = FotosAdapter()
        binding.rvImagens.adapter = adapter
        binding.rvImagens.layoutManager = GridLayoutManager(this, 2)

        binding.rvImagens.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                val podeRolarVerticalmente = recyclerView.canScrollVertically(1)
                if (!podeRolarVerticalmente) {

                    if (textoPesquisa == "")
                        recuperarFotosProximaPagina()
                    else
                        recuperarFotosPesquisasProximaPagina()

                }

            }
        })

        binding.btnPesquisar.setOnClickListener {
            val texto = binding.editPesquisa.text.toString()
            if (texto != "") {
                textoPesquisa = texto
                paginaAtual = 1
                adapter?.limparLista()
                recuperarPesquisaImagensAPI(1, texto)

            } else {
                Toast.makeText(this, "Preencha o campo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemSair -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
            }
        }

        return true
    }

    private fun recuperarFotosProximaPagina() {
        if (paginaAtual < 1000) {
            paginaAtual++
            recuperarImagensAPI(paginaAtual)
        }
    }

    private fun recuperarFotosPesquisasProximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++
            recuperarPesquisaImagensAPI(paginaAtual, textoPesquisa)
        }
    }

    private fun recuperarImagensAPI(pagina: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {

            var resposta: Response<RespostaFotos>? = null

            try {
                resposta = unsplashAPI.recuperarImagens(pagina)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {
                    Log.i("info_un", "Sucesso codigo status: ${resposta.code()}")
                    val listaDados = resposta.body()

                    if (listaDados != null) {
                        withContext(Dispatchers.Main) {
                            adapter?.adicionarLista(listaDados)
                        }
                    }


                } else {
                    Log.i("info_un", "Erro codigo status: ${resposta.code()}")
                }
            } else {
                Log.i("info_un", "Resposta nula")
            }


        }

    }

    private fun recuperarPesquisaImagensAPI(pagina: Int = 1, textoPesquisa: String) {
        job = CoroutineScope(Dispatchers.IO).launch {

            var resposta: Response<RespostaPesquisaFotos>? = null

            try {
                resposta = unsplashAPI.recuperarImagensPesquisa(pagina, textoPesquisa)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {
                    Log.i("info_un", "Sucesso codigo status: ${resposta.code()}")
                    totalPaginas = resposta.body()?.total_pages ?: 1000
                    val listaDados = resposta.body()?.results

                    if (listaDados != null) {
                        withContext(Dispatchers.Main) {
                            adapter?.adicionarLista(listaDados)
                        }
                    }


                } else {
                    Log.i("info_un", "Erro codigo status: ${resposta.code()}")
                }
            } else {
                Log.i("info_un", "Resposta nula")
            }


        }

    }

    override fun onStart() {
        super.onStart()
        recuperarImagensAPI(1)

    }

    override fun onStop() {
        super.onStop()

        job?.cancel()

    }

}