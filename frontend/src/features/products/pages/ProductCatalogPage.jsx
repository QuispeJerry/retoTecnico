import { useCallback } from 'react'
import { CartPanel } from '../../cart/components/CartPanel.jsx'
import { useCart } from '../../cart/hooks/useCart'
import { ProductFilters } from '../components/ProductFilters.jsx'
import { ProductList } from '../components/ProductList.jsx'
import { useProductFilters } from '../hooks/useProductFilters'
import { useProducts } from '../hooks/useProducts'

export function ProductCatalogPage() {
  const { products, status } = useProducts()
  const {
    categories,
    filteredProducts,
    searchTerm,
    selectedCategory,
    setSearchTerm,
    setSelectedCategory,
  } = useProductFilters(products)
  const { addProduct, items, removeProduct, totalItems, totalPrice } = useCart()

  const handleAddToCart = useCallback(
    (product) => {
      addProduct(product)
    },
    [addProduct],
  )

  return (
    <div className="app-shell">
      <header className="hero-header">
        <div className="hero-header__content">
          <p>Scotiabank Store</p>
          <h1>Productos financieros para comparar y seleccionar</h1>
          <span>Catalogo local con carrito persistente</span>
        </div>
      </header>

      <main className="catalog-layout">
        <section className="catalog-content" aria-labelledby="products-title">
          <div className="section-heading">
            <div>
              <p>Catalogo</p>
              <h2 id="products-title">Listado de productos</h2>
            </div>
            <span aria-live="polite">{filteredProducts.length} resultados</span>
          </div>

          <ProductFilters
            categories={categories}
            onCategoryChange={setSelectedCategory}
            onSearchChange={setSearchTerm}
            searchTerm={searchTerm}
            selectedCategory={selectedCategory}
          />

          {status === 'loading' ? (
            <p className="loading-state" role="status">
              Cargando productos...
            </p>
          ) : (
            <ProductList onAddToCart={handleAddToCart} products={filteredProducts} />
          )}
        </section>

        <CartPanel
          items={items}
          onRemoveProduct={removeProduct}
          totalItems={totalItems}
          totalPrice={totalPrice}
        />
      </main>
    </div>
  )
}
