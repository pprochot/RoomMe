package uj.roomme.app.fragments.shoppinglists

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.getExternalFilesDirs
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.services.service.ShoppingListService
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CompleteShoppingListFragment: Fragment(R.layout.fragment_complete_shoppinglist) {

    @Inject
    lateinit var shoppingListService: ShoppingListService
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    private lateinit var takePhotoButton: ImageButton
    private lateinit var receiptImage: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        findViews(view)
//
//        takePhotoButton.setOnClickListener {
//            dispatchTakePictureIntent()
//        }
    }

//    private fun findViews(view: View) = view.apply {
//        takePhotoButton = findViewById(R.id.buttonTakePhoto)
////        receiptImage = findViewById(R.id.imageReceipt)
//    }
//
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e: ActivityNotFoundException) {
//            Toasts.sendingRequestFailure(context) //TODO create new
//            // display error state to the user
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            receiptImage.setImageBitmap(imageBitmap)
//            saveImageToGallery(imageBitmap)
//        }
//    }
//
//    fun saveImageToGallery(bitmap: Bitmap) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                val resolver = requireActivity().contentResolver
//                val contentValues = ContentValues()
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg")
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "PicturesFolder")
//                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                val fos = resolver.openOutputStream(imageUri!!)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                /*
//                */
//                val file = imageUri.toFile()
//                val multiPartyBody = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(
//                    MediaType.parse("image/*"), file))
//                shoppingListService.setShoppingListAsCompleted("", 1, listOf(multiPartyBody))
//                    .processAsync { i, shoppingListCompletionPatchReturnModel, throwable ->
//                        Log.d("TAG", i.toString())
//                        Log.d("TAG", shoppingListCompletionPatchReturnModel.toString())
//                        Log.d("TAG", throwable.toString())
//                    }
//                /*
//                 */
//                Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT)
//            }
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Image not saved!", Toast.LENGTH_SHORT)
//            Log.d("TAG", e.toString())
//        }
//    }
//
//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    ...
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.example.android.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
//            }
//        }
//    }
}