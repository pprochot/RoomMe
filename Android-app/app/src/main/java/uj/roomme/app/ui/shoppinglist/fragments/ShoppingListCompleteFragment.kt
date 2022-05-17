package uj.roomme.app.ui.shoppinglist.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uj.roomme.app.R
import uj.roomme.app.adapters.RemovableReceiptsAdapter
import uj.roomme.app.consts.PermissionCheckers
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentShoppinglistCompleteBinding
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListCompleteFragmentDirections.Companion.actionDestCompleteShoppingListFragmentToCompletedShoppingListFragment
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListCompleteViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.ShoppingListService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListCompleteFragment : Fragment(R.layout.fragment_shoppinglist_complete) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private val args: ShoppingListCompleteFragmentArgs by navArgs()
    private val viewModel: ShoppingListCompleteViewModel by viewModels {
        ShoppingListCompleteViewModel.Factory(session, slService, args.listId)
    }

    private lateinit var binding: FragmentShoppinglistCompleteBinding
    private lateinit var contentResolver: ContentResolver
    private val uploadPhotosProvider = UploadPhotosProvider()
    private val takePhotoProvider = TakePhotoProvider()
    private lateinit var receiptsAdapter: RemovableReceiptsAdapter
    private lateinit var uploadPhotosActivityResult: ActivityResultLauncher<Intent>
    private lateinit var takePhotoActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadPhotosActivityResult = uploadPhotosProvider.uploadPhotosResult()
        takePhotoActivityResult = takePhotoProvider.takePhotoResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistCompleteBinding.bind(view)
        contentResolver = requireActivity().contentResolver
        setUpNavigation()
        setUpHandleErrors()
        setUpGridView()
        setUpButtons()
    }

    private fun setUpButtons() {
        binding.buttonUploadFile.setOnClickListener { uploadPhotosProvider.uploadPhotos() }
        binding.buttonTakePhoto.setOnClickListener { takePhotoProvider.takePhoto() }
        binding.buttonCompleteShoppingList.setOnClickListener { button ->
            button.makeNotClickable()
            val receipts = receiptsAdapter.dataList
                .map { createMultipartBody(it.first, it.second) }
                .toList()
            viewModel.completeShoppingListViaService(receipts)
        }
    }

    private fun setUpGridView() {
        receiptsAdapter = RemovableReceiptsAdapter()
        binding.rvReceipts.layoutManager = LinearLayoutManager(context)
        binding.rvReceipts.adapter = receiptsAdapter
    }

    private fun setUpNavigation() {
        val navController = findNavController()
        viewModel.completedShoppingListEvent.observe(viewLifecycleOwner, EventObserver {

            navController.navigate(
                actionDestCompleteShoppingListFragmentToCompletedShoppingListFragment(args.listId)
            )
        })
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.buttonCompleteShoppingList.makeClickable()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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

        fun uploadPhotos() {
            if (PermissionCheckers.isReadExternalStorageGranted(requireContext())) {
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
                receiptsAdapter.addItems(bitmaps)
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

        private lateinit var currentPhotoPath: String
        private lateinit var photoUri: Uri
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")

        fun takePhoto() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "uj.roomme.app.fileprovider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            }

            takePhotoActivityResult.launch(takePictureIntent)
        }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            // Create an image file name
            val timeStamp: String = dateFormat.format(Date())
            val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                currentPhotoPath = absolutePath
            }
        }


        fun takePhotoResult(): ActivityResultLauncher<Intent> {
            return registerForActivityResult(StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK)
                    return@registerForActivityResult

                val bitmap = retrieveBitmap()
                receiptsAdapter.addAtLastPosition(Pair(bitmap, photoUri))
            }
        }

        private fun retrieveBitmap(): Bitmap {
            return MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, photoUri);
        }
    }
}