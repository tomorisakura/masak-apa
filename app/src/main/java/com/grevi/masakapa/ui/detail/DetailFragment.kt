package com.grevi.masakapa.ui.detail

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.grevi.masakapa.R
import com.grevi.masakapa.databinding.FragmentDetailBinding
import com.grevi.masakapa.databinding.ItemCardBinding
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapter
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.*
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    private lateinit var itemCardBinding: ItemCardBinding
    private val args : DetailFragmentArgs by navArgs()
    private val recipesViewModel : RecipesViewModel by viewModels()
    private val ingredientsAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }
    private val stepAdapter: StepAdapter by lazy { StepAdapter() }
    private val databaseViewModel : DatabaseViewModel by viewModels()
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(requireContext()) }

    private val TAG = DetailFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater)
        itemCardBinding = ItemCardBinding.bind(binding.cardLayout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeNetwork()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.bucket)?.isVisible = false
    }

    private fun prepareView() = with(binding) {
        recipesViewModel.getDetail(args.key).observe(viewLifecycleOwner, {results ->
            when(results) {
                is State.Loading -> Log.i(TAG, results.msg)
                is State.Error -> toast(requireContext(), results.msg)
                is State.Success -> {
                    imgDetail.load(args.thumb) {
                        allowHardware(false)
                        crossfade(true)
                        placeholder(R.drawable.placeholder)
                    }
                    results.data.results.let {
                        recipeTitleText.text = it.name
                        itemCardBinding.textDiffItem.text = it.dificulty
                        itemCardBinding.textPortionItems.text = it.servings
                        itemCardBinding.textTimesItem.text = it.times
                        chefText.text = it.author.author
                        publishedText.text = it.author.published
                        prepareRV(it.ingredients, it.step)
                    }
                    prepareViewLayout(true)
                    pgMainRecipes.visibility = View.GONE

                    floatButton.setOnClickListener {
                        results.data.results.let { data ->
                            if (getSharedPermission) {
                                observeChecker(data)
                            }else {
                                snackBar(root, getString(R.string.bucket_permission_text)).show()
                            }
                        }
                    }
                }
                else -> Log.i(TAG, "")
            }
        })
    }

    private fun prepareRV(stringList: MutableList<String>, stepList : MutableList<String>) = with(binding) {
        rvIngredients.layoutManager = GridLayoutManager(requireContext(), 2)
        rvIngredients.adapter = ingredientsAdapter
        ingredientsAdapter.addList(stringList)

        rvStep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvStep.adapter = stepAdapter
        stepAdapter.addList(stepList)
    }

    private fun prepareViewLayout(state : Boolean) = with(binding) {
        when(state) {
            false -> {
                cardLayout.animate().alpha(0f)
                cardLayout.visibility = ViewGroup.INVISIBLE
                floatButton.animate().alpha(0f)
                floatButton.visibility = View.INVISIBLE
                textChefLayout.animate().alpha(0f)
                textChefLayout.visibility = View.INVISIBLE
                textIngredientLabel.animate().alpha(0f)
                textIngredientLabel.visibility = View.INVISIBLE
                textStepLabel.animate().alpha(0f)
                textStepLabel.visibility = View.INVISIBLE
                recipeTitleText.animate().alpha(0f)
                recipeTitleText.visibility = View.INVISIBLE
            }

            true -> {
                cardLayout.visibility = ViewGroup.VISIBLE
                cardLayout.animate().alpha(1f).duration = 1000L
                floatButton.visibility = View.VISIBLE
                floatButton.animate().alpha(1f).duration = 2000L
                textChefLayout.visibility = View.VISIBLE
                textChefLayout.animate().alpha(1f).duration = 1000L
                textIngredientLabel.visibility = View.VISIBLE
                textIngredientLabel.animate().alpha(1f)
                textStepLabel.visibility = View.VISIBLE
                textStepLabel.animate().alpha(1f)
                recipeTitleText.visibility = View.VISIBLE
                recipeTitleText.animate().alpha(1f)
            }
        }
    }

    private fun observeChecker(detail : Detail) = with(binding) {
        databaseViewModel.keyChecker(args.key).observe(viewLifecycleOwner) { isExist ->
            if (!isExist) {
                databaseViewModel.insertRecipes(detail, args.key, args.thumb)
                snackBar(root, "${detail.name} ditambahkan di bucket !").show()
            } else {
                snackBar(root, "${detail.name} sudah ada di bucket !").show()
            }
        }
    }

    private fun observeNetwork() {
        networkUtils.networkDataStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                prepareViewLayout(false)
                prepareView()
            } else {
                prepareViewLayout(false)
                snackBar(binding.root, getString(R.string.no_inet_text)).show()
            }
        }
    }

    private val getSharedPermission by lazy {
        requireContext().getSharedPreferences(Constant.PERMISSIONS_STORAGE, Context.MODE_PRIVATE)
            .getBoolean(Constant.PERMISSIONS_STORAGE, false)
    }
}