import products from '../../../data/Products.json'

const PRODUCT_LOAD_DELAY_MS = 450

export function getLocalProducts() {
  return new Promise((resolve) => {
    window.setTimeout(() => {
      resolve(products)
    }, PRODUCT_LOAD_DELAY_MS)
  })
}
