package com.paxus.pay.poslinkui.demo.entry

/**
 * Records lifecycle-sensitive registrations (observer/receiver/task) so cleanup symmetry can be audited.
 */
class LifecycleBindingRegistry {
    data class Binding(
        val owner: String,
        val type: String,
        val registerPoint: String,
        var unregisterPoint: String? = null
    ) {
        val isSymmetric: Boolean
            get() = !unregisterPoint.isNullOrBlank()
    }

    private val bindings = linkedMapOf<String, Binding>()

    fun register(key: String, owner: String, type: String, registerPoint: String) {
        bindings[key] = Binding(owner = owner, type = type, registerPoint = registerPoint)
    }

    fun unregister(key: String, unregisterPoint: String) {
        bindings[key]?.unregisterPoint = unregisterPoint
    }

    fun isSymmetric(key: String): Boolean = bindings[key]?.isSymmetric == true

    fun snapshot(): List<Binding> = bindings.values.toList()

    fun clear() {
        bindings.clear()
    }
}
