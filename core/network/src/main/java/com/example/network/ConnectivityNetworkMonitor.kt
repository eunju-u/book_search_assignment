package com.example.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.domain.network.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityNetworkMonitor @Inject constructor(
    @ApplicationContext context: Context,
) : NetworkMonitor {
    private val manager = context.getSystemService(ConnectivityManager::class.java)

    override val isOnline: Flow<Boolean> = callbackFlow {
        val lock = Any()
        var receivedCallback = false
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                synchronized(lock) {
                    receivedCallback = true
                    // 연결 여부는 이어서 전달되는 capabilities로 판단함
                }
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                synchronized(lock) {
                    receivedCallback = true
                    trySend(capabilities.isValidated())
                }
            }

            override fun onLost(network: Network) {
                synchronized(lock) {
                    receivedCallback = true
                    trySend(false)
                }
            }
        }
        manager.registerDefaultNetworkCallback(callback)
        // 콜백 밖에서 최초 상태만 조회. 먼저 도착한 이벤트를 덮어쓰지 않음
        synchronized(lock) {
            if (!receivedCallback) {
                val network = manager.activeNetwork
                val capabilities = network?.let { manager.getNetworkCapabilities(it) }
                trySend(capabilities.isValidated())
            }
        }
        awaitClose { manager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged().conflate()
}

private fun NetworkCapabilities?.isValidated(): Boolean =
    this != null && hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
