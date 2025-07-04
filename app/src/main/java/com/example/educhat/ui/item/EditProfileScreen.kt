package com.example.educhat.ui.item

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.R
import com.example.educhat.SupabaseAuthViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: SupabaseAuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userProfile by viewModel.userProfile

    var displayName by remember { mutableStateOf(userProfile?.displayName ?: "") }
    var description by remember { mutableStateOf(userProfile?.description ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var isEditingName by remember { mutableStateOf(false) }
    var isEditingDesc by remember { mutableStateOf(false) }
    var isEditingImage by remember { mutableStateOf(false) }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val resultUri = UCrop.getOutput(it)
                selectedImageUri = resultUri
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val destinationUri = Uri.fromFile(
                File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
            )
            val uCrop = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
            cropLauncher.launch(uCrop.getIntent(context))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        val imagePainter = rememberAsyncImagePainter(
            model = selectedImageUri ?: userProfile?.profileImageUrl ?: R.drawable.profile_image,
        )

        Image(
            painter = imagePainter,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .padding(4.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Change Profile Image")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isEditingName) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        } else {
            Text("Name: ${userProfile?.displayName ?: "N/A"}")
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = { isEditingName = true }) {
                Text("Edit Display Name")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditingDesc) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
            )
        } else {
            Text("Description: ${userProfile?.description ?: "N/A"}")
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = { isEditingDesc = true }) {
                Text("Edit Description")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    var uploadedImageUrl: String? = null

                    if (selectedImageUri != null) {
                        uploadedImageUrl = viewModel.uploadProfileImage(
                            selectedImageUri!!,
                            context.contentResolver
                        )
                    }

                    val success = viewModel.updateProfile(
                        newDisplayName = if (displayName != userProfile?.displayName) displayName else null,
                        newDescription = if (description != userProfile?.description) description else null,
                        newImageUrl = uploadedImageUrl
                    )

                    if (success) {
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        viewModel.loadUserProfile()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}