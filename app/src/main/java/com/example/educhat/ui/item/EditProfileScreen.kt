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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.educhat.R
import com.example.educhat.SupabaseAuthViewModel
import com.example.educhat.data.model.UserState
import com.vanpra.composematerialdialogs.MaterialDialog
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

    val profileUpdatedMsg = stringResource(R.string.profile_updated)
    val profileUpdateFailedMsg = stringResource(R.string.failed_to_update_profile)

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
                Toast.makeText(context, profileUpdatedMsg, Toast.LENGTH_SHORT).show()
                viewModel.loadUserProfile()
                navController.popBackStack()
            } else {
                Toast.makeText(context, profileUpdateFailedMsg, Toast.LENGTH_SHORT).show()
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
                contentDescription = stringResource(R.string.profile_image),
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
                        contentDescription = stringResource(R.string.change_profile_image),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text(stringResource(R.string.display_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(32.dp))

        var selectedColor by remember { mutableStateOf(Color.Red) }

        ColorPickerSection(
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it }
        )
    }
}

@Composable
fun ColorPickerSection(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    var red by remember { mutableStateOf((selectedColor.red * 255).toInt()) }
    var green by remember { mutableStateOf((selectedColor.green * 255).toInt()) }
    var blue by remember { mutableStateOf((selectedColor.blue * 255).toInt()) }

    val currentColor = Color(red, green, blue)

    Text(
        text = stringResource(R.string.choose_display_name_color),
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
            .clickable { dialogState.show() }
    )

    MaterialDialog(
        dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        buttons = {
            positiveButton(stringResource(R.string.ok)) { onColorSelected(currentColor) }
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            SpectrumSlider(stringResource(R.string.red), red, Color.Red) { red = it }
            SpectrumSlider(stringResource(R.string.green), green, Color.Green) { green = it }
            SpectrumSlider(stringResource(R.string.blue), blue, Color.Blue) { blue = it }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(currentColor)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SpectrumSlider(label: String, value: Int, color: Color, onValueChange: (Int) -> Unit) {
    Column {
        Text("$label: $value", style = MaterialTheme.typography.bodyMedium)

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..255f,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp), // Thicker
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    brush = when (label) {
                        stringResource(R.string.red) -> Brush.horizontalGradient(colors = (0..255).map { Color(it, 0, 0) })
                        stringResource(R.string.green) -> Brush.horizontalGradient(colors = (0..255).map { Color(0, it, 0) })
                        stringResource(R.string.blue) -> Brush.horizontalGradient(colors = (0..255).map { Color(0, 0, it) })
                        else -> Brush.horizontalGradient(listOf(Color.Black, Color.White))
                    }
                )
        )
    }
}