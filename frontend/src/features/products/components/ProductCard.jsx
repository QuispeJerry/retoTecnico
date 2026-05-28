import { memo } from 'react'
import { formatProductPrice, getProductImageUrl } from '../model/productMapper'

export const ProductCard = memo(function ProductCard({ onAddToCart, product }) {
  return (
    <article className="product-card">
      <img
        alt=""
        aria-hidden="true"
        className="product-card__image"
        height="72"
        src={getProductImageUrl(product.image)}
        width="72"
      />

      <div className="product-card__body">
        <span className="product-card__category">{product.category}</span>
        <h2>{product.title}</h2>
        <p>{formatProductPrice(product)}</p>
      </div>

      <button
        aria-label={`Agregar ${product.title} al carrito`}
        className="button button--primary"
        onClick={() => onAddToCart(product)}
        type="button"
      >
        Agregar
      </button>
    </article>
  )
})
