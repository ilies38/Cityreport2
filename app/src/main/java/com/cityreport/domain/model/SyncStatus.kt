package com.cityreport.domain.model

/**
 * Statut de synchronisation d'un rapport
 */
enum class SyncStatus {
    PENDING,  // En attente de synchronisation
    SYNCED,   // Synchronise avec le serveur
    FAILED    // Echec de synchronisation
}
