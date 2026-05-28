export function formatProductPrice(product) {
  return new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
  }).format(Number(product.price || 0))
}

export function getProductImageUrl(image) {
  if (image.startsWith('/')) {
    return image
  }

  return new URL(`../../../image/icons/${image}`, import.meta.url).href
}

export function formatProductDate(value) {
  if (!value) {
    return '-'
  }

  return new Intl.DateTimeFormat('es-PE', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(new Date(value))
}
