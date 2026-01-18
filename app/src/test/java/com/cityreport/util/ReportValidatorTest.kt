package com.cityreport.util

import com.cityreport.domain.model.ReportCategory
import org.junit.Test
import org.junit.Assert.*

class ReportValidatorTest {

    @Test
    fun `valid title should pass validation`() {
        // Given
        val title = "Valid Title"

        // When
        val result = ReportValidator.isValidTitle(title)

        // Then
        assertTrue(result)
    }

    @Test
    fun `empty title should fail validation`() {
        // Given
        val title = ""

        // When
        val result = ReportValidator.isValidTitle(title)

        // Then
        assertFalse(result)
    }

    @Test
    fun `blank title should fail validation`() {
        // Given
        val title = "   "

        // When
        val result = ReportValidator.isValidTitle(title)

        // Then
        assertFalse(result)
    }

    @Test
    fun `valid description should pass validation`() {
        // Given
        val description = "Valid description"

        // When
        val result = ReportValidator.isValidDescription(description)

        // Then
        assertTrue(result)
    }

    @Test
    fun `empty description should fail validation`() {
        // Given
        val description = ""

        // When
        val result = ReportValidator.isValidDescription(description)

        // Then
        assertFalse(result)
    }

    @Test
    fun `blank description should fail validation`() {
        // Given
        val description = "   "

        // When
        val result = ReportValidator.isValidDescription(description)

        // Then
        assertFalse(result)
    }

    @Test
    fun `valid coordinates should pass validation`() {
        // Given
        val latitude = 48.8566
        val longitude = 2.3522

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertTrue(result)
    }

    @Test
    fun `invalid latitude above 90 should fail`() {
        // Given
        val latitude = 91.0
        val longitude = 2.3522

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertFalse(result)
    }

    @Test
    fun `invalid latitude below minus 90 should fail`() {
        // Given
        val latitude = -91.0
        val longitude = 2.3522

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertFalse(result)
    }

    @Test
    fun `invalid longitude above 180 should fail`() {
        // Given
        val latitude = 48.8566
        val longitude = 181.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertFalse(result)
    }

    @Test
    fun `invalid longitude below minus 180 should fail`() {
        // Given
        val latitude = 48.8566
        val longitude = -181.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertFalse(result)
    }

    @Test
    fun `boundary latitude 90 should pass`() {
        // Given
        val latitude = 90.0
        val longitude = 0.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertTrue(result)
    }

    @Test
    fun `boundary latitude minus 90 should pass`() {
        // Given
        val latitude = -90.0
        val longitude = 0.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertTrue(result)
    }

    @Test
    fun `boundary longitude 180 should pass`() {
        // Given
        val latitude = 0.0
        val longitude = 180.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertTrue(result)
    }

    @Test
    fun `boundary longitude minus 180 should pass`() {
        // Given
        val latitude = 0.0
        val longitude = -180.0

        // When
        val result = ReportValidator.isValidCoordinates(latitude, longitude)

        // Then
        assertTrue(result)
    }

    @Test
    fun `all categories should be valid`() {
        // Given / When / Then
        ReportCategory.entries.forEach { category ->
            assertTrue(ReportValidator.isValidCategory(category))
        }
    }

    @Test
    fun `validateReport with valid data should return success`() {
        // Given
        val title = "Valid Title"
        val description = "Valid Description"
        val latitude = 48.8566
        val longitude = 2.3522

        // When
        val (isValid, error) = ReportValidator.validateReport(title, description, latitude, longitude)

        // Then
        assertTrue(isValid)
        assertNull(error)
    }

    @Test
    fun `validateReport with empty title should return error`() {
        // Given
        val title = ""
        val description = "Valid Description"
        val latitude = 48.8566
        val longitude = 2.3522

        // When
        val (isValid, error) = ReportValidator.validateReport(title, description, latitude, longitude)

        // Then
        assertFalse(isValid)
        assertEquals("Le titre est obligatoire", error)
    }

    @Test
    fun `validateReport with null location should return error`() {
        // Given
        val title = "Valid Title"
        val description = "Valid Description"

        // When
        val (isValid, error) = ReportValidator.validateReport(title, description, null, null)

        // Then
        assertFalse(isValid)
        assertEquals("La localisation est obligatoire", error)
    }
}
