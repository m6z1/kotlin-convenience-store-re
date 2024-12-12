package store.model

class ProductManager {
    private val fileReader = FileReader(PRODUCT_FILE_PATH)
    private val products = mutableListOf<Product>()

    init {
        val fileLines = fileReader.readFile().drop(1)
        fileLines.forEach {
            val productData = it.split(",")
            products.add(
                Product(
                    productData[0],
                    productData[1].toInt(),
                    productData[2].toInt(),
                    productData[3],
                )
            )
        }
        checkNullPromotion()
        checkEmptyOfRegularPriceProduct()
    }

    private fun checkNullPromotion() {
        products.forEachIndexed { index, product ->
            if (product.promotion == "null") products[index] =
                Product(product.name, product.price, product.quantity, null)
        }
    }

    private fun checkEmptyOfRegularPriceProduct() {
        val productsCount = products.groupingBy { it.name }.eachCount()
        productsCount.forEach {
            if (it.value == 1) {
                addEmptyOfRegularPriceProduct(it.key)
            }
        }
    }

    private fun addEmptyOfRegularPriceProduct(productNames: String) {
        val product = products.find { it.name == productNames }!!
        val productIndex = products.indexOf(product)
        val addingEmptyOfRegularPriceProduct = Product(product.name, product.price, 0, null)

        if (product.promotion != null) {
            products.add(productIndex + 1, addingEmptyOfRegularPriceProduct)
        }
    }

    fun getProducts(): List<Product> {
        return products.map { it.copy() }.toList()
    }

    fun validateToBuy(productToBuy: ProductToBuy) {
        products.find { it.name == productToBuy.name }
            ?: throw IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.")

        var quantity = 0
        products.forEach { product ->
            if (product.name == productToBuy.name) {
                quantity += product.quantity
            }
        }
        if (quantity < productToBuy.buyCount) throw IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
    }

    fun getPromotionProduct(productName: String): Product {
        return products.find { product -> product.name == productName && product.promotion != null }!!
    }

    fun getRegularPrice(productName: String): Int {
        return products.find { product -> product.name == productName && product.promotion == null }!!.price
    }

    companion object {
        private const val PRODUCT_FILE_PATH = "src/main/resources/products.md"
    }
}