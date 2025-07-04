package com.example.educhat.ui.item

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.SupabaseAuthViewModel
import kotlinx.coroutines.launch

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

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

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
            model = selectedImageUri ?: userProfile?.profileImageUrl
        )
        Image(
            painter = imagePainter,
            contentDescription = "Profile Image",
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Choose Profile Image")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

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
                        newDisplayName = displayName,
                        newDescription = description,
                        newImageUrl = uploadedImageUrl
                    )

                    if (success) {
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        viewModel.loadUserProfile()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
