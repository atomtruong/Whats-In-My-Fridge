package com.example.myfridge.ui.home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myfridge.R
import com.example.myfridge.data.database.FridgeItemInfo
import com.example.myfridge.databinding.AddFoodBinding
import com.example.myfridge.ui.database.DatabaseViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddFoodFragment : Fragment() {
    private val viewModel: DatabaseViewModel.FridgeItemInfoViewModel by viewModels()
    private var _binding: AddFoodBinding? = null
    private lateinit var editName: EditText
    private lateinit var editExpiration: DatePicker

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddFoodBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //viewModel.addFridgeItemInfo(FridgeItemInfo("","Apple","3/20/2023"))
        //val recipe = this.requireArguments().getSerializable("recipe") as RecipeItem

        editName = binding.editName
        editExpiration = binding.editDate

        val submitButton = _binding!!.submitButton
        val tempUri = initURI()
        registrationLauncher(tempUri)
        //This will save the value they enter. we can then create a food out of it
        val name = editName.text.toString()

        //val expirationDay = editExpiration.text.toString()
        submitButton.setOnClickListener {
            val name = editName.text.toString()

            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val calendar = Calendar.getInstance()
            calendar.set(editExpiration.year, editExpiration.month, editExpiration.dayOfMonth)
            val expiration = calendar.timeInMillis
            val bitmap = (binding.addPicture.drawable as BitmapDrawable).bitmap
            val byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream)
            val blob = byteStream.toByteArray()
            viewModel.addFridgeItemInfo(FridgeItemInfo(blob, name, expiration))
            findNavController().navigate(R.id.nav_home)
        }
        return root
    }

    private fun initURI(): Uri {
        val imageDir = File(
            (activity as AppCompatActivity).applicationContext.filesDir, getString(R.string.image_dir)
        )
        imageDir.mkdir()
        val tempImageItem = File(
            imageDir, getString(R.string.image_file)
        )
        return FileProvider.getUriForFile(
            (activity as AppCompatActivity).applicationContext, getString(R.string.provider), tempImageItem
        )
    }

    private fun registrationLauncher(path: Uri){
        val container = binding.addPicture
        val launcher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            container.setImageBitmap(null)
            val source = ImageDecoder.createSource(requireActivity().contentResolver, path)
            val bitmap = ImageDecoder.decodeBitmap(source)

            container.setImageBitmap(bitmap)
        }
        container.setOnClickListener {
            launcher.launch(path)
        }
    }
}

