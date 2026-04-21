package com.ejemplo.myapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.myapp.data.local.entities.UserEntity
import com.ejemplo.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.loginUser(email, password)
            if (user != null) {
                _currentUser.value = user
                _loginError.value = null
                onResult(true)
            } else {
                _loginError.value = "Credenciales incorrectas"
                onResult(false)
            }
        }
    }

    fun register(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.registerUser(name, email, password)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    suspend fun checkExistingSession(): UserEntity? {
        // En una app real, aquí usaríamos DataStore o SharedPreferences.
        // Como Room ya persiste al usuario, simplemente recuperamos el usuario existente.
        return repository.getCurrentUser().firstOrNull()
    }
}
