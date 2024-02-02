package com.gracodev.rickmortydemo.presentation.adapters

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracodev.rickmortydemo.data.model.CharacterData
import com.gracodev.rickmortydemo.databinding.CharacterItemLayoutBinding
import com.squareup.picasso.Picasso

class CharacterAdapter(
    private val characterList: MutableList<CharacterData> = mutableListOf(),
    private val onItemClick: (CharacterData) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    fun submitAll(newCharacterList: MutableList<CharacterData>) {
        characterList.clear()
        characterList.addAll(newCharacterList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CharacterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = characterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    inner class ViewHolder(val binding: CharacterItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLayoutVisible = false

        fun bind(character: CharacterData) {
            binding.apply {
                Picasso.get().load(character.image).into(imageCharacter)
                tvCharacterName.text = character.name


                property1.propertyTitle.text = "Status"
                property1.propertySubtitle.text = character.status

                property2.propertyTitle.text = "Species"
                property2.propertySubtitle.text = character.species

                property3.propertyTitle.text = "Type"
                property3.propertySubtitle.text = character.type

                property4.propertyTitle.text = "Gender"
                property4.propertySubtitle.text = character.gender

                property5.propertyTitle.text = "Origin"
                property5.propertySubtitle.text = character.origin.name

                property6.propertyTitle.text = "Location"
                property6.propertySubtitle.text = character.location.name

                btnViewDetail.setOnClickListener {
                    TransitionManager.beginDelayedTransition(fullContent)
                    isLayoutVisible = !isLayoutVisible
                    fullContent.visibility = if (isLayoutVisible) View.VISIBLE else View.GONE

                    btnViewDetail.text = if (isLayoutVisible) "Ocultar Detalle" else  "Ver Detalle"
                }
            }
        }
    }
}