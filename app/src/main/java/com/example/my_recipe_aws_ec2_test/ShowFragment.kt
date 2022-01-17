package com.example.my_recipe_aws_ec2_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.my_recipe_aws_ec2_test.databinding.ShowFragmentBinding

class ShowFragment : Fragment() {

    private lateinit var binding: ShowFragmentBinding
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            viewModel.getRecipe()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShowFragmentBinding
            .inflate(layoutInflater, container, false)

        setupEditButton()
        loadRecipe()

        return binding.root
    }

    private fun setupEditButton() {

        binding.edit.setOnClickListener {

            val action = ShowFragmentDirections
                .actionShowFragmentToEditFragment()
            Navigation.findNavController(it).navigate(action)

        }

    }

    private fun loadRecipe() {
        val titleObserver = Observer<String> {
            binding.title.text = it
        }

        val descriptionObserver = Observer<String> {
            binding.description.text = it
        }

        val imageObserver = Observer<String> {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.error)
                .into(binding.image)
        }

        viewModel.title.observe(viewLifecycleOwner, titleObserver)
        viewModel.description.observe(viewLifecycleOwner, descriptionObserver)
        viewModel.image.observe(viewLifecycleOwner, imageObserver)

    }

}