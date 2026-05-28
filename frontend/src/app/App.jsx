import { CartProvider } from '../features/cart/context/CartContext.jsx'
import { ProductCatalogPage } from '../features/products/pages/ProductCatalogPage.jsx'
import './App.css'

export function App() {
  return (
    <CartProvider>
      <ProductCatalogPage />
    </CartProvider>
  )
}
