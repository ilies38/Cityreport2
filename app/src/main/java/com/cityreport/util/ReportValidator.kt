package com.cityreport.util

import com.cityreport.domain.model.ReportCategory

/**
 * Objet utilitaire pour la validation des rapports
 */
object ReportValidator {

    /**
     * Valide le titre du rapport
     * @param title Le titre a valider
     * @return true si le titre est valide (non vide)
     */
    fun isValidTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    /**
     * Valide la description du rapport
     * @param description La description a valider
     * @return true si la description est valide (non vide)
     */
    fun isValidDescription(description: String): Boolean {
        return description.isNotBlank()
    }

    /**
     * Valide les coordonnees GPS
     * @param latitude La latitude (-90 a 90)
     * @param longitude La longitude (-180 a 180)
     * @return true si les coordonnees sont valides
     */
    fun isValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 && longitude in -180.0..180.0
    }

    /**
     * Valide la categorie du rapport
     * @param category La categorie a valider
     * @return true si la categorie est valide
     */
    fun isValidCategory(category: ReportCategory): Boolean {
        return true // Toutes les valeurs de l'enum sont valides
    }

    /**
     * Valide un rapport complet
     * @return Pair avec (isValid, errorMessage)
     */
    fun validateReport(
        title: String,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Pair<Boolean, String?> {
        return when {
            !isValidTitle(title) -> Pair(false, "Le titre est obligatoire")
            !isValidDescription(description) -> Pair(false, "La description est obligatoire")
            latitude == null || longitude == null -> Pair(false, "La localisation est obligatoire")
            !isValidCoordinates(latitude, longitude) -> Pair(false, "Coordonnees invalides")
            else -> Pair(true, null)
        }
    }
}
