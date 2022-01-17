package com.example.my_recipe_aws_ec2_test

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.my_recipe_aws_ec2_test.databinding.EditFragmentBinding
import kotlinx.coroutines.launch

private const val TAG = "EditFragment"

class EditFragment : Fragment() {

    private lateinit var binding: EditFragmentBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var image: ImageView

    private val titleTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            viewModel.title.value = p0.toString()
        }
    }

    private val descriptionTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            viewModel.description.value = p0.toString()
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Glide.with(this)
                .asBitmap()
                .load(uri)
                .centerInside()
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        @Nullable transition: Transition<in Bitmap?>?
                    ) {
                        val filename = context?.let { ctx ->
                            GetFilenameFromUriUtil.queryName(
                                ctx.contentResolver, uri
                            )
                        }

                        image.setImageBitmap(resource)

                        if (filename != null) {
                            viewModel.apply {
                                imageFileName = filename
                                uploadImage =
                                    BitmapToStringConverter.getStringFromBitmap(resource).toString()
                            }
                        }

                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditFragmentBinding
            .inflate(layoutInflater, container, false)

        image = binding.image

        loadRecipe()
        setUpImageLoadButton()
        setUpSubmitButton()

        binding.editTitle.addTextChangedListener(titleTextWatcher)
        binding.editDescription.addTextChangedListener(descriptionTextWatcher)

        return binding.root
    }

    private fun setUpSubmitButton() {
        binding.submit.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addRecipe()
            }
            Log.d(TAG, "title ready to send is: " + viewModel.title.value)
            Log.d(TAG, "description ready to send is: " + viewModel.description.value)
            Log.d(TAG, "upload image file name is: " + viewModel.imageFileName)
        }
    }

    private fun setUpImageLoadButton() {
        binding.loadImageButton.setOnClickListener {
            selectImageFromGalleryResult.launch("image/*")
        }
    }

    private fun loadRecipe() {
        binding.editTitle.setText(viewModel.title.value)
        binding.editDescription.setText(viewModel.description.value)
        Glide.with(this)
            .load(viewModel.image)
            .placeholder(R.drawable.default_image)
            .error(R.drawable.error)
            .into(binding.image)
    }

}