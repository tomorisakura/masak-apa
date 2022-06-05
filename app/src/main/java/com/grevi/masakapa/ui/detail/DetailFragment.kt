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
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeLiveData
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.databinding.FragmentDetailBinding
import com.grevi.masakapa.databinding.ItemCardBinding
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapter
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant
import com.grevi.masakapa.util.Constant.TWO
import com.grevi.masakapa.util.hide
import com.grevi.masakapa.util.show
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
        hideViewComponent()
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
        pgMainRecipes.hide()
        showViewComponent()
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

    private fun showViewComponent() = with(binding) {
        cardLayout.show()
        floatButton.show()
        textChefLayout.show()
        textIngredientLabel.show()
        textStepLabel.show()
        recipeTitleText.show()
    }

    private fun hideViewComponent() = with(binding) {
        cardLayout.hide()
        floatButton.hide()
        textChefLayout.hide()
        textIngredientLabel.hide()
        textStepLabel.hide()
        recipeTitleText.hide()
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
}
