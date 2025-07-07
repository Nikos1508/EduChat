package com.example.educhat.ui.item

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.R
import com.example.educhat.SupabaseAuthViewModel
import com.example.educhat.data.model.UserState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: SupabaseAuthViewModel,
    navController: NavController,
    onSaveAvailable: (saveAction: () -> Unit) -> Unit
) {
    val userState by viewModel.userState
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userProfile by viewModel.userProfile

    var displayName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    fun parseColorOrDefault(hex: String?, default: Color): Color {
        return try {
            hex?.let { Color(it.toColorInt()) } ?: default
        } catch (e: Exception) {
            default
        }
    }

    var selectedColor by remember {
        mutableStateOf(parseColorOrDefault(userProfile?.displayNameColor, Color(0xFFFF5733)))
    }

    fun colorToHex(color: Color): String {
        val r = (color.red * 255).toInt()
        val g = (color.green * 255).toInt()
        val b = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", r, g, b)
    }

    LaunchedEffect(userProfile) {
        displayName = userProfile?.displayName ?: ""
        description = userProfile?.description ?: ""
        selectedColor = parseColorOrDefault(userProfile?.displayNameColor, Color(0xFFFF5733))
    }

    val saveProfileChanges = {
        scope.launch {
            viewModel.setUserState(UserState.Loading)

            var imageUpdateSuccess = true

            if (selectedImageUri != null) {
                viewModel.uploadAndSetProfileImage(selectedImageUri!!, context.contentResolver)
                selectedImageUri = null
            }

            val success = viewModel.updateProfile(
                newDisplayName = displayName.takeIf { it.isNotBlank() },
                newDescription = description.takeIf { it.isNotBlank() }
            )

            val colorHex = colorToHex(selectedColor)
            val colorUpdateSuccess = viewModel.updateThemeColor(colorHex)

            if (success && imageUpdateSuccess && colorUpdateSuccess) {
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                viewModel.loadUserProfile()
                navController.popBackStack()
            } else {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        onSaveAvailable {
            saveProfileChanges()
        }
    }

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
        ) {
            val imagePainter = rememberAsyncImagePainter(
                model = selectedImageUri ?: userProfile?.profileImageUrl ?: R.drawable.profile_image
            )

            Image(
                painter = imagePainter,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change Profile Image",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Choose Display Name Color", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        var selectedColor by remember { mutableStateOf(Color.Red) }

        ColorPickerSection(
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it }
        )
    }
}

@Composable
fun ColorPickerSection(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colorDialogState = rememberMaterialDialogState()

    Text(
        text = "Choose Display Name Color",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(selectedColor)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { colorDialogState.show() }
    )

    MaterialDialog(
        dialogState = colorDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        colorChooser(
            colors = listOf(
                Color.Red, Color.Green, Color.Blue, Color.Yellow,
                Color.Magenta, Color.Cyan, Color.Gray, Color.Black
            ),
            onColorSelected = { onColorSelected(it) }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}
