package uj.roomme.app.fragments.shoppinglist

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uj.roomme.app.R
import uj.roomme.app.adapters.GridAdapter
import uj.roomme.app.consts.PermissionCheckers
import uj.roomme.app.fragments.shoppinglist.viewmodel.CompleteShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.ShoppingListService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class CompleteShoppingListFragment : Fragment(R.layout.fragment_complete_shoppinglist) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private val args: CompleteShoppingListFragmentArgs by navArgs()
    private val viewModel: CompleteShoppingListViewModel by viewModels {
        CompleteShoppingListViewModel.Factory(session, slService, args.listId)
    }

    private lateinit var takePhotoButton: ImageButton
    private lateinit var uploadPhotosButton: ImageButton
    private lateinit var completeButton: FloatingActionButton
    private lateinit var gridView: GridView
    private lateinit var contentResolver: ContentResolver
    private val uploadPhotosProvider = UploadPhotosProvider()
    private val takePhotoProvider = TakePhotoProvider()
    private lateinit var gridAdapter: GridAdapter
    private lateinit var uploadPhotosActivityResult: ActivityResultLauncher<Intent>
    private lateinit var takePhotoActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadPhotosActivityResult = uploadPhotosProvider.uploadPhotosResult()
        takePhotoActivityResult = takePhotoProvider.takePhotoResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        contentResolver = requireActivity().contentResolver
        gridAdapter = GridAdapter(requireContext(), emptyList())
        gridView.adapter = gridAdapter
        uploadPhotosButton.setOnClickListener(uploadPhotosProvider::uploadPhotos)
        takePhotoButton.setOnClickListener(takePhotoProvider::takePhoto)
        completeButton.setOnClickListener {
            val receipts = gridAdapter.getAllItems()
                .map { createMultipartBody(it.first, it.second) }
                .toList()
            viewModel.completeShoppingListViaService(receipts)
        }
        setUpNavigation(view)
    }

    private fun findViews(view: View) = view.apply {
        gridView = findViewById(R.id.gridViewReceipts)
        uploadPhotosButton = findViewById(R.id.buttonUploadFile)
        takePhotoButton = findViewById(R.id.buttonTakePhoto)
        completeButton = findViewById(R.id.buttonCompleteShoppingList)
    }

    private fun setUpNavigation(view: View) {
        val navController = findNavController()
        viewModel.completedShoppingListEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(
                CompleteShoppingListFragmentDirections.actionDestCompleteShoppingListFragmentToCompletedShoppingListFragment(
                    args.listId
                )
            )
        })
    }

    private fun createMultipartBody(bitmap: Bitmap, uri: Uri): MultipartBody.Part {
        val file = File(requireContext().cacheDir, uri.lastPathSegment)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()
        try {
            FileOutputStream(file).use {
                it.write(bitmapData)
                it.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val requestBody = RequestBody.create(MediaType.parse(contentResolver.getType(uri)), file)
        return MultipartBody.Part.createFormData("receiptFiles", file.name, requestBody)
    }

    private inner class UploadPhotosProvider {

        fun uploadPhotos(view: View?) {
            if (PermissionCheckers.isReadExternalStorageGranted(requireContext())) {
                // TODO check if that works
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100
                )
                return
            }

            val selectPhotosIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = "image/*"
            }

            uploadPhotosActivityResult.launch(selectPhotosIntent)
        }

        fun uploadPhotosResult(): ActivityResultLauncher<Intent> {
            return registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK)
                    return@registerForActivityResult

                val bitmaps = retrieveBitmaps(result)
                gridAdapter.addItems(bitmaps)
            }
        }

        private fun retrieveBitmaps(result: ActivityResult): List<Pair<Bitmap, Uri>> {
            val clipData = result.data!!.clipData

            return if (clipData == null) {
                val uri = result.data!!.data!!
                return listOf(retrieveBitmapPair(uri))

            } else {
                (0 until clipData.itemCount).map { clipData.getItemAt(it) }
                    .map { retrieveBitmapPair(it.uri) }
                    .toList()
            }
        }

        private fun retrieveBitmapPair(uri: Uri): Pair<Bitmap, Uri> {
            val stream = requireActivity().contentResolver.openInputStream(uri)
            return Pair(BitmapFactory.decodeStream(stream), uri)
        }
    }

    private inner class TakePhotoProvider {

        fun takePhotoResult(): ActivityResultLauncher<Intent> {
            return registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK)
                    return@registerForActivityResult

                val imageBitmap = retrieveBitmap(result)
                saveImageToGallery(imageBitmap)
//                gridAdapter.addItem(imageBitmap) TODO
            }
        }

        fun takePhoto(view: View?) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoActivityResult.launch(takePictureIntent)
        }

        private fun retrieveBitmap(result: ActivityResult): Bitmap {
            return result.data?.extras?.get("data") as Bitmap
        }

        private fun saveImageToGallery(bitmap: Bitmap) {
            try {
                val resolver = requireActivity().contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator + "PicturesFolder"
                    )
                }
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                val fos = resolver.openOutputStream(imageUri!!)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } catch (e: Exception) {
                Log.d("TAG", e.toString())
            }
        }
    }
}