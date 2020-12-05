package drm.ezenglish.util

interface FirebaseCallback<T> {
    fun onCallback(value: T)
}