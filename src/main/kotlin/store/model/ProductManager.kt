package store.model

class ProductManager {
    private val fileReader = FileReader(PRODUCT_FILE_PATH)
    private val products = mutableListOf<Product>()

    init {
        val fileLines = fileReader.readFile().drop(0)
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
                updateEmptyOfRegularPriceProduct(productName = it.key)
            }
        }
    }

    private fun updateEmptyOfRegularPriceProduct(productName: String) {
        products.forEachIndexed { index, product ->
            if (product.name == productName) {
                products.add(index + 1, Product(product.name, product.price, 0, null))
            }
        }
    }

    companion object {
        private const val PRODUCT_FILE_PATH = "src/resources/products.md"
    }
}