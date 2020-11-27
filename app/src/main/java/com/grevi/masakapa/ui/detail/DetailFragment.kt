package com.grevi.masakapa.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.R
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapte
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_card.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args : DetailFragmentArgs by navArgs()
    private val recipesViewModel : RecipesViewModel by viewModels()
    private lateinit var ingredientsAdapter : IngredientsAdapter
    private lateinit var stepAdapte: StepAdapte

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViewLayout(false)
        prepareView(view)
    }

    private fun prepareView(view: View) {
        //Log.v("ARGS", args.key)
        recipesViewModel.getDetail(args.key!!).observe(viewLifecycleOwner, Observer {results ->
            when(results.status) {
                Resource.Status.ERROR -> toast(view.context, results.msg.toString())
                Resource.Status.LOADING -> prepareViewLayout(false)
                Resource.Status.SUCCESS -> {
                    Glide.with(view).load(args.thumb).placeholder(R.drawable.placeholder).into(imgDetail)
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
                    prepareViewLayout(true)
                }
            }
        })

        floatButton.setOnClickListener {
            Snackbar.make(view, "clicked", Snackbar.LENGTH_SHORT).show()
        }
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

    private fun prepareViewLayout(state : Boolean) {
        when(state) {
            false -> {
                floatButton.animate().alpha(0f)
                textChefLayout.animate().alpha(0f)
                cardLayout.animate().alpha(0f)
                textIngredientLabel.animate().alpha(0f)
                textStepLabel.animate().alpha(0f)
                recipeTitleText.animate().alpha(0f)
            }

            true -> {
                floatButton.animate().alpha(1f).duration = 2000L
                textChefLayout.animate().alpha(1f).duration = 1000L
                cardLayout.animate().alpha(1f).duration = 1000L
                textIngredientLabel.animate().alpha(1f)
                textStepLabel.animate().alpha(1f)
                recipeTitleText.animate().alpha(1f)
            }
        }
    }
}