package br.com.jrmantovani.unsplashaula.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.jrmantovani.unsplashaula.databinding.ItemFotoBinding
import br.com.jrmantovani.unsplashaula.model.RespostaFotosItem
import com.squareup.picasso.Picasso

class FotosAdapter(): RecyclerView.Adapter<FotosAdapter.FotosViewHolder>() {

    var listaFotosItem = mutableListOf<RespostaFotosItem>()

    fun adicionarLista( lista: List<RespostaFotosItem> ){
        this.listaFotosItem.addAll( lista )
        notifyDataSetChanged()
    }

    fun limparLista(){
        this.listaFotosItem.clear()
    }


    inner class FotosViewHolder(itemFotoBinding: ItemFotoBinding): RecyclerView.ViewHolder(itemFotoBinding.root){
        private val binding: ItemFotoBinding

        init {
            binding = itemFotoBinding
        }

        fun bind(fotoItem: RespostaFotosItem){
            Picasso.get()
                .load(fotoItem.urls.regular)
//               .fit()
                .into(binding.imgItemFoto)

                if(fotoItem.alt_description == null){
                    binding.textDescricao.text = "-"
                }else{
                    binding.textDescricao.text = fotoItem.alt_description.toString()
                }
                if (fotoItem.user.name == null ){
                   binding.textUsuario.text = "postado por: -"

                }else{
                    binding.textUsuario.text = "postado por: ${fotoItem.user.name}"
                }




        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemFotoBinding = ItemFotoBinding.inflate(layoutInflater, parent, false)
        return FotosViewHolder(itemFotoBinding)
    }

    override fun onBindViewHolder(holder: FotosViewHolder, position: Int) {
        val foto = listaFotosItem[position]
        holder.bind(foto)
    }

    override fun getItemCount(): Int {
       return listaFotosItem.size
    }


}