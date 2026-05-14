package com.vito.core.data.repository.impl

import com.vito.core.data.model.PackageDto
import com.vito.core.data.repository.PackageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageRepositoryImpl @Inject constructor() : PackageRepository {
    private val packages = MutableStateFlow<List<PackageDto>>(emptyList())

    override fun getPackage(packageId: String): Flow<PackageDto?> {
        return MutableStateFlow(packages.value.find { it.id == packageId })
    }

    override fun getUserPackages(userId: String): Flow<List<PackageDto>> {
        return MutableStateFlow(packages.value.filter { it.userId == userId })
    }

    override fun getDriverPackages(driverId: String): Flow<List<PackageDto>> {
        return MutableStateFlow(packages.value.filter { it.driverId == driverId })
    }

    override fun getAvailablePackages(): Flow<List<PackageDto>> {
        return MutableStateFlow(packages.value.filter { it.status == "PENDING" })
    }

    override suspend fun sendPackage(pkg: PackageDto): Result<String> = try {
        val packageId = "pkg_${UUID.randomUUID()}"
        val newPkg = pkg.copy(id = packageId)
        packages.value = packages.value + newPkg
        Result.success(packageId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun acceptPackage(packageId: String, driverId: String): Result<Unit> = try {
        packages.value = packages.value.map {
            if (it.id == packageId) it.copy(driverId = driverId, status = "ACCEPTED")
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePackageStatus(packageId: String, status: String): Result<Unit> = try {
        packages.value = packages.value.map {
            if (it.id == packageId) it.copy(status = status)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deliverPackage(packageId: String, signatureUrl: String?, photoUrl: String?): Result<Unit> = try {
        packages.value = packages.value.map {
            if (it.id == packageId) it.copy(status = "DELIVERED", signatureUrl = signatureUrl, photoUrl = photoUrl)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun ratePackage(packageId: String, rating: Int, review: String?, tip: Double): Result<Unit> = try {
        packages.value = packages.value.map {
            if (it.id == packageId) it.copy(rating = rating, review = review, tip = tip)
            else it
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getCurrentUserId(): String? = null
}