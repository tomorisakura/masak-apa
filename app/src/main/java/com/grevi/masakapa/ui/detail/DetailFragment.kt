package com.grevi.masakapa.ui.detail

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.grevi.masakapa.R
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapte
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE
import com.grevi.masakapa.util.HandlerListener
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_card.*

@AndroidEntryPoint
class DetailFragment : Fragment(), HandlerListener {

    private val args : DetailFragmentArgs by navArgs()
    private val recipesViewModel : RecipesViewModel by viewModels()
    private lateinit var ingredientsAdapter : IngredientsAdapter
    private lateinit var stepAdapte: StepAdapte
    private val databaseViewModel : DatabaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseViewModel.handlerListener = this
        prepareViewLayout(false)
        prepareView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val item = menu.findItem(R.id.markToolBar)
        item?.isEnabled = false
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
                        //println(it.ingredients)
                    }
                    prepareViewLayout(true)
                    pgMainRecipes.visibility = View.GONE

                    floatButton.setOnClickListener {
                        results.data?.results?.let { data ->
                            storageHandler(data, args.key, args.thumb)
                        }
                    }
                }
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

    private fun prepareViewLayout(state : Boolean) {
        when(state) {
            false -> {
                floatButton.animate().alpha(0f)
                textChefLayout.animate().alpha(0f)
                cardLayout.animate().alpha(0f)
                cardLayout.visibility = ViewGroup.INVISIBLE
                textIngredientLabel.animate().alpha(0f)
                textStepLabel.animate().alpha(0f)
                recipeTitleText.animate().alpha(0f)
            }

            true -> {
                floatButton.animate().alpha(1f).duration = 2000L
                textChefLayout.animate().alpha(1f).duration = 1000L
                cardLayout.animate().alpha(1f).duration = 1000L
                cardLayout.visibility = ViewGroup.VISIBLE
                textIngredientLabel.animate().alpha(1f)
                textStepLabel.animate().alpha(1f)
                recipeTitleText.animate().alpha(1f)
            }
        }
    }

    private fun snackBar(view: View, msg : String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun storageHandler(detail: Detail, key : String?, thumb : String?) {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                snackBar(mainDetail, "Tidak boleh akses memori 😒")
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE
                )
                Log.v("PERMISSION", "FAIL")
            }
        } else {
            databaseViewModel.keyChecker(detail, key!!, thumb!!)
        }
    }

    override fun message(msg: String, state: Boolean) {
        when(state) {
            true -> snackBar(mainDetail, msg)
            false -> snackBar(mainDetail, msg)
        }
    }
}