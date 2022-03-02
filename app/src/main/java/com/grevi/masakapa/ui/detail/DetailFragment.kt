package com.grevi.masakapa.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.grevi.masakapa.R
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.databinding.FragmentDetailBinding
import com.grevi.masakapa.databinding.ItemCardBinding
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapter
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeLiveData
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant
import com.grevi.masakapa.util.Constant.ONE_FLOAT
import com.grevi.masakapa.util.Constant.ONE_SECOND
import com.grevi.masakapa.util.Constant.TWO
import com.grevi.masakapa.util.Constant.TWO_SECOND
import com.grevi.masakapa.common.popup.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, RecipesViewModel>() {

    private lateinit var itemCardBinding: ItemCardBinding
    private val args : DetailFragmentArgs by navArgs()
    private val ingredientsAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }
    private val stepAdapter: StepAdapter by lazy { StepAdapter() }
    private val databaseViewModel : DatabaseViewModel by viewModels()

    private val getSharedPermission by lazy {
        baseContext.getSharedPreferences(Constant.PERMISSIONS_STORAGE, Context.MODE_PRIVATE)
            .getBoolean(Constant.PERMISSIONS_STORAGE, false)
    }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        observeRecipesDetail()
        prepareViewLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemCardBinding = ItemCardBinding.bind(binding.cardLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.bucket)?.isVisible = false
    }

    private fun observeRecipesDetail() = with(viewModels) {
        observeLiveData(getDetail(args.key)) { observeView(it) }
    }

    private fun observeView(response: DetailResponse) = with(binding) {
        imgDetail.load(args.thumb) {
            allowHardware(false)
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }

        response.results.let {
            recipeTitleText.text = it.name
            itemCardBinding.textDiffItem.text = it.dificulty
            itemCardBinding.textPortionItems.text = it.servings
            itemCardBinding.textTimesItem.text = it.times
            chefText.text = it.author.author
            publishedText.text = it.author.published
            prepareRV(it.ingredients, it.step)
        }

        pgMainRecipes.visibility = View.GONE

        floatButton.setOnClickListener {
            response.results.let { data ->
                if (getSharedPermission) {
                    observeChecker(data)
                }else {
                    snackBar(root, getString(R.string.bucket_permission_text)).show()
                }
            }
        }
    }

    private fun prepareRV(stringList: MutableList<String>, stepList : MutableList<String>)
    = with(binding) {
        rvIngredients.layoutManager = GridLayoutManager(context, TWO)
        rvIngredients.adapter = ingredientsAdapter
        ingredientsAdapter.addList(stringList)

        rvStep.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvStep.adapter = stepAdapter
        stepAdapter.addList(stepList)
    }

    private fun prepareViewLayout() = with(binding) {
        cardLayout.visibility = ViewGroup.VISIBLE
        cardLayout.animate().alpha(ONE_FLOAT).duration = ONE_SECOND
        floatButton.visibility = View.VISIBLE
        floatButton.animate().alpha(ONE_FLOAT).duration = TWO_SECOND
        textChefLayout.visibility = View.VISIBLE
        textChefLayout.animate().alpha(ONE_FLOAT).duration = ONE_SECOND
        textIngredientLabel.visibility = View.VISIBLE
        textIngredientLabel.animate().alpha(ONE_FLOAT)
        textStepLabel.visibility = View.VISIBLE
        textStepLabel.animate().alpha(ONE_FLOAT)
        recipeTitleText.visibility = View.VISIBLE
        recipeTitleText.animate().alpha(ONE_FLOAT)
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

    private fun prepareViewLayout(state : Boolean) = with(binding) {
        when (state) {
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
}
