import { memo } from 'react'
import { formatProductPrice } from '../../products/model/productMapper'

export const CartPanel = memo(function CartPanel({
  items,
  onRemoveProduct,
  totalItems,
  totalPrice,
}) {
  return (
    <aside aria-label="Carrito de compras" className="cart-panel">
      <div className="cart-panel__header">
        <h2>Carrito</h2>
        <span aria-label={`${totalItems} productos en el carrito`}>
          {totalItems} items
        </span>
      </div>

      {items.length === 0 ? (
        <p className="cart-panel__empty">Agrega productos para ver el total.</p>
      ) : (
        <ul className="cart-list">
          {items.map((item) => (
            <li key={item.product.id}>
              <div>
                <strong>{item.product.title}</strong>
                <span>
                  {item.quantity} x {formatProductPrice(item.product)}
                </span>
              </div>
              <button
                aria-label={`Eliminar ${item.product.title} del carrito`}
                className="button button--ghost"
                onClick={() => onRemoveProduct(item.product.id)}
                type="button"
              >
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      )}

      <div className="cart-total">
        <span>Total</span>
        <strong>{formatProductPrice({ price: totalPrice })}</strong>
      </div>
    </aside>
  )
})
