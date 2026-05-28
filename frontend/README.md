# Frontend - Catalogo de productos

SPA en React + Vite para listar productos desde `products.json`, buscar por nombre, filtrar por categoria y manejar un carrito persistente en `localStorage`.

## Ejecutar

```bash
npm install
npm run dev
```

Para validar:

```bash
npm run lint
npm run test
npm run build
```

## Decisiones tecnicas

- Se usa React con Vite y CSS simple.
- Los productos se cargan desde `src/data/products.json`.
- La latencia se simula en `localProductRepository.js` usando `setTimeout`.
- El carrito usa Context API + `useReducer` y persiste en `localStorage`.
- La busqueda y filtro usan `useMemo` para evitar recalculos innecesarios.
- Componentes de listado, filtros, cards y carrito usan `React.memo` donde aplica.
- Se incluyen roles, `aria-label`, `aria-live`, botones nativos y controles de formulario accesibles.

## Mejoras pendientes

- Agregar manejo visual de errores al cargar productos.
- Agregar decremento de cantidad sin eliminar todo el producto.
- Agregar pruebas para remover productos y restaurar carrito desde `localStorage`.
