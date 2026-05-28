import { useCallback, useEffect, useMemo, useReducer } from 'react'
import { CartContext } from './cartContext'

const CART_STORAGE_KEY = 'scotiabank-products-cart'

function cartReducer(state, action) {
  switch (action.type) {
    case 'add': {
      const currentItem = state[action.product.id]
      return {
        ...state,
        [action.product.id]: {
          product: action.product,
          quantity: currentItem ? currentItem.quantity + 1 : 1,
        },
      }
    }
    case 'remove': {
      const nextState = { ...state }
      delete nextState[action.productId]
      return nextState
    }
    case 'hydrate':
      return action.cart
    default:
      return state
  }
}

function readStoredCart() {
  try {
    const storedCart = window.localStorage.getItem(CART_STORAGE_KEY)
    return storedCart ? JSON.parse(storedCart) : {}
  } catch {
    return {}
  }
}

export function CartProvider({ children }) {
  const [cart, dispatch] = useReducer(cartReducer, {}, readStoredCart)

  const addProduct = useCallback((product) => {
    dispatch({ type: 'add', product })
  }, [])

  const removeProduct = useCallback((productId) => {
    dispatch({ type: 'remove', productId })
  }, [])

  useEffect(() => {
    window.localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart))
  }, [cart])

  const value = useMemo(() => {
    const items = Object.values(cart)
    const totalItems = items.reduce((total, item) => total + item.quantity, 0)
    const totalPrice = items.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0,
    )

    return {
      addProduct,
      items,
      removeProduct,
      totalItems,
      totalPrice,
    }
  }, [addProduct, cart, removeProduct])

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}
