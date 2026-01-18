package com.cityreport.ui.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cityreport.R
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Couleurs pour les badges de statut
private val ColorPending = Color(0xFFFF9800)
private val ColorSynced = Color(0xFF4CAF50)
private val ColorFailed = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Bouton carte
                    IconButton(onClick = onNavigateToMap) {
                        Icon(Icons.Default.Map, contentDescription = stringResource(R.string.nav_map))
                    }
                    // Bouton filtres
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.home_filter_all))
                    }
                    // Menu dropdown pour filtres categorie
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.home_filter_all)) },
                            onClick = {
                                viewModel.filterByCategory(null)
                                showFilterMenu = false
                            }
                        )
                        ReportCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(getCategoryLabel(category)) },
                                onClick = {
                                    viewModel.filterByCategory(category)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        getCategoryIcon(category),
                                        contentDescription = null,
                                        tint = getCategoryColor(category)
                                    )
                                }
                            )
                        }
                    }
                    // Bouton parametres
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.nav_create))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barre de recherche
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onClear = { viewModel.clearSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Chips de filtres categorie (affichage horizontal)
            CategoryFilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.filterByCategory(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Contenu principal
            when {
                isLoading -> {
                    // Indicateur de chargement
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                reports.isEmpty() -> {
                    // Etat vide
                    EmptyState(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    // Liste des rapports
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(reports, key = { it.id }) { report ->
                            ReportCard(
                                report = report,
                                onClick = { onNavigateToDetails(report.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                        }
                        // Espace en bas pour le FAB
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text(stringResource(R.string.home_search_hint)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.action_cancel))
                }
            }
        },
        singleLine = true
    )
}

@Composable
private fun CategoryFilterChips(
    selectedCategory: ReportCategory?,
    onCategorySelected: (ReportCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLabel = stringResource(R.string.home_filter_all)
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip "Tous"
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text(allLabel) }
            )
        }
        // Chips pour chaque categorie
        items(ReportCategory.entries.toList()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(getCategoryLabel(category)) },
                leadingIcon = if (selectedCategory == category) {
                    {
                        Icon(
                            getCategoryIcon(category),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun ReportCard(
    report: Report,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icone de categorie
            Icon(
                imageVector = getCategoryIcon(report.category),
                contentDescription = null,
                tint = getCategoryColor(report.category),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenu principal
            Column(modifier = Modifier.weight(1f)) {
                // Titre
                Text(
                    text = report.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Description (max 2 lignes)
                Text(
                    text = report.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Ligne infos : categorie + date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge categorie
                    Text(
                        text = getCategoryLabel(report.category),
                        style = MaterialTheme.typography.labelSmall,
                        color = getCategoryColor(report.category)
                    )

                    // Date formatee
                    Text(
                        text = formatDate(report.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Badge statut de synchronisation
            SyncStatusBadge(status = report.syncStatus)
        }
    }
}

@Composable
private fun SyncStatusBadge(status: SyncStatus) {
    val pendingLabel = stringResource(R.string.sync_pending)
    val syncedLabel = stringResource(R.string.sync_synced)
    val failedLabel = stringResource(R.string.sync_failed)

    val (color, label) = when (status) {
        SyncStatus.PENDING -> ColorPending to pendingLabel
        SyncStatus.SYNCED -> ColorSynced to syncedLabel
        SyncStatus.FAILED -> ColorFailed to failedLabel
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Report,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_empty_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.home_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

// Formateur de date réutilisable pour éviter les allocations répétées
private val dateFormatter by lazy {
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
}

// Fonction utilitaire pour formater la date
private fun formatDate(timestamp: Long): String {
    return dateFormatter.format(Date(timestamp))
}

// Fonction utilitaire pour obtenir le label de la categorie
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

// Fonction utilitaire pour obtenir l'icone de la categorie
private fun getCategoryIcon(category: ReportCategory): ImageVector {
    return when (category) {
        ReportCategory.CLEANLINESS -> Icons.Default.Warning
        ReportCategory.ROAD -> Icons.Default.Warning
        ReportCategory.LIGHTING -> Icons.Default.FlashOn
        ReportCategory.SAFETY -> Icons.Default.Security
        ReportCategory.OTHER -> Icons.Default.MoreVert
    }
}

// Fonction utilitaire pour obtenir la couleur de la categorie
private fun getCategoryColor(category: ReportCategory): Color {
    return when (category) {
        ReportCategory.CLEANLINESS -> Color(0xFF4CAF50)
        ReportCategory.ROAD -> Color(0xFF795548)
        ReportCategory.LIGHTING -> Color(0xFFFFC107)
        ReportCategory.SAFETY -> Color(0xFFF44336)
        ReportCategory.OTHER -> Color(0xFF9E9E9E)
    }
}
