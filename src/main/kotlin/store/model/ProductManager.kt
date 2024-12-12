package store.model

class ProductManager {
    private val fileReader = FileReader(PRODUCT_FILE_PATH)
    val products = mutableListOf<Product>()

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
        val emptyOfRegularPriceProductsName = mutableListOf<String>()
        productsCount.forEach {
            if (it.value == 1) {
                emptyOfRegularPriceProductsName.add(it.key)
            }
        }
        updateEmptyOfRegularPriceProducts(emptyOfRegularPriceProductsName)
    }

    private fun updateEmptyOfRegularPriceProducts(productNames: List<String>) {
        val originalProducts = products.toList()
        originalProducts.forEachIndexed { index, product ->
            productNames.forEach { productName ->
                if (product.name == productName) {
                    products.add(index + 1, Product(product.name, product.price, 0, null))
                }
            }
        }
    }

    companion object {
        private const val PRODUCT_FILE_PATH = "src/main/resources/products.md"
    }
}