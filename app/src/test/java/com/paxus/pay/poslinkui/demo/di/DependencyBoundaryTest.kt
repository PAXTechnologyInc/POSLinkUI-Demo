package com.paxus.pay.poslinkui.demo.di

import com.paxus.pay.poslinkui.demo.data.EntryActionStateStore
import com.paxus.pay.poslinkui.demo.utils.AppExecutionCoordinator
import com.paxus.pay.poslinkui.demo.utils.BackgroundTaskRunner
import com.paxus.pay.poslinkui.demo.utils.ThreadPoolManager
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionCatalogRepository
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionFilterManager
import org.junit.Assert.assertEquals
import org.junit.Test

class DependencyBoundaryTest {
    @Test
    fun entryActionFilterManager_shouldDependOnAbstractions() {
        val constructor = EntryActionFilterManager::class.java.constructors.first()
        val parameters = constructor.parameterTypes.toList()
        assertEquals(
            listOf(EntryActionCatalogRepository::class.java, EntryActionStateStore::class.java),
            parameters
        )
    }

    @Test
    fun appExecutionCoordinator_shouldUseInjectableThreadPoolManager() {
        val constructor = AppExecutionCoordinator::class.java.constructors.first()
        assertEquals(
            listOf(
                ThreadPoolManager::class.java,
                BackgroundTaskRunner::class.java
            ),
            constructor.parameterTypes.toList()
        )
    }
}
