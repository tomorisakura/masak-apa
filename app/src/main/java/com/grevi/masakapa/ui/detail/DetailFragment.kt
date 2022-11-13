package com.grevi.masakapa.ui.detail

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.grevi.masakapa.R
import com.grevi.masakapa.common.base.BaseActivity
import com.grevi.masakapa.common.base.BaseFragment
import com.grevi.masakapa.common.base.observeLiveData
import com.grevi.masakapa.common.coroutine.runTask
import com.grevi.masakapa.common.permission.storagePermission
import com.grevi.masakapa.common.popup.snackBar
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.databinding.FragmentDetailBinding
import com.grevi.masakapa.databinding.ItemCardBinding
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.ui.adapter.IngredientsAdapter
import com.grevi.masakapa.ui.adapter.StepAdapter
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.util.Constant.TWO
import com.grevi.masakapa.util.hide
import com.grevi.masakapa.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, RecipesViewModel>() {

    private lateinit var itemCardBinding: ItemCardBinding
    private val args: DetailFragmentArgs by navArgs()
    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    private val stepAdapter: StepAdapter by lazy { StepAdapter() }

    override fun getViewModelClass(): Class<RecipesViewModel> = RecipesViewModel::class.java

    override fun getViewBindingInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun subscribeUI() {
        getDetailRecipes()
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

    private fun getDetailRecipes() = runTask {
        viewModel.getDetail(args.key)
        viewModel.keyChecker(args.key)
    }

    private fun observeRecipesDetail() = with(viewModel) {
        observeLiveData(recipesDetail) { initView(it) }
    }

    private fun initView(response: DetailResponse) = with(binding) {
        imgDetail.load(args.thumb) {
            allowHardware(false)
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }

        recipeTitleText.text = response.results.name
        itemCardBinding.textDiffItem.text = response.results.difficulty
        itemCardBinding.textPortionItems.text = response.results.servings
        itemCardBinding.textTimesItem.text = response.results.times
        chefText.text = response.results.author.author
        publishedText.text = response.results.author.published
        prepareRV(response.results.ingredients, response.results.step)

        pgMainRecipes.hide()
        showViewComponent()
        floatButton.setOnClickListener {
            (activity as BaseActivity<*>).storagePermission(
                onGrant = {
                    viewModel.setStoragePermission(true)
                    observeChecker(response.results)
                },
                onDeny = {
                    snackBar(root, getString(R.string.bucket_permission_text)).show()
                }
            )
        }
    }

    private fun prepareRV(stringList: List<String>, stepList: List<String>) =
        with(binding) {
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

    private fun observeChecker(detail: Detail) = with(viewModel) {
        isExistFromDb.observe(viewLifecycleOwner) { isExist ->
            if (!isExist) {
                runTask { viewModel.insertRecipes(detail, args.key, args.thumb) }
                snackBar(binding.root, "${detail.name} ditambahkan di bucket !").show()
            } else {
                snackBar(binding.root, "${detail.name} sudah ada di bucket !").show()
            }
        }
    }
}
