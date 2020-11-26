package com.grevi.masakapa.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapte
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_card.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args : DetailFragmentArgs by navArgs()
    private val recipesViewModel : RecipesViewModel by viewModels()
    private lateinit var ingredientsAdapter : IngredientsAdapter
    private lateinit var stepAdapte: StepAdapte

    private var expanded : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        //Log.v("ARGS", args.key

        Glide.with(this).load(args.thumb).placeholder(R.drawable.placeholder).into(imgDetail)
        recipesViewModel.getDetail(args.key).observe(viewLifecycleOwner, Observer {results ->
            results.data?.results?.let {
                recipeTitleText.text = it.name
                textDiffItem.text = it.dificulty
                textPortionItems.text = it.servings
                textTimesItem.text = it.times
                chefText.text = it.author.author
                publishedText.text = it.author.published
                //descText.text = it.desc
                prepareRV(it.ingredients, it.step)
                println(it.ingredients)
            }
        })
    }

    private fun prepareRV(stringList: MutableList<String>, stepList : MutableList<String>) {
        ingredientsAdapter = IngredientsAdapter()
        rv_ingredients.layoutManager = GridLayoutManager(this.context, 2)
        rv_ingredients.adapter = ingredientsAdapter
        ingredientsAdapter.addList(stringList)

        stepAdapte = StepAdapte()
        rv_step.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_step.adapter = stepAdapte
        stepAdapte.addList(stepList)
    }
}