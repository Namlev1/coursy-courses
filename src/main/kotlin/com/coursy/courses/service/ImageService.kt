package com.coursy.courses.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.coursy.courses.failure.Failure
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class ImagesService(
    private val minIoService: MinIOService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun uploadImage(
        file: MultipartFile,
        courseId: UUID
    ): Either<Failure, Unit> {
        val tempFile = Files.createTempFile("image", ".jpg")
        return try {
            file.transferTo(tempFile)
            uploadImageFromPath(tempFile, courseId)
        } finally {
            Files.deleteIfExists(tempFile)
        }
    }

    fun getImage(
        courseId: UUID
    ): Either<Failure, InputStream> {
        return minIoService.getFileStream("$courseId/image.jpg")
            .mapLeft { failure ->
                logger.error("Failed to download image: ${failure.message()}")
                failure
            }
    }

    private fun uploadImageFromPath(
        inputFile: Path,
        platformId: UUID
    ): Either<Failure, Unit> {
        return Files.newInputStream(inputFile).use { inputStream ->
            minIoService.uploadFile(
                "$platformId/image.jpg",
                inputStream,
                "image/jpeg",
                Files.size(inputFile)
            ).fold(
                { failure ->
                    logger.error("Failed to upload image: ${failure.message()}")
                    failure.left()
                },
                { Unit.right() }
            )
        }
    }
}