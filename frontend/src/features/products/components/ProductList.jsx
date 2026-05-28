import { memo } from 'react'
import { ProductCard } from './ProductCard.jsx'

export const ProductList = memo(function ProductList({ onAddToCart, products }) {
  if (products.length === 0) {
    return (
      <p className="empty-state" role="status">
        No se encontraron productos con los filtros aplicados.
      </p>
    )
  }

  return (
    <section aria-label="Listado de productos" className="product-grid">
      {products.map((product) => (
        <ProductCard key={product.id} onAddToCart={onAddToCart} product={product} />
      ))}
    </section>
  )
})
