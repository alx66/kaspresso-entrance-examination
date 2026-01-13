package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class CerealStorageImplTest {

    private lateinit var storage: CerealStorage
    private val containerCapacity = 10f
    private val storageCapacity = 20f

    @BeforeEach
    fun setUp() {
        storage = CerealStorageImpl(containerCapacity, storageCapacity)
    }

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `should add cereal to storage`() {
        val remaining = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(0f, remaining, 0.01f)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return remaining when container is full`() {
        val remaining = storage.addCereal(Cereal.RICE, 15f)
        assertEquals(5f, remaining, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should throw when adding negative amount`() {
        assertFailsWith<IllegalArgumentException> {
            storage.addCereal(Cereal.RICE, -1f)
        }
    }

    @Test
    fun `should throw when no space for new container`() {
        val smallStorage = CerealStorageImpl(containerCapacity = 10f, storageCapacity = 10f)

        // Заполняем хранилище первым контейнером
        smallStorage.addCereal(Cereal.RICE, 10f)
        // Пытаемся добавить второй контейнер, но места уже нет
        assertFailsWith<IllegalStateException> {
            smallStorage.addCereal(Cereal.BUCKWHEAT, 10f)
        }
    }

    @Test
    fun `should add to existing container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val remaining = storage.addCereal(Cereal.RICE, 3f)
        assertEquals(0f, remaining, 0.01f)
        assertEquals(8f, storage.getAmount(Cereal.RICE), 0.01f)
    }
}