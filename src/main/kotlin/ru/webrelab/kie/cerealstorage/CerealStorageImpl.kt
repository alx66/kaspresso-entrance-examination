package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val containerExists = storage.containsKey(cereal)
        if (!containerExists) {
            val maxContainers = (storageCapacity / containerCapacity).toInt()
            if (storage.size >= maxContainers) {
                throw IllegalStateException("Недостаточно места в хранилище для нового контейнера")
            }
        }

        val currentAmount = storage[cereal] ?: 0f
        val newAmount = currentAmount + amount
        val amountToStore = minOf(newAmount, containerCapacity)
        val remaining = newAmount - amountToStore

        storage[cereal] = amountToStore
        return remaining
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val currentAmount = storage[cereal] ?: return 0f

        val taken = minOf(amount, currentAmount)
        storage[cereal] = currentAmount - taken
        return taken
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal] ?: return false
        if (currentAmount != 0f) return false

        storage.remove(cereal)
        return true
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal]
            ?: throw IllegalStateException("Контейнер с крупой $cereal не найден")

        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        if (storage.isEmpty()) return "Хранилище пусто"

        return storage.entries.joinToString("\n") { (cereal, amount) ->
            "${cereal.local}: $amount из $containerCapacity"
        }
    }
}
