package com.cityreport.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cityreport.R
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Couleurs pour les badges de statut
private val ColorPending = Color(0xFFFF9800)
private val ColorSynced = Color(0xFF4CAF50)
private val ColorFailed = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    reportId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val report by viewModel.report.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRetrying by viewModel.isRetrying.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()

    // Etat pour le dialog photo plein ecran
    var showPhotoDialog by remember { mutableStateOf(false) }
    // Etat pour le dialog de confirmation de suppression
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Redirection apres suppression
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onNavigateBack()
        }
    }

    // Dialog de confirmation de suppression
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.details_delete_confirm_title)) },
            text = { Text(stringResource(R.string.details_delete_confirm_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteReport()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.action_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                actions = {
                    // Bouton Modifier
                    IconButton(
                        onClick = { report?.let { onNavigateToEdit(it.id) } },
                        enabled = report != null
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(R.string.details_edit),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    // Bouton Supprimer
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        enabled = report != null
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.details_delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                // Ecran de chargement
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            report == null -> {
                // Rapport non trouve
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.error),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            else -> {
                // Contenu du rapport
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Photo du rapport (si disponible)
                    if (report!!.photoUrl != null) {
                        AsyncImage(
                            model = report!!.photoUrl,
                            contentDescription = stringResource(R.string.create_photo),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clickable { showPhotoDialog = true },
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Carte informations principales
                        ReportInfoCard(report = report!!)

                        // Carte localisation avec mini carte
                        LocationCard(report = report!!)

                        // Bouton retry si echec de synchronisation
                        if (report!!.syncStatus == SyncStatus.FAILED) {
                            RetryButton(
                                isRetrying = isRetrying,
                                onRetry = { viewModel.retrySync() }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                // Dialog photo plein ecran
                if (showPhotoDialog && report!!.photoUrl != null) {
                    PhotoFullScreenDialog(
                        photoUrl = report!!.photoUrl!!,
                        onDismiss = { showPhotoDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportInfoCard(report: Report) {
    val categoryLabel = getCategoryLabel(report.category)
    val syncPendingLabel = stringResource(R.string.sync_pending)
    val syncSyncedLabel = stringResource(R.string.sync_synced)
    val syncFailedLabel = stringResource(R.string.sync_failed)
    val dateLabel = stringResource(R.string.details_date)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Titre
            Text(
                text = report.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Badge statut de synchronisation
            SyncStatusBadge(
                status = report.syncStatus,
                pendingLabel = syncPendingLabel,
                syncedLabel = syncSyncedLabel,
                failedLabel = syncFailedLabel
            )

            // Description
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Separateur visuel
            Spacer(modifier = Modifier.height(8.dp))

            // Categorie avec icone
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = getCategoryIcon(report.category),
                    contentDescription = null,
                    tint = getCategoryColor(report.category),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = categoryLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = getCategoryColor(report.category),
                    fontWeight = FontWeight.Medium
                )
            }

            // Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = dateLabel + " ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDate(report.timestamp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LocationCard(report: Report) {
    val position = LatLng(report.latitude, report.longitude)
    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition.fromLatLngZoom(position, 15f)
    }
    val locationLabel = stringResource(R.string.details_location)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titre section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = locationLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mini carte Google Maps
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = position),
                    title = report.title
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Coordonnees
            Text(
                text = "Latitude : %.6f".format(report.latitude),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Longitude : %.6f".format(report.longitude),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SyncStatusBadge(
    status: SyncStatus,
    pendingLabel: String,
    syncedLabel: String,
    failedLabel: String
) {
    val (color, label, icon) = when (status) {
        SyncStatus.PENDING -> Triple(ColorPending, pendingLabel, "...")
        SyncStatus.SYNCED -> Triple(ColorSynced, syncedLabel, "...")
        SyncStatus.FAILED -> Triple(ColorFailed, failedLabel, "...")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RetryButton(
    isRetrying: Boolean,
    onRetry: () -> Unit
) {
    val syncingLabel = stringResource(R.string.details_syncing)
    val retryLabel = stringResource(R.string.details_retry_sync)

    Button(
        onClick = onRetry,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isRetrying,
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorFailed
        )
    ) {
        if (isRetrying) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(syncingLabel)
        } else {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(retryLabel)
        }
    }
}

@Composable
private fun PhotoFullScreenDialog(
    photoUrl: String,
    onDismiss: () -> Unit
) {
    val closeLabel = stringResource(R.string.action_cancel)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() }
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = stringResource(R.string.create_photo),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            // Bouton fermer
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = closeLabel,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// Fonctions utilitaires
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
private fun getCategoryLabel(category: ReportCategory): String {
    return when (category) {
        ReportCategory.CLEANLINESS -> stringResource(R.string.category_cleanliness)
        ReportCategory.ROAD -> stringResource(R.string.category_road)
        ReportCategory.LIGHTING -> stringResource(R.string.category_lighting)
        ReportCategory.SAFETY -> stringResource(R.string.category_safety)
        ReportCategory.OTHER -> stringResource(R.string.category_other)
    }
}

private fun getCategoryIcon(category: ReportCategory): ImageVector {
    return when (category) {
        ReportCategory.CLEANLINESS -> Icons.Default.Warning
        ReportCategory.ROAD -> Icons.Default.Warning
        ReportCategory.LIGHTING -> Icons.Default.FlashOn
        ReportCategory.SAFETY -> Icons.Default.Security
        ReportCategory.OTHER -> Icons.Default.MoreVert
    }
}

private fun getCategoryColor(category: ReportCategory): Color {
    return when (category) {
        ReportCategory.CLEANLINESS -> Color(0xFF4CAF50)
        ReportCategory.ROAD -> Color(0xFF795548)
        ReportCategory.LIGHTING -> Color(0xFFFFC107)
        ReportCategory.SAFETY -> Color(0xFFF44336)
        ReportCategory.OTHER -> Color(0xFF9E9E9E)
    }
}
