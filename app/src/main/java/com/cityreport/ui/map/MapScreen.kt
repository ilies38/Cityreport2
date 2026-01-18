package com.cityreport.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cityreport.R
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key

// Couleurs pour les marqueurs selon le statut
private val ColorPending = Color(0xFFFF9800)
private val ColorSynced = Color(0xFF4CAF50)
private val ColorFailed = Color(0xFFF44336)

// Position par defaut (Paris) si aucun rapport
private val DEFAULT_POSITION = LatLng(48.8566, 2.3522)
private const val DEFAULT_ZOOM = 12f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsState()

    // Rapport selectionne pour afficher l'InfoWindow personnalisee
    var selectedReport by remember { mutableStateOf<Report?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(DEFAULT_POSITION, DEFAULT_ZOOM)
    }

    // Met à jour la position de la caméra quand les rapports changent
    LaunchedEffect(reports) {
        if (reports.isNotEmpty()) {
            val firstReport = reports.first()
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(firstReport.latitude, firstReport.longitude),
                DEFAULT_ZOOM
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.map_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Carte Google Maps
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = false
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = false
                ),
                onMapClick = {
                    // Fermer l'InfoWindow quand on clique ailleurs
                    selectedReport = null
                }
            ) {
                // Afficher un marqueur pour chaque rapport avec clé stable
                reports.forEach { report ->
                    key(report.id) {
                        val position = remember(report.latitude, report.longitude) {
                            LatLng(report.latitude, report.longitude)
                        }
                        val markerState = remember(report.id) {
                            MarkerState(position = position)
                        }
                        val markerColor = remember(report.syncStatus) {
                            getMarkerColor(report.syncStatus)
                        }

                        Marker(
                            state = markerState,
                            title = report.title,
                            snippet = getCategoryLabelNonComposable(report.category),
                            icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                            onClick = {
                                selectedReport = report
                                true // Consommer l'evenement
                            }
                        )
                    }
                }
            }

            // Legende des couleurs en haut a droite
            MapLegend(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            // InfoWindow personnalisee en bas
            if (selectedReport != null) {
                ReportInfoCard(
                    report = selectedReport!!,
                    onViewDetails = {
                        onNavigateToDetails(selectedReport!!.id)
                    },
                    onDismiss = {
                        selectedReport = null
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }

            // Message si aucun rapport
            if (reports.isEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.map_empty),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapLegend(modifier: Modifier = Modifier) {
    val legendLabel = stringResource(R.string.map_legend)
    val syncedLabel = stringResource(R.string.sync_synced)
    val pendingLabel = stringResource(R.string.sync_pending)
    val failedLabel = stringResource(R.string.sync_failed)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = legendLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            LegendItem(color = ColorSynced, label = syncedLabel)
            LegendItem(color = ColorPending, label = pendingLabel)
            LegendItem(color = ColorFailed, label = failedLabel)
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Card(
            modifier = Modifier
                .width(12.dp)
                .height(12.dp),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {}
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun ReportInfoCard(
    report: Report,
    onViewDetails: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryLabel = getCategoryLabel(report.category)
    val syncLabel = getSyncStatusLabel(report.syncStatus)
    val viewDetailsLabel = stringResource(R.string.map_view_details)

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titre
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Categorie et statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge categorie
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = getCategoryColor(report.category).copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = categoryLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = getCategoryColor(report.category)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Badge statut
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = getSyncStatusColor(report.syncStatus).copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = syncLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = getSyncStatusColor(report.syncStatus)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description (apercu)
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bouton voir details
            Button(
                onClick = onViewDetails,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(viewDetailsLabel)
            }
        }
    }
}

// Fonction pour obtenir la couleur du marqueur selon le statut
private fun getMarkerColor(status: SyncStatus): Float {
    return when (status) {
        SyncStatus.PENDING -> BitmapDescriptorFactory.HUE_ORANGE
        SyncStatus.SYNCED -> BitmapDescriptorFactory.HUE_GREEN
        SyncStatus.FAILED -> BitmapDescriptorFactory.HUE_RED
    }
}

private fun getSyncStatusColor(status: SyncStatus): Color {
    return when (status) {
        SyncStatus.PENDING -> ColorPending
        SyncStatus.SYNCED -> ColorSynced
        SyncStatus.FAILED -> ColorFailed
    }
}

@Composable
private fun getSyncStatusLabel(status: SyncStatus): String {
    return when (status) {
        SyncStatus.PENDING -> stringResource(R.string.sync_pending)
        SyncStatus.SYNCED -> stringResource(R.string.sync_synced)
        SyncStatus.FAILED -> stringResource(R.string.sync_failed)
    }
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

// Version non-composable pour utilisation dans les marqueurs (contexte non-composable)
private fun getCategoryLabelNonComposable(category: ReportCategory): String {
    return when (category) {
        ReportCategory.CLEANLINESS -> "Cleanliness"
        ReportCategory.ROAD -> "Road"
        ReportCategory.LIGHTING -> "Lighting"
        ReportCategory.SAFETY -> "Safety"
        ReportCategory.OTHER -> "Other"
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
